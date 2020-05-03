package com.jakuszko.mateusz.library.controller;

import com.jakuszko.mateusz.library.domain.CopyDto;
import com.jakuszko.mateusz.library.exceptions.BorrowNotFoundException;
import com.jakuszko.mateusz.library.exceptions.CopyNotFoundException;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import com.jakuszko.mateusz.library.service.facade.CopyDbServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/copy")
public class CopyController {
    private final CopyDbServiceFacade copyDbServiceFacade;

    @Autowired
    public CopyController(CopyDbServiceFacade copyDbServiceFacade) {
        this.copyDbServiceFacade = copyDbServiceFacade;
    }

    @GetMapping
    public List<CopyDto> get() {
        return copyDbServiceFacade.getCopies();
    }

    @GetMapping("/{id}")
    public CopyDto get(@PathVariable Long id) throws CopyNotFoundException, TitleNotFoundException {
        return copyDbServiceFacade.getCopy(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody CopyDto copyDto) throws TitleNotFoundException, CopyNotFoundException {
        copyDbServiceFacade.createCopy(copyDto);
    }

    @PutMapping
    public void update(@RequestBody CopyDto copyDto) throws BorrowNotFoundException, TitleNotFoundException, CopyNotFoundException {
        copyDbServiceFacade.updateCopy(copyDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws CopyNotFoundException, BorrowNotFoundException, TitleNotFoundException {
        copyDbServiceFacade.deleteCopy(id);
    }
}
