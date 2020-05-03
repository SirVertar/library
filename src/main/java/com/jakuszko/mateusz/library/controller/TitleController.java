package com.jakuszko.mateusz.library.controller;

import com.jakuszko.mateusz.library.domain.TitleDto;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import com.jakuszko.mateusz.library.service.facade.TitleDbServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/title")
public class TitleController {

    private final TitleDbServiceFacade titleDbServiceFacade;

    @Autowired
    public TitleController(TitleDbServiceFacade titleDbServiceFacade) {
        this.titleDbServiceFacade = titleDbServiceFacade;
    }

    @GetMapping
    public List<TitleDto> get() {
        return titleDbServiceFacade.getTitles();
    }

    @GetMapping("/{id}")
    public TitleDto get(@PathVariable Long id) throws TitleNotFoundException {
        return titleDbServiceFacade.getTitle(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody TitleDto titleDto) {
        titleDbServiceFacade.createTitle(titleDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws TitleNotFoundException {
        titleDbServiceFacade.deleteTitle(id);
    }

    @PutMapping
    public void update(@RequestBody TitleDto titleDto) throws TitleNotFoundException {
        titleDbServiceFacade.updateTitle(titleDto);
    }

    @GetMapping("/copiesOf/{id}")
    public Long getQuantityOfAvailableCopiesOfTitle(@PathVariable Long id) throws TitleNotFoundException {
        return titleDbServiceFacade.getQuantityOfAvailableCopiesOfTitle(id);
    }
}
