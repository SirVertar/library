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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
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
    public void createAndGetReaderTest() {
        //Given
        List<Reader> readers = createListOfReaders();

        //When
        readerDbService.create(readers.get(0));
        Long idReader1 = readers.get(0).getId();
        Optional<Reader> currentReader1 = readerDbService.getReader(idReader1);

        //Then
        assertTrue(currentReader1.isPresent());
        assertEquals("Mateusz", currentReader1.get().getName());
        assertEquals("Jakuszko", currentReader1.get().getSurname());
        assertEquals(LocalDate.of(2012, 10, 13), currentReader1.get().getRegisteredDate());
    }

    @Test
    public void getReadersTest() {
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
        assertTrue(Arrays.asList(currentReader1, currentReader2).stream()
                .filter(reader -> reader.get().getId().equals(idReader1))
                .allMatch(reader -> reader.get().getName().equals("Mateusz") &&
                        reader.get().getSurname().equals("Jakuszko")));
        assertTrue(Arrays.asList(currentReader1, currentReader2).stream()
                .filter(reader -> reader.get().getId().equals(idReader2))
                .allMatch(reader -> reader.get().getName().equals("Weronika") &&
                        reader.get().getSurname().equals("Chopin")));

    }

    @Test
    public void updateReaderTest() {
        //Given
        List<Reader> readers = createListOfReaders();
        //When
        readerDbService.create(readers.get(0));
        Long idReader1 = readers.get(0).getId();
        Optional<Reader> reader = readerDbService.getReader(idReader1);
        reader.orElseThrow(ReaderNotFoundException::new).setName("Matylda");
        readerDbService.update(reader.get());
        Optional<Reader> updatedReader = readerDbService.getReader(idReader1);
        //Then
        assertTrue(updatedReader.isPresent());
        assertEquals("Matylda", updatedReader.get().getName());
        assertEquals("Jakuszko", updatedReader.get().getSurname());
        assertEquals(LocalDate.of(2012, 10, 13), updatedReader.get().getRegisteredDate());
    }

    @Test
    public void deleteReaderTest() {
        //Given
        List<Reader> readers = createListOfReaders();
        //When
        readerDbService.create(readers.get(0));
        Long idReader1 = readers.get(0).getId();
        Optional<Reader> reader = readerDbService.getReader(idReader1);
        Long readerId = reader.orElseThrow(ReaderNotFoundException::new).getId();
        readerDbService.delete(readerId);
        Optional<Reader> deletedReader = readerDbService.getReader(readerId);
        //Then
        assertFalse(deletedReader.isPresent());
    }

}
