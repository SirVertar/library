package com.jakuszko.mateusz.library.mapper;

import com.jakuszko.mateusz.library.domain.Copy;
import com.jakuszko.mateusz.library.domain.Title;
import com.jakuszko.mateusz.library.domain.TitleDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TitleMapper {

    public TitleDto mapToTitleDto(Title title) {
        return TitleDto.builder()
                .id(title.getId())
                .title(title.getTitle())
                .author(title.getAuthor())
                .releaseDate(title.getReleaseDate())
                .build();
    }

    public Title mapToTitle(TitleDto titleDto, List<Copy> copies) {
        return Title.builder()
                .id(titleDto.getId())
                .title(titleDto.getTitle())
                .author(titleDto.getAuthor())
                .releaseDate(titleDto.getReleaseDate())
                .copies(copies)
                .build();
    }

    public List<TitleDto> mapToTitleDtoList(List<Title> titles) {
        return titles.stream()
                .map(e -> TitleDto.builder()
                        .id(e.getId())
                        .title(e.getTitle())
                        .author(e.getAuthor())
                        .releaseDate(e.getReleaseDate()).build())
                .collect(Collectors.toList());
    }
}

