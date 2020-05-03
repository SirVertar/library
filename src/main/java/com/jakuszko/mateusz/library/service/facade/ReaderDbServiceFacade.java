package com.jakuszko.mateusz.library.service.facade;

import com.jakuszko.mateusz.library.domain.*;
import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundException;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import com.jakuszko.mateusz.library.mapper.BorrowMapper;
import com.jakuszko.mateusz.library.mapper.CopyMapper;
import com.jakuszko.mateusz.library.mapper.ReaderMapper;
import com.jakuszko.mateusz.library.mapper.TitleMapper;
import com.jakuszko.mateusz.library.service.BorrowDbService;
import com.jakuszko.mateusz.library.service.CopyDbService;
import com.jakuszko.mateusz.library.service.ReaderDbService;
import com.jakuszko.mateusz.library.service.TitleDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReaderDbServiceFacade {
    private final BorrowDbService borrowDbService;
    private final CopyDbService copyDbService;
    private final ReaderDbService readerDbService;
    private final TitleDbService titleDbService;
    private final ReaderMapper readerMapper;
    private final BorrowMapper borrowMapper;
    private final CopyMapper copyMapper;
    private final TitleMapper titleMapper;

    @Autowired
    public ReaderDbServiceFacade(BorrowDbService borrowDbService, CopyDbService copyDbService,
                                 ReaderDbService readerDbService, TitleDbService titleDbService, ReaderMapper readerMapper,
                                 BorrowMapper borrowMapper, CopyMapper copyMapper, TitleMapper titleMapper) {
        this.borrowDbService = borrowDbService;
        this.copyDbService = copyDbService;
        this.readerDbService = readerDbService;
        this.titleDbService = titleDbService;
        this.readerMapper = readerMapper;
        this.borrowMapper = borrowMapper;
        this.copyMapper = copyMapper;
        this.titleMapper = titleMapper;
    }

    public List<ReaderDto> getReaders() {
        List<Reader> readers = readerDbService.getReaders();
        List<Borrow> borrows = borrowDbService.getBorrowListByReadersList(readers);
        List<Long> copiesIdsList = borrows.stream().filter(Objects::nonNull)
                .flatMap(borrow -> borrow.getCopies().stream()
                        .map(Copy::getId)).collect(Collectors.toList());
        List<Copy> copies = copyDbService.getCopiesByIdList(copiesIdsList);
        List<Title> titles = titleDbService.getTitlesByCopyLists(copies);
        List<TitleDto> titleDtos = titleMapper.mapToTitleDtoList(titles);
        List<CopyDto> copyDtos = copyMapper.mapToCopyDtoList(copies, titleDtos);
        List<BorrowDto> borrowDtos = borrowMapper.mapToBorrowDtoList(borrows, copyDtos);
        return readerMapper.mapToReaderDtoList(readers, borrowDtos);
    }

    public ReaderDto getReader(Long id) throws ReaderNotFoundException {
        Reader reader = readerDbService.getReader(id).orElseThrow(ReaderNotFoundException::new);
        List<Borrow> borrowDtos = borrowDbService.getBorrowListByReaderId(id);
        List<Copy> copies = copyDbService.getCopiesByReaderId(id);
        List<Title> titles = titleDbService.getTitlesByCopyLists(copies);
        List<CopyDto> copyDtos = copyMapper.mapToCopyDtoList(copies, titleMapper.mapToTitleDtoList(titles));
        return readerMapper.mapToReaderDto(reader, borrowMapper.mapToBorrowDtoList(borrowDtos, copyDtos));
    }

    public void createReader(ReaderDto readerDto) throws ReaderNotFoundException {
        readerDbService.create(readerMapper.mapToReader(readerDto, new ArrayList<Borrow>()));
    }

    public void updateReader(ReaderDto readerDto) throws ReaderNotFoundException {
        List<Borrow> borrows = borrowDbService.getBorrowListByReaderId(readerDto.getId());
        readerDbService.update(readerMapper.mapToReader(readerDto, borrows));
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
