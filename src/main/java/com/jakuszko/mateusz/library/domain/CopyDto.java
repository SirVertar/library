package com.jakuszko.mateusz.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CopyDto {
    private final Long id;
    private final Boolean isBorrowed;
    private final Long borrowId;
    private final TitleDto titleDto;
}
