package com.jakuszko.mateusz.library.mapper;

import com.jakuszko.mateusz.library.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BorrowMapper {

    public BorrowDto mapToBorrowDto(Borrow borrow, List<CopyDto> copyDtos) {
        log.info("Map Borrow to BorrowDto");
        return BorrowDto.builder()
                .id(borrow.getId())
                .startDate(borrow.getStartDate())
                .endDate(borrow.getEndDate())
                .readerId(borrow.getReader().getId())
                .copies(copyDtos)
                .build();
    }

    public Borrow mapToBorrow(BorrowDto borrowDto, List<Copy> copies, Reader reader) {
        log.info("Map BorrowDto to Borrow");
        return Borrow.builder()
                .id(borrowDto.getId())
                .reader(reader)
                .copies(copies)
                .build();
    }

    public List<BorrowDto> mapToBorrowDtoList(List<Borrow> borrows, List<CopyDto> copyDtos) {
        log.info("Map Borrows to BorrowDtos");
        return borrows.stream().filter(Objects::nonNull).filter(borrow -> borrow.getReader() != null)
                .map(e -> BorrowDto.builder()
                        .id(e.getId())
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .readerId(e.getReader().getId())
                        .copies(copyDtos.stream()
                                .filter(copyDto -> copyDto.getBorrowId().equals(e.getId())).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    public List<Borrow> mapToBorrowList(List<BorrowDto> borrowDtos, List<Copy> copies, Reader reader) {
        log.info("Map BorrowDtos to Borrows");
        return borrowDtos.stream()
                .map(e -> Borrow.builder()
                        .id(e.getId())
                        .reader(reader)
                        .copies(copies.stream()
                                .filter(copy -> copy.getBorrow().getId().equals(e.getId())).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
