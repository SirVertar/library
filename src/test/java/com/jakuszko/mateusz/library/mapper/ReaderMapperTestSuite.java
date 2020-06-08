package com.jakuszko.mateusz.library.mapper;

import com.jakuszko.mateusz.library.domain.Borrow;
import com.jakuszko.mateusz.library.domain.BorrowDto;
import com.jakuszko.mateusz.library.domain.Reader;
import com.jakuszko.mateusz.library.domain.ReaderDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReaderMapperTestSuite {

    @Autowired
    private ReaderMapper mapper;

    @Test
    public void shouldMapFromReaderToReaderDto() {
        //Given
        List<Object> data = prepareDataForTest();
        Reader reader = (Reader) data.get(0);
        List<BorrowDto> borrowDtos = (List<BorrowDto>) data.get(4);
        //When
        ReaderDto readerDto = mapper.mapToReaderDto(reader, borrowDtos);
        //Then
        assertEquals(2L, readerDto.getId().longValue());
        assertEquals("Mateusz", readerDto.getName());
        assertEquals("Jakuszko", readerDto.getSurname());
        assertEquals(LocalDate.of(2010, 10, 11), readerDto.getRegisteredDate());
        assertEquals(1, readerDto.getBorrows().size());
    }

    @Test
    public void shouldMapFromReaderDtoToReader() {
        //Given
        List<Object> data = prepareDataForTest();
        ReaderDto readerDto = (ReaderDto) data.get(1);
        List<Borrow> borrows = (List<Borrow>) data.get(3);
        //When
        Reader reader = mapper.mapToReader(readerDto, borrows);
        //Then
        assertEquals(2L, reader.getId().longValue());
        assertEquals("Mateusz", reader.getName());
        assertEquals("Jakuszko", reader.getSurname());
        assertEquals(LocalDate.of(2010, 10, 11), reader.getRegisteredDate());
        assertEquals(1, reader.getBorrows().size());
    }

    @Test
    public void shouldMapFromReadersToReaderDtos() {
        //Given
        List<Object> data = prepareDataForTest();
        List<Reader> readers = (List<Reader>) data.get(2);
        List<BorrowDto> borrowDtos = (List<BorrowDto>) data.get(4);
        //When
        List<ReaderDto> readerDtos = mapper.mapToReaderDtoList(readers, borrowDtos);
        //Then
        assertEquals(1, readerDtos.size());
        assertTrue(readerDtos.stream()
                .anyMatch(readerDto -> readerDto.getId().equals(2L)));
        assertTrue(readerDtos.stream()
                .filter(readerDto -> readerDto.getId().equals(2L))
                .anyMatch(readerDto -> readerDto.getName().equals("Mateusz") &&
                        readerDto.getSurname().equals("Jakuszko") &&
                        readerDto.getRegisteredDate().equals(LocalDate.of(2010, 10, 11))));
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

        ReaderDto readerDto = ReaderDto.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .id(2L)
                .registeredDate(LocalDate.of(2010, 10, 11))
                .borrows(borrowDtos)
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
        List<Object> listOfData = new ArrayList<>();
        listOfData.add(reader);
        listOfData.add(readerDto);
        listOfData.add(readers);
        listOfData.add(borrows);
        listOfData.add(borrowDtos);

        return listOfData;
    }
}
