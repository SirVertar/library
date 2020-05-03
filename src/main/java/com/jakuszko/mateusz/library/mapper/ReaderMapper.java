package com.jakuszko.mateusz.library.mapper;


import com.jakuszko.mateusz.library.domain.Reader;
import com.jakuszko.mateusz.library.domain.ReaderDto;
import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReaderMapper {
    private final BorrowMapper borrowMapper;

    @Autowired
    public ReaderMapper(BorrowMapper borrowMapper) {
        this.borrowMapper = borrowMapper;
    }

    public ReaderDto mapToReaderDto(Reader reader) {
        return ReaderDto.builder()
                .id(reader.getId())
                .name(reader.getName())
                .surname(reader.getSurname())
                .registeredDate(reader.getRegisteredDate())
                .borrowList(borrowMapper.mapToBorrowDtoList(reader.getBorrowList()))
                .build();
    }

    public Reader mapToReader(ReaderDto readerDto) throws ReaderNotFoundException {
        return Reader.builder()
                .id(readerDto.getId())
                .name(readerDto.getName())
                .surname(readerDto.getSurname())
                .registeredDate(readerDto.getRegisteredDate())
                .borrowList(borrowMapper.mapToBorrowList(readerDto.getBorrowList()))
                .build();
    }

    public List<ReaderDto> mapToReaderDtoList(List<Reader> readers) {
        return readers.stream()
                .map(e -> ReaderDto.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .surname(e.getSurname())
                        .registeredDate(e.getRegisteredDate())
                        .borrowList(borrowMapper.mapToBorrowDtoList(e.getBorrowList()))
                        .build())
                .collect(Collectors.toList());
    }

}
