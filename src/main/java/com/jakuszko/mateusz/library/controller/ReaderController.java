package com.jakuszko.mateusz.library.controller;

import com.jakuszko.mateusz.library.domain.ReaderDto;
import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundException;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import com.jakuszko.mateusz.library.facade.ReaderServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("v1/readers")
public class ReaderController {

    private final ReaderServiceFacade readerServiceFacade;

    @GetMapping
    public List<ReaderDto> get() {
        log.info("Get all readers");
        return readerServiceFacade.getReaders();
    }

    @GetMapping("/{id}")
    public ReaderDto get(@PathVariable Long id) throws ReaderNotFoundException {
        log.info("Get reader by id");
        return readerServiceFacade.getReader(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody ReaderDto readerDto) throws ReaderNotFoundException {
        log.info("Create reader");
        readerServiceFacade.createReader(readerDto);
    }

    @PutMapping
    public void update(@RequestBody ReaderDto readerDto) throws ReaderNotFoundException {
        log.info("Update reader");
        readerServiceFacade.updateReader(readerDto);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws ReaderNotFoundException {
        log.info("Delete reader by id");
        readerServiceFacade.deleteReader(id);
    }

    @PostMapping("/borrows/{bookId}/readers/{readerId}")
    public void borrowBook(@PathVariable Long bookId, @PathVariable Long readerId) throws ReaderNotFoundException,
            TitleNotFoundException {
        log.info("Borrow book by bookId and readerId");
        readerServiceFacade.borrowBook(bookId, readerId);
    }
}
