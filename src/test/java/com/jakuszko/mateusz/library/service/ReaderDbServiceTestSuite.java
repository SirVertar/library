package com.jakuszko.mateusz.library.service;

import com.jakuszko.mateusz.library.domain.Reader;
import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
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
        readers.add(reader1);
        readers.add(reader2);
        return readers;
    }

    @Test
    public void saveReadersTest() throws ReaderNotFoundException {
        //Given
        List<Reader> readers = createListOfReaders();

        //When
        readerDbService.create(readers.get(0));
        readerDbService.create(readers.get(1));
        Long idReader1 = readers.get(0).getId();
        Long idReader2 = readers.get(1).getId();
        Optional<Reader> currentReader1 = readerDbService.getReader(idReader1);
        Optional<Reader> currentReader2 = readerDbService.getReader(idReader2);


        //Then
        assertTrue(currentReader1.isPresent());
        assertTrue(currentReader2.isPresent());
        assertEquals("Mateusz", currentReader1.get().getName());
        assertEquals("Jakuszko", currentReader1.get().getSurname());
        assertEquals(LocalDate.of(2012,10,13), currentReader1.get().getRegisteredDate());
        assertEquals("Weronika", currentReader2.get().getName());
        assertEquals("Chopin", currentReader2.get().getSurname());
        assertEquals(LocalDate.of(2015,12,25), currentReader2.get().getRegisteredDate());
    }
}
