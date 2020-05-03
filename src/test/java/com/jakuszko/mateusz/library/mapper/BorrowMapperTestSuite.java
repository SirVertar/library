package com.jakuszko.mateusz.library.mapper;

import com.jakuszko.mateusz.library.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BorrowMapperTestSuite {

    @Autowired
    private BorrowMapper borrowMapper;

    @Test
    public void mapToBorrowDtoTest() {
        //Given
        Reader reader1 = Reader.builder()
                .id(1L)
                .name("Mateusz")
                .surname("Jakuszko")
                .registeredDate(LocalDate.of(2012, 10, 13)).build();

        Borrow borrow = Borrow.builder().id(22L)
                .reader(reader1).build();
        Title title1 = Title.builder().title("Lord Of The Rings").author("Tolkien").releaseDate(2005).build();
        Title title2 = Title.builder().title("Sword of destiny").author("Goodkind").releaseDate(1990).build();
        TitleDto title1Dto = TitleDto.builder().title("Lord Of The Rings").author("Tolkien").releaseDate(2005).build();
        TitleDto title2Dto = TitleDto.builder().title("Sword of destiny").author("Goodkind").releaseDate(1990).build();
        Copy copy11 = Copy.builder().isBorrowed(true).title(title1).borrow(borrow).build();
        Copy copy21 = Copy.builder().isBorrowed(true).title(title2).borrow(borrow).build();
        CopyDto copy11Dto = CopyDto.builder().isBorrowed(true).titleDto(title1Dto).borrowId(22L).build();
        CopyDto copy21Dto = CopyDto.builder().isBorrowed(true).titleDto(title2Dto).borrowId(22L).build();
        List<Copy> copies = Arrays.asList(copy11, copy21);
        List<CopyDto> copiesDto = Arrays.asList(copy11Dto, copy21Dto);

        borrow.setCopies(copies);
        //When
        BorrowDto borrowDto = borrowMapper.mapToBorrowDto(borrow,copiesDto);
        //Then
        assertEquals(2, borrowDto.getCopies().size());
        assertEquals(22L, borrowDto.getId().longValue());
        assertEquals(1L, borrowDto.getReaderId().longValue());
    }
}
