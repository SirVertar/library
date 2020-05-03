package com.jakuszko.mateusz.library.service;

import com.jakuszko.mateusz.library.domain.Title;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TitleRepositoryTestSuite {
    @Autowired
    private TitleDbService titleDbService;

    private List<Title> createDataList() {
        List<Title> titles = new ArrayList<>();
        Title title1 = Title.builder().title("Lord Of The Rings").author("Tolkien").releaseDate(2005).build();
        Title title2 = Title.builder().title("Sword of destiny").author("Goodkind").releaseDate(1990).build();
        Title title3 = Title.builder().title("DiscWord").author("Pratchett").releaseDate(2006).build();
        titles.add(title1);
        titles.add(title2);
        titles.add(title3);
        return titles;
    }

    @Test
    public void saveTitleTest() throws TitleNotFoundException {
        //Given
        List<Title> titles = createDataList();
        //When
        titleDbService.create(titles.get(0));
        titleDbService.create(titles.get(1));
        titleDbService.create(titles.get(2));
        Optional<Title> title1 = titleDbService.getTitle(titles.get(0).getId());
        Optional<Title> title2 = titleDbService.getTitle(titles.get(1).getId());
        Optional<Title> title3 = titleDbService.getTitle(titles.get(2).getId());
        //Then
        assertTrue(title1.isPresent());
        assertTrue(title2.isPresent());
        assertTrue(title3.isPresent());
    }
}
