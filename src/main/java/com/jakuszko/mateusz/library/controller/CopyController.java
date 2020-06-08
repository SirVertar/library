package com.jakuszko.mateusz.library.controller;

import com.jakuszko.mateusz.library.domain.CopyDto;
import com.jakuszko.mateusz.library.exceptions.BorrowNotFoundException;
import com.jakuszko.mateusz.library.exceptions.CopyNotFoundException;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import com.jakuszko.mateusz.library.facade.CopyServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("v1/copies")
public class CopyController {
    private final CopyServiceFacade copyServiceFacade;

    @GetMapping
    public List<CopyDto> get() {
        log.info("Get all copies");
        return copyServiceFacade.getCopies();
    }

    @GetMapping("/{id}")
    public CopyDto get(@PathVariable Long id) throws CopyNotFoundException, TitleNotFoundException {
        log.info("Get copy by id");
        return copyServiceFacade.getCopy(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody CopyDto copyDto) throws TitleNotFoundException, CopyNotFoundException {
        log.info("Create copy");
        copyServiceFacade.createCopy(copyDto);
    }

    @PutMapping
    public void update(@RequestBody CopyDto copyDto) throws BorrowNotFoundException, TitleNotFoundException,
            CopyNotFoundException {
        log.info("Update copy");
        copyServiceFacade.updateCopy(copyDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws CopyNotFoundException,
            TitleNotFoundException {
        log.info("Delete copy");
        copyServiceFacade.deleteCopy(id);
    }
}
