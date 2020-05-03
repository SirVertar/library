package com.jakuszko.mateusz.library.controller;

import com.jakuszko.mateusz.library.domain.TitleDto;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import com.jakuszko.mateusz.library.service.facade.LibraryServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/title")
public class TitleController {

    private final LibraryServiceFacade libraryServiceFacade;

    @Autowired
    public TitleController(LibraryServiceFacade libraryServiceFacade) {
        this.libraryServiceFacade = libraryServiceFacade;
    }

    @GetMapping
    public List<TitleDto> get() {
        return libraryServiceFacade.getTitles();
    }

    @GetMapping("/{id}")
    public TitleDto get(@PathVariable Long id) throws TitleNotFoundException {
        return libraryServiceFacade.getTitle(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody TitleDto titleDto) {
        libraryServiceFacade.createTitle(titleDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws TitleNotFoundException {
        libraryServiceFacade.deleteTitle(id);
    }

    @PutMapping
    public void update(@RequestBody TitleDto titleDto) {
        libraryServiceFacade.updateTitle(titleDto);
    }

    @GetMapping("/copiesOf/{id}")
    public Long getQuantityOfAvailableCopiesOfTitle(@PathVariable Long id) throws TitleNotFoundException {
        return libraryServiceFacade.getQuantityOfAvailableCopiesOfTitle(id);
    }
}
