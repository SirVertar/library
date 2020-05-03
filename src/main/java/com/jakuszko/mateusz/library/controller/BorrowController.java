package com.jakuszko.mateusz.library.controller;

import com.jakuszko.mateusz.library.domain.BorrowDto;
import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundException;
import com.jakuszko.mateusz.library.service.facade.BorrowDbServiceFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/borrow")
public class BorrowController {

    private final BorrowDbServiceFacade borrowDbServiceFacade;

    @Autowired
    public BorrowController(BorrowDbServiceFacade borrowDbServiceFacade) {
        this.borrowDbServiceFacade = borrowDbServiceFacade;
    }

    @GetMapping
    public List<BorrowDto> get() {
        log.info("Get all borrow books");
        return borrowDbServiceFacade.getBorrows();
    }

    @GetMapping("/{id}")
    public BorrowDto get(@PathVariable Long id) {
        return borrowDbServiceFacade.getBorrow(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody BorrowDto borrowDto) throws ReaderNotFoundException {
        borrowDbServiceFacade.createBorrow(borrowDto);
    }

    @PutMapping
    public void update(@RequestBody BorrowDto borrowDto) throws ReaderNotFoundException {
        borrowDbServiceFacade.updateBorrow(borrowDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        borrowDbServiceFacade.deleteBorrow(id);
    }
}
