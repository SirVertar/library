package com.jakuszko.mateusz.library.controller;

import com.jakuszko.mateusz.library.domain.BorrowDto;
import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundException;
import com.jakuszko.mateusz.library.facade.BorrowServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("v1/borrows")
public class BorrowController {

    private final BorrowServiceFacade borrowServiceFacade;

    @GetMapping
    public List<BorrowDto> get() {
        log.info("Get all borrow books");
        return borrowServiceFacade.getBorrows();
    }

    @GetMapping("/{id}")
    public BorrowDto get(@PathVariable Long id) {
        log.info("Get borrow by id");
        return borrowServiceFacade.getBorrow(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody BorrowDto borrowDto) throws ReaderNotFoundException {
        log.info("crate borrow");
        borrowServiceFacade.createBorrow(borrowDto);
    }

    @PutMapping
    public void update(@RequestBody BorrowDto borrowDto) throws ReaderNotFoundException {
        log.info("Update borrow");
        borrowServiceFacade.updateBorrow(borrowDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Delete borrow with id: " + id);
        borrowServiceFacade.deleteBorrow(id);
    }
}
