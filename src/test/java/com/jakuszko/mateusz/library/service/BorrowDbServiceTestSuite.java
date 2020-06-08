package com.jakuszko.mateusz.library.service;

import com.jakuszko.mateusz.library.domain.Borrow;
import com.jakuszko.mateusz.library.domain.Copy;
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

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class BorrowDbServiceTestSuite {

    @Autowired
    private BorrowDbService borrowDbService;

    @Test
    public void getBorrowsTest() {
        //Given
        Borrow borrow1 = createBorrow();
        Borrow borrow2 = createBorrow();
        //When
        borrowDbService.create(borrow1);
        borrowDbService.create(borrow2);
        Long borrow1Id = borrow1.getId();
        Long borrow2Id = borrow2.getId();
        List<Borrow> borrows = borrowDbService.getBorrows();
        //Then
        assertEquals(2, borrows.size());
        assertTrue(borrows.stream()
                .filter(borrow -> borrow.getId().equals(borrow1Id))
                .allMatch(borrow -> borrow.getEndDate().equals(LocalDate.of(2020, 12, 23)) &&
                        borrow.getStartDate().equals(LocalDate.of(2020, 9, 23))));
        assertTrue(borrows.stream()
                .filter(borrow -> borrow.getId().equals(borrow2Id))
                .allMatch(borrow -> borrow.getEndDate().equals(LocalDate.of(2020, 12, 23)) &&
                        borrow.getStartDate().equals(LocalDate.of(2020, 9, 23))));
    }

    @Test
    public void getBorrowTest() {
        //Given
        Borrow borrow = createBorrow();
        //When
        borrowDbService.create(borrow);
        Long borrowId = borrow.getId();
        Optional<Borrow> testBorrow = borrowDbService.getBorrow(borrow.getId());
        //Then
        assertTrue(testBorrow.isPresent());
        assertEquals(borrowId, testBorrow.get().getId());
        assertEquals(borrow.getEndDate(), testBorrow.get().getEndDate());
        assertEquals(borrow.getStartDate(), testBorrow.get().getStartDate());
        assertEquals(borrow.getReader(), testBorrow.get().getReader());
    }

    @Test
    public void createBorrowTest() {
        //Given
        Borrow borrow = createBorrow();
        //When
        borrowDbService.create(borrow);
        Long borrowId = borrow.getId();
        //Then
        assertTrue(borrowDbService.getBorrow(borrowId).isPresent());
    }

    @Test
    public void updateBorrowTest() {
        //Given
        Borrow borrow = createBorrow();
        //When
        borrowDbService.create(borrow);
        LocalDate endDate = borrow.getEndDate();
        Long borrowId = borrow.getId();
        borrow.setEndDate(LocalDate.of(1990, 10, 11));
        borrowDbService.update(borrow);
        Optional<Borrow> updatedBorrow = borrowDbService.getBorrow(borrowId);
        //Then
        assertTrue(updatedBorrow.isPresent());
        assertEquals(LocalDate.of(1990, 10, 11), updatedBorrow.get().getEndDate());
    }

    @Test
    public void deleteBorrowTest() {
        //Given
        Borrow borrow = createBorrow();
        //When
        borrowDbService.create(borrow);
        LocalDate endDate = borrow.getEndDate();
        Long borrowId = borrow.getId();
        borrowDbService.delete(borrowId);
        Optional<Borrow> updatedBorrow = borrowDbService.getBorrow(borrowId);
        //Then
        assertFalse(updatedBorrow.isPresent());
    }

    private Borrow createBorrow() {
        return Borrow.builder().startDate(LocalDate.of(2020, 9, 23))
                .endDate(LocalDate.of(2020, 12, 23))
                .copies(new ArrayList<Copy>())
                .build();
    }
}
