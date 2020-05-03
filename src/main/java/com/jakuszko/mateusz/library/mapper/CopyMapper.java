package com.jakuszko.mateusz.library.mapper;

import com.jakuszko.mateusz.library.domain.*;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CopyMapper {

    public CopyDto mapToCopyDto(Copy copy, TitleDto titleDto) {
        try {
            return CopyDto.builder()
                    .id(copy.getId())
                    .titleDto(titleDto)
                    .isBorrowed(copy.getIsBorrowed())
                    .borrowId(copy.getBorrow().getId())
                    .build();
        } catch (NullPointerException e) {
            return CopyDto.builder()
                    .id(copy.getId())
                    .titleDto(titleDto)
                    .isBorrowed(copy.getIsBorrowed())
                    .borrowId(-1L)
                    .build();
        }
    }

    public Copy mapToCopy(CopyDto copyDto, Title title) {
        return Copy.builder()
                .id(copyDto.getId())
                .title(title)
                .isBorrowed(copyDto.getIsBorrowed())
                .borrow(new Borrow())
                .build();
    }

    public List<CopyDto> mapToCopyDtoList(List<Copy> copies, List<TitleDto> titleDtos) {

        try {
            return copies.stream()
                    .map(copy -> CopyDto.builder()
                            .id(copy.getId())
                            .titleDto(titleDtos.stream().filter(e -> e.getId() == copy.getTitle().getId()).findFirst().get())
                            .isBorrowed(copy.getIsBorrowed())
                            .borrowId(copy.getBorrow().getId())
                            .build())
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            return copies.stream()
                    .map(copy -> CopyDto.builder()
                            .id(copy.getId())
                            .titleDto(titleDtos.stream().filter(f -> f.getId() == copy.getTitle().getId()).findFirst().get())
                            .isBorrowed(copy.getIsBorrowed())
                            .borrowId(-1L)
                            .build())
                    .collect(Collectors.toList());
        }
    }

    public List<Copy> mapToCopyList(List<CopyDto> copies, Stream<Title> titleStream) throws TitleNotFoundException {
        return copies.stream()
                .map(e -> Copy.builder()
                        .id(e.getId())
                        .title(titleStream.filter(title -> title.getId().equals(e.getTitleDto().getId())).findFirst().get())
                        .isBorrowed(e.getIsBorrowed())
                        .build())
                .collect(Collectors.toList());
    }
}
