package com.jakuszko.mateusz.library.controller;

import com.jakuszko.mateusz.library.domain.TitleDto;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import com.jakuszko.mateusz.library.facade.TitleServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("v1/titles")
public class TitleController {

    private final TitleServiceFacade titleServiceFacade;

    @GetMapping
    public List<TitleDto> get() {
        log.info("Get all titles");
        System.out.println(titleServiceFacade.getTitles().size());
        return titleServiceFacade.getTitles();
    }

    @GetMapping("/{id}")
    public TitleDto get(@PathVariable Long id) throws TitleNotFoundException {
        log.info("Get title by id");
        return titleServiceFacade.getTitle(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody TitleDto titleDto) {
        log.info("Create title");
        titleServiceFacade.createTitle(titleDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws TitleNotFoundException {
        log.info("Delete title by id");
        titleServiceFacade.deleteTitle(id);
    }

    @PutMapping
    public void update(@RequestBody TitleDto titleDto) throws TitleNotFoundException {
        log.info("Update title");
        titleServiceFacade.updateTitle(titleDto);
    }

    @GetMapping("/titles/{id}")
    public Long getQuantityOfAvailableCopiesOfTitle(@PathVariable Long id) throws TitleNotFoundException {
        return titleServiceFacade.getQuantityOfAvailableCopiesOfTitle(id);
    }
}
