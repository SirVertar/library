package com.jakuszko.mateusz.library.service;

import com.jakuszko.mateusz.library.domain.Reader;
import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReaderDbServiceTestSuite {
    @Autowired
    private ReaderDbService readerDbService;

    private List<Reader> createListOfReaders() {
        List<Reader> readers = new ArrayList<>();
        Reader reader1 = Reader.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .registeredDate(LocalDate.of(2012, 10, 13)).build();
        Reader reader2 = Reader.builder()
                .name("Weronika")
                .surname("Chopin")
                .registeredDate(LocalDate.of(2015, 12, 25)).build();
        Reader reader3 = Reader.builder()
                .name("Beata")
                .surname("Forod")
                .registeredDate(LocalDate.of(2002, 5, 9)).build();
        readers.add(reader1);
        readers.add(reader2);
        readers.add(reader3);
        return readers;
    }

    @Test
    public void saveReadersTest() throws ReaderNotFoundException {
        //Given
        List<Reader> readers = createListOfReaders();

        //When
        readerDbService.create(readers.get(0));
        readerDbService.create(readers.get(1));
        readerDbService.create(readers.get(2));
        Long idReader1 = readers.get(0).getId();
        Long idReader2 = readers.get(1).getId();
        Long idReader3 = readers.get(2).getId();

        //Then
        Optional<Reader> currentReader1 = readerDbService.getReader(idReader1);
        Optional<Reader> currentReader2 = readerDbService.getReader(idReader2);
        Optional<Reader> currentReader3 = readerDbService.getReader(idReader3);
        assertTrue(currentReader1.isPresent());
        assertTrue(currentReader2.isPresent());
        assertTrue(currentReader3.isPresent());
    }
}
