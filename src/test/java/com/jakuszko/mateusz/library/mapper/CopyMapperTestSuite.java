package com.jakuszko.mateusz.library.mapper;

import com.jakuszko.mateusz.library.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CopyMapperTestSuite {

    @Autowired
    CopyMapper copyMapper;

    @Test
    public void shouldMapCopyToCopyDto() {
        //Given
        List<Object> data = prepareDataForTest();
        Copy copy = (Copy)data.get(0);
        TitleDto titleDto = (TitleDto)data.get(3);
        CopyDto expectedCopyDto = (CopyDto)data.get(1);
        //When
        CopyDto copyDto = copyMapper.mapToCopyDto(copy, titleDto);
        //Then
        assertEquals(expectedCopyDto.getId(), copyDto.getId());
        assertEquals(expectedCopyDto.getBorrowId(), copyDto.getBorrowId());
        assertEquals(expectedCopyDto.getIsBorrowed(), copyDto.getIsBorrowed());
        assertEquals(expectedCopyDto.getTitleDto().getId(), copyDto.getTitleDto().getId());
        assertEquals(expectedCopyDto.getTitleDto().getAuthor(), copyDto.getTitleDto().getAuthor());
        assertEquals(expectedCopyDto.getTitleDto().getTitle(), copyDto.getTitleDto().getTitle());
        assertEquals(expectedCopyDto.getTitleDto().getReleaseDate(), copyDto.getTitleDto().getReleaseDate());
    }

    @Test
    public void shouldMapCopyDtoToCopy() {
        //Given
        List<Object> data = prepareDataForTest();
        Title title = (Title)data.get(2);
        Borrow borrow = (Borrow)data.get(4);
        CopyDto copyDto = (CopyDto)data.get(1);
        Copy expectedCopy = (Copy)data.get(0);
        //When
        Copy copy = copyMapper.mapToCopy(copyDto, title, borrow);
        //Then
        assertEquals(expectedCopy.getId(), copy.getId());
        assertEquals(expectedCopy.getBorrow().getId(), copy.getBorrow().getId());
        assertEquals(expectedCopy.getIsBorrowed(), copy.getIsBorrowed());
        assertEquals(expectedCopy.getTitle().getId(), copy.getTitle().getId());
    }

    @Test
    public void shouldMapCopiesToCopyDtos() {
        //Given
        List<Object> data = prepareDataForTest();
        List<CopyDto> expectedCopyDtos = (List<CopyDto>)data.get(6);
        List<Copy> copies = (List<Copy>)data.get(5);
        List<TitleDto> titleDtos = (List<TitleDto>)data.get(7);
        //When
        List<CopyDto> copyDtos = copyMapper.mapToCopyDtoList(copies, titleDtos);
        //Then
        assertEquals(expectedCopyDtos.size(), copyDtos.size());
        assertEquals(expectedCopyDtos.get(0).getId(), copyDtos.get(0).getId());
        assertEquals(expectedCopyDtos.get(0).getTitleDto().getId(), copyDtos.get(0).getTitleDto().getId());
        assertEquals(expectedCopyDtos.get(0).getIsBorrowed(), copyDtos.get(0).getIsBorrowed());
        assertEquals(expectedCopyDtos.get(0).getBorrowId(), copyDtos.get(0).getBorrowId());
    }

    private List<Object> prepareDataForTest() {
        List<Borrow> borrows = new ArrayList<>();
        List<BorrowDto> borrowDtos = new ArrayList<>();
        Reader reader = Reader.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .id(2L)
                .registeredDate(LocalDate.of(2010, 10, 11))
                .borrows(borrows)
                .build();

        List<Reader> readers = new ArrayList<>();
        readers.add(reader);

        Borrow borrow = Borrow.builder()
                .id(22L)
                .startDate(LocalDate.of(2019, 10, 11))
                .endDate(LocalDate.of(2019, 12, 11))
                .reader(reader)
                .build();
        borrows.add(borrow);

        BorrowDto borrowDto = BorrowDto.builder()
                .id(22L)
                .startDate(LocalDate.of(2019, 10, 11))
                .endDate(LocalDate.of(2019, 12, 11))
                .readerId(reader.getId())
                .build();
        borrowDtos.add(borrowDto);

        List<Copy> copies = new ArrayList<>();
        List<CopyDto> copyDtos = new ArrayList<>();
        List<TitleDto> titleDtos = new ArrayList<>();
        Title title = Title.builder().id(12L).releaseDate(2012).copies(copies).author("Mateusz").title("Kino").build();
        TitleDto titleDto = TitleDto.builder().id(12L).releaseDate(2012).author("Mateusz").title("Kino").build();
        Copy copy = Copy.builder().id(33L).isBorrowed(true).borrow(borrow).title(title).build();
        CopyDto copyDto = CopyDto.builder().id(33L).isBorrowed(true).titleDto(titleDto).borrowId(borrow.getId()).build();
        copies.add(copy);
        copyDtos.add(copyDto);
        titleDtos.add(titleDto);

        List<Object> listOfData = new ArrayList<>();
        listOfData.add(copy);
        listOfData.add(copyDto);
        listOfData.add(title);
        listOfData.add(titleDto);
        listOfData.add(borrow);
        listOfData.add(copies);
        listOfData.add(copyDtos);
        listOfData.add(titleDtos);

        return listOfData;
    }
}
