package com.jakuszko.mateusz.library.mapper;

import com.jakuszko.mateusz.library.domain.Borrow;
import com.jakuszko.mateusz.library.domain.Copy;
import com.jakuszko.mateusz.library.domain.CopyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CopyMapper {
    private final TitleMapper titleMapper;

    @Autowired
    public CopyMapper(TitleMapper titleMapper) {
        this.titleMapper = titleMapper;
    }

    public CopyDto mapToCopyDto(Copy copy) {
        try {
            return CopyDto.builder()
                    .id(copy.getId())
                    .titleDto(titleMapper.mapToTitleDto(copy.getTitle()))
                    .isBorrowed(copy.getIsBorrowed())
                    .borrowId(copy.getBorrow().getId())
                    .build();
        } catch (NullPointerException e) {
            return CopyDto.builder()
                    .id(copy.getId())
                    .titleDto(titleMapper.mapToTitleDto(copy.getTitle()))
                    .isBorrowed(copy.getIsBorrowed())
                    .borrowId(-1L)
                    .build();
        }
    }

    public Copy mapToCopy(CopyDto copyDto) {
        return Copy.builder()
                .id(copyDto.getId())
                .title(titleMapper.mapToTitle(copyDto.getTitleDto()))
                .isBorrowed(copyDto.getIsBorrowed())
                .borrow(new Borrow())
                .build();
    }

    public List<CopyDto> mapToCopyDtoList(List<Copy> copies) {

        try {
            return copies.stream()
                    .map(copy -> CopyDto.builder()
                            .id(copy.getId())
                            .titleDto(titleMapper.mapToTitleDto(copy.getTitle()))
                            .isBorrowed(copy.getIsBorrowed())
                            .borrowId(copy.getBorrow().getId())
                            .build())
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            return copies.stream()
                    .map(copy -> CopyDto.builder()
                            .id(copy.getId())
                            .titleDto(titleMapper.mapToTitleDto(copy.getTitle()))
                            .isBorrowed(copy.getIsBorrowed())
                            .borrowId(-1L)
                            .build())
                    .collect(Collectors.toList());
        }
    }

    public List<Copy> mapToCopyList(List<CopyDto> copies) {
        return copies.stream()
                .map(e -> Copy.builder()
                        .id(e.getId())
                        .title(titleMapper.mapToTitle(e.getTitleDto()))
                        .isBorrowed(e.getIsBorrowed())
                        .build())
                .collect(Collectors.toList());
    }
}
