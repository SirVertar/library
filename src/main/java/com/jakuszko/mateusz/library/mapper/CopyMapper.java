package com.jakuszko.mateusz.library.mapper;

import com.jakuszko.mateusz.library.domain.*;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class CopyMapper {

    public CopyDto mapToCopyDto(Copy copy, TitleDto titleDto) {
        log.info("Map Copy do CopyDto");
        return CopyDto.builder()
                .id(copy.getId())
                .titleDto(titleDto)
                .isBorrowed(copy.getIsBorrowed())
                .borrowId(copy.getBorrow().getId())
                .build();
    }

    public Copy mapToCopy(CopyDto copyDto, Title title, Borrow borrow) {
        log.info("Map CopyDto to Copy");
        return Copy.builder()
                .id(copyDto.getId())
                .title(title)
                .isBorrowed(copyDto.getIsBorrowed())
                .borrow(borrow)
                .build();
    }

    public List<CopyDto> mapToCopyDtoList(List<Copy> copies, List<TitleDto> titleDtos) {
        log.info("Map Copies to CopyDtos");
        return copies.stream()
                .map(copy -> CopyDto.builder()
                        .id(copy.getId())
                        .titleDto(titleDtos.stream().filter(e -> e.getId() == copy.getTitle().getId()).findFirst().get())
                        .isBorrowed(copy.getIsBorrowed())
                        .borrowId(copy.getBorrow().getId())
                        .build())
                .collect(Collectors.toList());
    }

}
