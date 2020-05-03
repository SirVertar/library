package com.jakuszko.mateusz.library.service.facade;

import com.jakuszko.mateusz.library.domain.Borrow;
import com.jakuszko.mateusz.library.domain.Copy;
import com.jakuszko.mateusz.library.domain.CopyDto;
import com.jakuszko.mateusz.library.domain.Title;
import com.jakuszko.mateusz.library.exceptions.BorrowNotFoundException;
import com.jakuszko.mateusz.library.exceptions.CopyNotFoundException;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import com.jakuszko.mateusz.library.mapper.CopyMapper;
import com.jakuszko.mateusz.library.mapper.TitleMapper;
import com.jakuszko.mateusz.library.service.BorrowDbService;
import com.jakuszko.mateusz.library.service.CopyDbService;
import com.jakuszko.mateusz.library.service.TitleDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CopyDbServiceFacade {
    private final BorrowDbService borrowDbService;
    private final com.jakuszko.mateusz.library.service.CopyDbService copyDbService;
    private final TitleDbService titleDbService;
    private final CopyMapper copyMapper;
    private final TitleMapper titleMapper;

    @Autowired
    public CopyDbServiceFacade(BorrowDbService borrowDbService, CopyDbService copyDbService,
                               TitleDbService titleDbService, CopyMapper copyMapper, TitleMapper titleMapper) {
        this.borrowDbService = borrowDbService;
        this.copyDbService = copyDbService;
        this.titleDbService = titleDbService;
        this.copyMapper = copyMapper;
        this.titleMapper = titleMapper;
    }

    public List<CopyDto> getCopies() {
        return copyMapper.mapToCopyDtoList(copyDbService.getCopies(), titleMapper.mapToTitleDtoList(titleDbService.getTitles()));
    }

    public CopyDto getCopy(Long id) throws CopyNotFoundException, TitleNotFoundException {
        Copy copy = copyDbService.getCopy(id).orElseThrow(CopyNotFoundException::new);
        Title title = titleDbService.getTitle(copy.getTitle().getId()).orElseThrow(TitleNotFoundException::new);
        return copyMapper.mapToCopyDto(copy, titleMapper.mapToTitleDto(title));
    }

    public void createCopy(CopyDto copyDto) throws TitleNotFoundException, CopyNotFoundException {
        //copyDbService.create(copyMapper.mapToCopy(copyDto, getTitleByCopyDto(copyDto)));
        Title title = titleDbService.getTitle(copyMapper.mapToCopy(copyDto, getTitleByCopyDto(copyDto)).getTitle().getId()).orElseThrow(TitleNotFoundException::new);
        title.getCopies().add(copyMapper.mapToCopy(copyDto, getTitleByCopyDto(copyDto)));
        titleDbService.update(title);
        Copy copy = copyMapper.mapToCopy(copyDto, getTitleByCopyDto(copyDto));
        copy.setTitle(title);
        borrowDbService.update(copy.getBorrow());
        copyDbService.create(copy);
    }

    public void deleteCopy( Long id) throws CopyNotFoundException, TitleNotFoundException {
        Copy copy = copyDbService.getCopy(id).orElseThrow(CopyNotFoundException::new);
        Title title = titleDbService.getTitle(copy.getTitle().getId()).orElseThrow(TitleNotFoundException::new);
        if (copy.getBorrow() != null && copy.getBorrow().getId() != -1L && borrowDbService.getBorrow(copy.getBorrow().getId()).isPresent()) {
            Borrow borrow = borrowDbService.getBorrow(copy.getBorrow().getId()).get();
            borrow.setCopies(null);
            borrowDbService.update(borrow);
        }
        title.setCopies(null);
        copy.setBorrow(null);
        copy.setTitle(null);
        titleDbService.update(title);
        copyDbService.update(copy);
        copyDbService.delete(id);
    }

    public void updateCopy(CopyDto copyDto) throws BorrowNotFoundException, CopyNotFoundException, TitleNotFoundException {
        Copy copy = copyMapper.mapToCopy(copyDto, getTitleByCopyDto(copyDto));
        copy.setBorrow(borrowDbService.getBorrow(copyDto.getBorrowId()).orElseThrow(BorrowNotFoundException::new));
        copyDbService.update(copy);
    }

    private Title getTitleByCopyDto(CopyDto copyDto) throws TitleNotFoundException, CopyNotFoundException {
        return titleDbService.getTitle(copyDto.getTitleDto().getId()).orElseThrow(CopyNotFoundException::new);
    }
}
