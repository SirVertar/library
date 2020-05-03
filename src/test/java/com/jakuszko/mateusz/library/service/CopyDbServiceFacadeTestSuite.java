package com.jakuszko.mateusz.library.service;

import com.jakuszko.mateusz.library.domain.Borrow;
import com.jakuszko.mateusz.library.domain.Copy;
import com.jakuszko.mateusz.library.domain.Reader;
import com.jakuszko.mateusz.library.domain.Title;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CopyDbServiceFacadeTestSuite {
    @Autowired
    private CopyDbService copyDbService;
    @Autowired
    private TitleDbService titleDbService;
    @Autowired
    private BorrowDbService borrowDbService;
    @Autowired
    private ReaderDbService readerDbService;

    @Test
    public void copySaveTest() {
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
        System.out.println(title1.getId());
        //When
        readerDbService.create(reader1);
        borrowDbService.create(borrow);
        titleDbService.create(title1);
        titleDbService.create(title2);
        copyDbService.create(copy11);
        copyDbService.create(copy12);
        copyDbService.create(copy21);
        copyDbService.create(copy22);
        //Then
        Long title1Id = title1.getId();
        Long title2Id = title2.getId();
        System.out.println(title1Id);
        assertNotNull(title1Id);
        assertNotNull(title2Id);
    }
}
