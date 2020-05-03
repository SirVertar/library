package com.jakuszko.mateusz.library.service.facade;

import com.jakuszko.mateusz.library.domain.*;
import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundException;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import com.jakuszko.mateusz.library.mapper.ReaderMapper;
import com.jakuszko.mateusz.library.service.BorrowDbService;
import com.jakuszko.mateusz.library.service.CopyDbService;
import com.jakuszko.mateusz.library.service.ReaderDbService;
import com.jakuszko.mateusz.library.service.TitleDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReaderDbServiceFacade {
    private final BorrowDbService borrowDbService;
    private final CopyDbService copyDbService;
    private final ReaderDbService readerDbService;
    private final TitleDbService titleDbService;
    private final ReaderMapper readerMapper;

    @Autowired
    public ReaderDbServiceFacade(BorrowDbService borrowDbService, CopyDbService copyDbService,
                                 ReaderDbService readerDbService, TitleDbService titleDbService, ReaderMapper readerMapper) {
        this.borrowDbService = borrowDbService;
        this.copyDbService = copyDbService;
        this.readerDbService = readerDbService;
        this.titleDbService = titleDbService;
        this.readerMapper = readerMapper;
    }

    public List<ReaderDto> getReaders () {
        return readerMapper.mapToReaderDtoList(readerDbService.getReaders());
    }
    public ReaderDto getReader(Long id) throws ReaderNotFoundException {
        return readerMapper.mapToReaderDto(readerDbService.getReader(id).orElseThrow(ReaderNotFoundException::new));
    }

    public void createReader(ReaderDto readerDto) throws ReaderNotFoundException {
        readerDbService.create(readerMapper.mapToReader(readerDto));
    }

    public void updateReader(ReaderDto readerDto) throws ReaderNotFoundException {
        readerDbService.update(readerMapper.mapToReader(readerDto));
    }

    public void deleteReader(Long id) throws ReaderNotFoundException {
        readerDbService.getReader(id).orElseThrow(ReaderNotFoundException::new)
                .getBorrowList().stream()
                .map(Borrow::getCopies)
                .forEach(copyDbService::setBorrowToNull);
        readerDbService.getReader(id).orElseThrow(ReaderNotFoundException::new)
                .getBorrowList().forEach(borrowDbService::setCopiesAndReaderToNull);
        readerDbService.getReader(id).get().getBorrowList()
                .forEach(e -> borrowDbService.delete(e.getId()));
        readerDbService.delete(id);
    }

    public void borrowBook(Long bookId, Long readerId) throws ReaderNotFoundException, TitleNotFoundException {
        Reader reader = readerDbService.getReader(readerId).orElseThrow(ReaderNotFoundException::new);
        Borrow borrow = Borrow.builder().reader(reader).build();
        Title title = titleDbService.getTitle(bookId).orElseThrow(TitleNotFoundException::new);
        if (title.getCopies().stream().anyMatch(e -> !e.getIsBorrowed())) {
            borrow.setCopies(title.getCopies().stream()
                    .filter(e -> title.getCopies().size() != 0)
                    .filter(e -> !e.getIsBorrowed())
                    .collect(Collectors.toList()));
        }
        reader.getBorrowList().add(borrow);
        borrow.getCopies().forEach(e -> e.setIsBorrowed(true));
        borrow.getCopies().forEach(e -> e.setBorrow(borrow));
        List<Copy> copies = borrow.getCopies();

        borrowDbService.create(borrow);
        copies.forEach(copyDbService::update);
        readerDbService.update(reader);
    }
}
