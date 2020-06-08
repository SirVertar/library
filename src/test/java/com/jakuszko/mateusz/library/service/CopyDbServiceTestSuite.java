package com.jakuszko.mateusz.library.service;

import com.jakuszko.mateusz.library.domain.Copy;
import com.jakuszko.mateusz.library.exceptions.CopyNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class CopyDbServiceTestSuite {

    @Autowired
    CopyDbService copyDbService;

    @Test
    public void createAndGetCopyTest() {
        //Given
        Copy copy = Copy.builder().isBorrowed(false).build();
        //When
        copyDbService.create(copy);
        Long copyId = copy.getId();
        Optional<Copy> createdCopy = copyDbService.getCopy(copyId);
        //Then
        assertTrue(createdCopy.isPresent());
        assertEquals(false, createdCopy.get().getIsBorrowed());
    }


    @Test
    public void getCopiesTest() {
        //Given
        Copy copy1 = Copy.builder().isBorrowed(false).build();
        Copy copy2 = Copy.builder().isBorrowed(true).build();
        //When
        copyDbService.create(copy1);
        copyDbService.create(copy2);
        Long copy1Id = copy1.getId();
        Long copy2Id = copy2.getId();
        List<Copy> copies = copyDbService.getCopies();
        //Then
        assertEquals(2, copies.size());
        assertTrue(copies.stream()
        .filter(copy -> copy.getId().equals(copy1Id))
        .noneMatch(Copy::getIsBorrowed));
        assertTrue(copies.stream()
                .filter(copy -> copy.getId().equals(copy2Id))
                .allMatch(Copy::getIsBorrowed));
    }

    @Test
    public void updateCopyTest() {
        //Given
        Copy copy = Copy.builder().isBorrowed(false).build();
        //When
        copyDbService.create(copy);
        Long copyId = copy.getId();
        Copy createdCopy = copyDbService.getCopy(copyId).orElseThrow(CopyNotFoundException::new);
        createdCopy.setIsBorrowed(true);
        copyDbService.update(createdCopy);
        Optional<Copy> updatedCopy = copyDbService.getCopy(copyId);
        //Then
        assertTrue(updatedCopy.isPresent());
        assertEquals(true, updatedCopy.get().getIsBorrowed());
    }

    @Test
    public void deleteCopyTest() {
        //Given
        Copy copy = Copy.builder().isBorrowed(false).build();
        //When
        copyDbService.create(copy);
        Long copyId = copy.getId();
        copyDbService.delete(copyId);
        Optional<Copy> deletedCopy = copyDbService.getCopy(copyId);
        //Then
        assertFalse(deletedCopy.isPresent());
    }
}
