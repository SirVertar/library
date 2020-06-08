package com.jakuszko.mateusz.library.mapper;


import com.jakuszko.mateusz.library.domain.Borrow;
import com.jakuszko.mateusz.library.domain.BorrowDto;
import com.jakuszko.mateusz.library.domain.Reader;
import com.jakuszko.mateusz.library.domain.ReaderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ReaderMapper {

    public ReaderDto mapToReaderDto(Reader reader, List<BorrowDto> borrowDtos) {
        log.info("Map Reader to ReaderDto");
        return ReaderDto.builder()
                .id(reader.getId())
                .name(reader.getName())
                .surname(reader.getSurname())
                .registeredDate(reader.getRegisteredDate())
                .borrows(borrowDtos)
                .build();
    }

    public Reader mapToReader(ReaderDto readerDto, List<Borrow> borrows) {
        log.info("Map ReaderDto to Reader");
        return Reader.builder()
                .id(readerDto.getId())
                .name(readerDto.getName())
                .surname(readerDto.getSurname())
                .registeredDate(readerDto.getRegisteredDate())
                .borrows(borrows)
                .build();
    }

    public List<ReaderDto> mapToReaderDtoList(List<Reader> readers, List<BorrowDto> borrowDtos) {
        log.info("Map Readers to ReaderDtos");
        return readers.stream()
                .map(e -> ReaderDto.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .surname(e.getSurname())
                        .registeredDate(e.getRegisteredDate())
                        .borrows(borrowDtos.stream()
                                .filter(borrow -> borrow.getReaderId().equals(e.getId()))
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
