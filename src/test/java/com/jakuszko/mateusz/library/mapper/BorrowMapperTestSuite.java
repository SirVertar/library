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
                .name("Mateusz")
                .surname("Jakuszko")
                .registeredDate(LocalDate.of(2012, 10, 13)).build();

        Borrow borrow = Borrow.builder()
                .reader(reader1).build();
        Title title1 = Title.builder().title("Lord Of The Rings").author("Tolkien").releaseDate(2005).build();
        Title title2 = Title.builder().title("Sword of destiny").author("Goodkind").releaseDate(1990).build();
        Copy copy11 = Copy.builder().isBorrowed(true).title(title1).borrow(borrow).build();
        Copy copy12 = Copy.builder().isBorrowed(true).title(title1).borrow(borrow).build();
        Copy copy21 = Copy.builder().isBorrowed(true).title(title2).borrow(borrow).build();
        Copy copy22 = Copy.builder().isBorrowed(true).title(title2).borrow(borrow).build();
        List<Copy> copies = Arrays.asList(copy11, copy12, copy21, copy22);
        borrow.setCopies(copies);
        //When
        BorrowDto borrowDto = borrowMapper.mapToBorrowDto(borrow);
        //Then
        assertEquals(4, borrowDto.getCopies().size());
    }

}
