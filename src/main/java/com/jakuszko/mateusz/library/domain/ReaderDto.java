package com.jakuszko.mateusz.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ReaderDto {
    private final Long id;
    private final String name;
    private final String surname;
    private final LocalDate registeredDate;
    private final List<BorrowDto> borrowList;
}
