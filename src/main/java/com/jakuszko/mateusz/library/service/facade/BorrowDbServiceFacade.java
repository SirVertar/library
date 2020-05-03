package com.jakuszko.mateusz.library.service.facade;

import com.jakuszko.mateusz.library.domain.Borrow;
import com.jakuszko.mateusz.library.domain.BorrowDto;
import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundException;
import com.jakuszko.mateusz.library.mapper.BorrowMapper;
import com.jakuszko.mateusz.library.service.BorrowDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class BorrowDbServiceFacade {

    private final BorrowDbService borrowDbService;
    private final BorrowMapper borrowMapper;

    @Autowired
    public BorrowDbServiceFacade(BorrowDbService borrowDbService, BorrowMapper borrowMapper) {
        this.borrowDbService = borrowDbService;
        this.borrowMapper = borrowMapper;
    }

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
}
