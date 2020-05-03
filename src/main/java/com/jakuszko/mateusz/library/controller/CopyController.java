package com.jakuszko.mateusz.library.controller;

import com.jakuszko.mateusz.library.domain.CopyDto;
import com.jakuszko.mateusz.library.exceptions.BorrowNotFoundException;
import com.jakuszko.mateusz.library.exceptions.CopyNotFoundException;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import com.jakuszko.mateusz.library.service.facade.LibraryServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/copy")
public class CopyController {
    private final LibraryServiceFacade libraryServiceFacade;

    @Autowired
    public CopyController(LibraryServiceFacade libraryServiceFacade) {
        this.libraryServiceFacade = libraryServiceFacade;
    }

    @GetMapping
    public List<CopyDto> get() {
        return libraryServiceFacade.getCopies();
    }

    @GetMapping("/{id}")
    public CopyDto get(@PathVariable Long id) throws CopyNotFoundException {
        return libraryServiceFacade.getCopy(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody CopyDto copyDto) throws TitleNotFoundException {
        libraryServiceFacade.createCopy(copyDto);
    }

    @PutMapping
    public void update(@RequestBody CopyDto copyDto) throws BorrowNotFoundException {
        libraryServiceFacade.updateCopy(copyDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws CopyNotFoundException, BorrowNotFoundException, TitleNotFoundException {
        libraryServiceFacade.deleteCopy(id);
    }
}
