package com.jakuszko.mateusz.library.mapper;


import com.jakuszko.mateusz.library.domain.Borrow;
import com.jakuszko.mateusz.library.domain.BorrowDto;
import com.jakuszko.mateusz.library.domain.Reader;
import com.jakuszko.mateusz.library.domain.ReaderDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReaderMapper {

    public ReaderDto mapToReaderDto(Reader reader, List<BorrowDto> borrowDtos) {
        return ReaderDto.builder()
                .id(reader.getId())
                .name(reader.getName())
                .surname(reader.getSurname())
                .registeredDate(reader.getRegisteredDate())
                .borrowList(borrowDtos)
                .build();
    }

    public Reader mapToReader(ReaderDto readerDto, List<Borrow> borrows) {
        return Reader.builder()
                .id(readerDto.getId())
                .name(readerDto.getName())
                .surname(readerDto.getSurname())
                .registeredDate(readerDto.getRegisteredDate())
                .borrowList(borrows)
                .build();
    }

    public List<ReaderDto> mapToReaderDtoList(List<Reader> readers, List<BorrowDto> borrowDtos) {
        return readers.stream()
                .map(e -> ReaderDto.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .surname(e.getSurname())
                        .registeredDate(e.getRegisteredDate())
                        .borrowList(borrowDtos.stream()
                                .filter(borrow -> borrow.getReaderId().equals(e.getId()))
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
