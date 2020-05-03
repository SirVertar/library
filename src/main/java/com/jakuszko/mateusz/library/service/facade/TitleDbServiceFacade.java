package com.jakuszko.mateusz.library.service.facade;

import com.jakuszko.mateusz.library.domain.Copy;
import com.jakuszko.mateusz.library.domain.TitleDto;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import com.jakuszko.mateusz.library.mapper.TitleMapper;
import com.jakuszko.mateusz.library.service.CopyDbService;
import com.jakuszko.mateusz.library.service.TitleDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TitleDbServiceFacade {
    private final CopyDbService copyDbService;
    private final TitleDbService titleDbService;
    private final TitleMapper titleMapper;

    @Autowired
    public TitleDbServiceFacade(CopyDbService copyDbService, TitleDbService titleDbService, TitleMapper titleMapper) {
        this.copyDbService = copyDbService;
        this.titleDbService = titleDbService;
        this.titleMapper = titleMapper;
    }

    public List<TitleDto> getTitles() {
        return titleMapper.mapToTitleDtoList(titleDbService.getTitles());
    }

    public TitleDto getTitle(Long id) throws TitleNotFoundException {
        return titleMapper.mapToTitleDto(titleDbService.getTitle(id).orElseThrow(TitleNotFoundException::new));
    }

    public void createTitle(@RequestBody TitleDto titleDto) {
        titleDbService.create(titleMapper.mapToTitle(titleDto,
                copyDbService.getCopies().stream()
                        .filter(e -> e.getTitle().getId().equals(titleDto.getId())).collect(Collectors.toList())));
    }

    public void deleteTitle(Long id) throws TitleNotFoundException {
        titleDbService.getTitle(id).orElseThrow(TitleNotFoundException::new).getCopies().stream()
                .filter(e ->copyDbService.getCopy(e.getId()).isPresent())
                .map(e -> copyDbService.getCopy(e.getId()).orElseThrow(ClassCastException::new).getId())
                .forEach(copyDbService::delete);
        titleDbService.update(id);
    }

    public void updateTitle(TitleDto titleDto) throws TitleNotFoundException {
        titleDbService.create(titleMapper.mapToTitle(titleDto, findListOfCopiesByTitleId(titleDto.getId())));
    }

    public Long getQuantityOfAvailableCopiesOfTitle(Long id) throws TitleNotFoundException {
        return titleDbService.getTitle(id).orElseThrow(TitleNotFoundException::new).getCopies().stream()
                .filter(e -> !e.getIsBorrowed())
                .count();
    }

    private List<Copy> findListOfCopiesByTitleId(Long titleID) throws TitleNotFoundException {
        return copyDbService.getCopiesByTitle(titleDbService.getTitle(titleID).orElseThrow(TitleNotFoundException::new));
    }
}
