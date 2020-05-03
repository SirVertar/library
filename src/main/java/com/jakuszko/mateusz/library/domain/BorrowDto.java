package com.jakuszko.mateusz.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class BorrowDto {
    private final Long id;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Long readerId;
    private final List<CopyDto> copies;
}
