package com.jakuszko.mateusz.library.mapper;

import com.jakuszko.mateusz.library.domain.Borrow;
import com.jakuszko.mateusz.library.domain.BorrowDto;
import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundException;
import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundRuntimeException;
import com.jakuszko.mateusz.library.service.ReaderDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BorrowMapper {
    private final CopyMapper copyMapper;
    private final ReaderDbService readerDbService;

    @Autowired
    public BorrowMapper(CopyMapper copyMapper, ReaderDbService readerDbService) {
        this.copyMapper = copyMapper;
        this.readerDbService = readerDbService;
    }

    public BorrowDto mapToBorrowDto(Borrow borrow) {
        return BorrowDto.builder()
                .id(borrow.getId())
                .startDate(borrow.getStartDate())
                .endDate(borrow.getEndDate())
                .readerId(borrow.getReader().getId())
                .copies(copyMapper.mapToCopyDtoList(borrow.getCopies()))
                .build();
    }

    public Borrow mapToBorrow(BorrowDto borrowDto) throws ReaderNotFoundException {
        return Borrow.builder()
                .id(borrowDto.getId())
                .reader(readerDbService.getReader(borrowDto.getReaderId()).orElseThrow(ReaderNotFoundException::new))
                .copies(copyMapper.mapToCopyList(borrowDto.getCopies()))
                .build();
    }

    public List<BorrowDto> mapToBorrowDtoList(List<Borrow> borrows) {
        return borrows.stream()
                .map(e -> BorrowDto.builder()
                        .id(e.getId())
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .readerId(e.getReader().getId())
                        .copies(copyMapper.mapToCopyDtoList(e.getCopies()))
                        .build())
                .collect(Collectors.toList());
    }

    public List<Borrow> mapToBorrowList(List<BorrowDto> borrowDtos) {
        return borrowDtos.stream()
                .map(e -> Borrow.builder()
                        .id(e.getId())
                        .reader(readerDbService.getReader(e.getReaderId()).orElseThrow(ReaderNotFoundRuntimeException::new))
                        .copies(copyMapper.mapToCopyList(e.getCopies())).build())
                .collect(Collectors.toList());
    }
}
