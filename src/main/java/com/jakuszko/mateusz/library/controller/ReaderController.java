package com.jakuszko.mateusz.library.controller;

import com.jakuszko.mateusz.library.domain.ReaderDto;
import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundException;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import com.jakuszko.mateusz.library.service.facade.LibraryServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/reader")
public class ReaderController {

    private final LibraryServiceFacade libraryServiceFacade;

    @Autowired
    public ReaderController(LibraryServiceFacade libraryServiceFacade) {
        this.libraryServiceFacade = libraryServiceFacade;
    }

    @GetMapping
    public List<ReaderDto> get() {
        return libraryServiceFacade.getReaders();
    }

    @GetMapping("/{id}")
    public ReaderDto get(@PathVariable Long id) throws ReaderNotFoundException {
        return libraryServiceFacade.getReader(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody ReaderDto readerDto) throws ReaderNotFoundException {
        libraryServiceFacade.createReader(readerDto);
    }

    @PutMapping
    public void update(@RequestBody ReaderDto readerDto) throws ReaderNotFoundException {
        libraryServiceFacade.updateReader(readerDto);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws ReaderNotFoundException {
        libraryServiceFacade.deleteReader(id);
    }

    @PutMapping("/borrow/{bookId}/by/{readerId}")
    public void borrowBook(@PathVariable Long bookId, @PathVariable Long readerId) throws ReaderNotFoundException, TitleNotFoundException {
        libraryServiceFacade.borrowBook(bookId, readerId);
    }
}
