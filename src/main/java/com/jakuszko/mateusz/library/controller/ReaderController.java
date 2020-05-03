package com.jakuszko.mateusz.library.controller;

import com.jakuszko.mateusz.library.domain.ReaderDto;
import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundException;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import com.jakuszko.mateusz.library.service.facade.ReaderDbServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/reader")
public class ReaderController {

    private final ReaderDbServiceFacade readerDbServiceFacade;

    @Autowired
    public ReaderController(ReaderDbServiceFacade readerDbServiceFacade) {
        this.readerDbServiceFacade = readerDbServiceFacade;
    }

    @GetMapping
    public List<ReaderDto> get() {
        return readerDbServiceFacade.getReaders();
    }

    @GetMapping("/{id}")
    public ReaderDto get(@PathVariable Long id) throws ReaderNotFoundException {
        return readerDbServiceFacade.getReader(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody ReaderDto readerDto) throws ReaderNotFoundException {
        readerDbServiceFacade.createReader(readerDto);
    }

    @PutMapping
    public void update(@RequestBody ReaderDto readerDto) throws ReaderNotFoundException {
        readerDbServiceFacade.updateReader(readerDto);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws ReaderNotFoundException {
        readerDbServiceFacade.deleteReader(id);
    }

    @PutMapping("/borrow/{bookId}/by/{readerId}")
    public void borrowBook(@PathVariable Long bookId, @PathVariable Long readerId) throws ReaderNotFoundException, TitleNotFoundException {
        readerDbServiceFacade.borrowBook(bookId, readerId);
    }
}
