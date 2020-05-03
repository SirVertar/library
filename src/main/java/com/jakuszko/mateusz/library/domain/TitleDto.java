package com.jakuszko.mateusz.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class TitleDto {
    private final Long id;
    private final String title;
    private final String author;
    private final Integer releaseDate;
}
