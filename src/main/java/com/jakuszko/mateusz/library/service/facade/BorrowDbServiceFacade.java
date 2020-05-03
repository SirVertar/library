package com.jakuszko.mateusz.library.service.facade;

import com.jakuszko.mateusz.library.domain.*;
import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundException;
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
public class BorrowDbServiceFacade {

    private final BorrowDbService borrowDbService;
    private final BorrowMapper borrowMapper;
    private final ReaderDbService readerDbService;
    private final ReaderMapper readerMapper;
    private final CopyDbService copyDbService;
    private final CopyMapper copyMapper;
    private final TitleDbService titleDbService;
    private final TitleMapper titleMapper;

    @Autowired
    public BorrowDbServiceFacade(BorrowDbService borrowDbService, BorrowMapper borrowMapper,
                                 ReaderDbService readerDbService, ReaderMapper readerMapper,
                                 CopyDbService copyDbService, CopyMapper copyMapper,
                                 TitleDbService titleDbService, TitleMapper titleMapper) {
        this.borrowDbService = borrowDbService;
        this.borrowMapper = borrowMapper;
        this.readerDbService = readerDbService;
        this.readerMapper = readerMapper;
        this.copyDbService = copyDbService;
        this.copyMapper = copyMapper;
        this.titleDbService = titleDbService;
        this.titleMapper = titleMapper;
    }

    public List<BorrowDto> getBorrows() {
        List<Borrow> borrows = borrowDbService.getBorrowList();
        List<CopyDto> copyDtos = getCopyDtos(borrows);
        return borrowMapper.mapToBorrowDtoList(borrows, copyDtos);
    }

    public BorrowDto getBorrow(Long id) {
        Borrow borrow = borrowDbService.getBorrow(id).orElseThrow(BootstrapMethodError::new);
        List<CopyDto> copyDtos = getCopyDtos(borrow);
        return borrowMapper.mapToBorrowDto(borrow, copyDtos);
    }

    public void createBorrow(BorrowDto borrowDto) throws ReaderNotFoundException {
        List<Long> copiesIdsList = borrowDto.getCopies().stream()
                .map(CopyDto::getId).collect(Collectors.toList());
        Reader reader = readerDbService.getReader(borrowDto.getReaderId()).orElseThrow(ReaderNotFoundException::new);
        List<Copy> copies = copyDbService.getCopiesByIdList(copiesIdsList);
        borrowDbService.create(borrowMapper.mapToBorrow(borrowDto, copies, reader));
    }

    public void updateBorrow(@RequestBody BorrowDto borrowDto) throws ReaderNotFoundException {
        List<Long> copiesIdsList = borrowDto.getCopies().stream()
                .map(CopyDto::getId).collect(Collectors.toList());
        Reader reader = readerDbService.getReader(borrowDto.getReaderId()).orElseThrow(ReaderNotFoundException::new);
        List<Copy> copies = copyDbService.getCopiesByIdList(copiesIdsList);
        borrowDbService.update(borrowMapper.mapToBorrow(borrowDto, copies, reader));
    }

    public void deleteBorrow(Long id) {
        Optional<Borrow> borrow = borrowDbService.getBorrow(id);
        if (borrow.isPresent()) {
            borrow.get().getCopies().forEach(e -> e.setBorrow(null));
            borrow.get().setCopies(null);
            borrowDbService.delete(id);
        }
    }

    private List<CopyDto> getCopyDtos(Borrow borrow) {
        List<Long> copiesIdsList = borrow.getCopies().stream().map(Copy::getId).collect(Collectors.toList());
        List<Copy> copies = copyDbService.getCopiesByIdList(copiesIdsList);
        List<Title> titles = titleDbService.getTitlesByCopyLists(copies);
        List<TitleDto> titleDtos = titleMapper.mapToTitleDtoList(titles);
        return copyMapper.mapToCopyDtoList(copies, titleDtos);
    }

    private List<CopyDto> getCopyDtos(List<Borrow> borrows) {
        List<Long> copiesIdsList = borrows.stream().flatMap(borrow -> borrow.getCopies().stream())
                .map(Copy::getId).collect(Collectors.toList());
        List<Copy> copies = copyDbService.getCopiesByIdList(copiesIdsList);
        List<Title> titles = titleDbService.getTitlesByCopyLists(copies);
        List<TitleDto> titleDtos = titleMapper.mapToTitleDtoList(titles);
        return copyMapper.mapToCopyDtoList(copies, titleDtos);
    }
}
