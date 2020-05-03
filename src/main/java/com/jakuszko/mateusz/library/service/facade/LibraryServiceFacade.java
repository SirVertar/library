package com.jakuszko.mateusz.library.service.facade;

import com.jakuszko.mateusz.library.domain.*;
import com.jakuszko.mateusz.library.exceptions.BorrowNotFoundException;
import com.jakuszko.mateusz.library.exceptions.CopyNotFoundException;
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
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibraryServiceFacade {

    private final BorrowDbService borrowDbService;
    private final CopyDbService copyDbService;
    private final ReaderDbService readerDbService;
    private final TitleDbService titleDbService;
    private final CopyMapper copyMapper;
    private final ReaderMapper readerMapper;
    private final TitleMapper titleMapper;
    private final BorrowMapper borrowMapper;

    @Autowired
    public LibraryServiceFacade(BorrowDbService borrowDbService, CopyDbService copyDbService, ReaderDbService readerDbService,
                                TitleDbService titleDbService, CopyMapper copyMapper, ReaderMapper readerMapper,
                                TitleMapper titleMapper, BorrowMapper borrowMapper) {
        this.borrowDbService = borrowDbService;
        this.copyDbService = copyDbService;
        this.readerDbService = readerDbService;
        this.titleDbService = titleDbService;
        this.copyMapper = copyMapper;
        this.readerMapper = readerMapper;
        this.titleMapper = titleMapper;
        this.borrowMapper = borrowMapper;
    }

    @Autowired


    public List<BorrowDto> getBorrows() {
        return borrowMapper.mapToBorrowDtoList(borrowDbService.getBorrowList());
    }

    public BorrowDto getBorrow(Long id) {
        return borrowMapper.mapToBorrowDto(borrowDbService.getBorrow(id).orElseThrow(BootstrapMethodError::new));
    }

    public void createBorrow(BorrowDto borrowDto) throws ReaderNotFoundException {
            borrowDbService.create(borrowMapper.mapToBorrow(borrowDto));
    }

    public void updateBorrow(@RequestBody BorrowDto borrowDto) throws ReaderNotFoundException {
            borrowDbService.update(borrowMapper.mapToBorrow(borrowDto));
    }

    public void deleteBorrow(Long id) {
        Optional<Borrow> borrow = borrowDbService.getBorrow(id);
        if (borrow.isPresent()) {
            borrow.get().getCopies().forEach(e -> e.setBorrow(null));
            borrow.get().setCopies(null);
            borrowDbService.delete(id);
        }
    }

    public List<CopyDto> getCopies() {
        return copyMapper.mapToCopyDtoList(copyDbService.getCopies());
    }

    public CopyDto getCopy(Long id) throws CopyNotFoundException {
        return copyMapper.mapToCopyDto(copyDbService.getCopy(id).orElseThrow(CopyNotFoundException::new));
    }

    public void createCopy(CopyDto copyDto) throws TitleNotFoundException {
        copyDbService.create(copyMapper.mapToCopy(copyDto));
        Title title = titleDbService.getTitle(copyMapper.mapToCopy(copyDto).getTitle().getId()).orElseThrow(TitleNotFoundException::new);
        title.getCopies().add(copyMapper.mapToCopy(copyDto));
        titleDbService.update(title);
        Copy copy = copyMapper.mapToCopy(copyDto);
        copy.setTitle(title);
        copyDbService.create(copy);
    }

    public void deleteCopy( Long id) throws CopyNotFoundException, TitleNotFoundException {
        Copy copy = copyDbService.getCopy(id).orElseThrow(CopyNotFoundException::new);
        Title title = titleDbService.getTitle(copy.getTitle().getId()).orElseThrow(TitleNotFoundException::new);
        if (copy.getBorrow() != null && copy.getBorrow().getId() != -1L && borrowDbService.getBorrow(copy.getBorrow().getId()).isPresent()) {
            Borrow borrow = borrowDbService.getBorrow(copy.getBorrow().getId()).get();
            borrow.setCopies(null);
            borrowDbService.update(borrow);
        }
        title.setCopies(null);
        copy.setBorrow(null);
        copy.setTitle(null);
        titleDbService.update(title);
        copyDbService.update(copy);
        copyDbService.delete(id);
    }

    public void updateCopy(CopyDto copyDto) throws BorrowNotFoundException {
        Copy copy = copyMapper.mapToCopy(copyDto);
        copy.setBorrow(borrowDbService.getBorrow(copyDto.getBorrowId()).orElseThrow(BorrowNotFoundException::new));
        copyDbService.update(copy);
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

    public List<TitleDto> getTitles() {
        return titleMapper.mapToTitleDtoList(titleDbService.getTitles());
    }

    public TitleDto getTitle(Long id) throws TitleNotFoundException {
        return titleMapper.mapToTitleDto(titleDbService.getTitle(id).orElseThrow(TitleNotFoundException::new));
    }

    public void createTitle(@RequestBody TitleDto titleDto) {
        titleDbService.create(titleMapper.mapToTitle(titleDto));
    }

    public void deleteTitle(Long id) throws TitleNotFoundException {
        titleDbService.getTitle(id).orElseThrow(TitleNotFoundException::new).getCopies().stream()
                .filter(e ->copyDbService.getCopy(e.getId()).isPresent())
                .map(e -> copyDbService.getCopy(e.getId()).get().getId())
                .forEach(copyDbService::delete);
        titleDbService.update(id);
    }

    public void updateTitle(TitleDto titleDto) {
        titleDbService.create(titleMapper.mapToTitle(titleDto));
    }

    public Long getQuantityOfAvailableCopiesOfTitle(Long id) throws TitleNotFoundException {
        return titleDbService.getTitle(id).orElseThrow(TitleNotFoundException::new).getCopies().stream()
                .filter(e -> !e.getIsBorrowed())
                .count();
    }
}
