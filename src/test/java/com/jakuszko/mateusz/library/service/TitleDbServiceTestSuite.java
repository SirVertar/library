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

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class TitleDbServiceTestSuite {
    @Autowired
    private TitleDbService titleDbService;

    private List<Title> createDataList() {
        List<Title> titles = new ArrayList<>();
        Title title1 = Title.builder()
                .title("Lord Of The Rings")
                .author("Tolkien")
                .releaseDate(2005)
                .copies(new ArrayList<>())
                .build();
        Title title2 = Title.builder()
                .title("Sword of destiny")
                .author("Goodkind")
                .releaseDate(1990)
                .copies(new ArrayList<>())
                .build();
        titles.add(title1);
        titles.add(title2);
        return titles;
    }

    @Test
    public void createTitleTest() throws TitleNotFoundException {
        //Given
        List<Title> titles = createDataList();
        //When
        titleDbService.create(titles.get(0));
        titleDbService.create(titles.get(1));
        Optional<Title> title1 = titleDbService.getTitle(titles.get(0).getId());
        Optional<Title> title2 = titleDbService.getTitle(titles.get(1).getId());
        //Then
        assertTrue(title1.isPresent());
        assertTrue(title2.isPresent());
    }

    @Test
    public void getTitleTest() {
        //Given
        List<Title> titles = createDataList();
        //When
        titleDbService.create(titles.get(0));
        Optional<Title> title1 = titleDbService.getTitle(titles.get(0).getId());
        //Then
        assertTrue(title1.isPresent());
        assertEquals(0, title1.get().getCopies().size());
        assertEquals("Lord Of The Rings", title1.get().getTitle());
        assertEquals("Tolkien", title1.get().getAuthor());
        assertEquals(2005, title1.get().getReleaseDate().longValue());
    }


    @Test
    public void getTitlesTest() {
        //Given
        List<Title> titles = createDataList();
        //When
        titleDbService.create(titles.get(0));
        titleDbService.create(titles.get(1));
        Long firstId = titles.get(0).getId();
        Long secondId = titles.get(1).getId();
        List<Title> testedTitles = titleDbService.getTitles();
        //Then
        assertEquals(2, testedTitles.size());
        assertTrue(testedTitles.stream()
                .anyMatch(title -> title.getId().equals(firstId) &&
                        title.getCopies().size() == 0 &&
                        title.getAuthor().equals("Tolkien") &&
                        title.getReleaseDate() == 2005 &&
                        title.getTitle().equals("Lord Of The Rings")));
        assertTrue(testedTitles.stream()
                .anyMatch(title -> title.getId().equals(secondId) &&
                        title.getCopies().size() == 0 &&
                        title.getAuthor().equals("Goodkind") &&
                        title.getReleaseDate() == 1990 &&
                        title.getTitle().equals("Sword of destiny")));
    }

    @Test
    public void updateTitlesTest() {
        //Given
        List<Title> titles = createDataList();
        //When
        titleDbService.create(titles.get(0));
        Long titleId = titles.get(0).getId();
        Title changedTitle = titleDbService.getTitle(titleId).orElseThrow(TitleNotFoundException::new);
        changedTitle.setAuthor("Olisa");
        titleDbService.update(changedTitle);
        Optional<Title> titleAfterChange = titleDbService.getTitle(titleId);
        //Then
        assertTrue(titleAfterChange.isPresent());
        assertEquals("Olisa", titleAfterChange.get().getAuthor());
        assertEquals(titleId, titleAfterChange.get().getId());
        assertEquals("Lord Of The Rings", titleAfterChange.get().getTitle());
        assertEquals(2005, titleAfterChange.get().getReleaseDate().longValue());

    }

    @Test
    public void deleteTitlesTest() {
        //Given
        List<Title> titles = createDataList();
        //When
        titleDbService.create(titles.get(0));
        titleDbService.create(titles.get(1));
        Long firstId = titles.get(0).getId();
        Long secondId = titles.get(1).getId();
        int sizeOfTitlesBeforeDelete = titleDbService.getTitles().size();
        titleDbService.delete(secondId);
        List<Title> titlesAfterDelete = titleDbService.getTitles();
        //Then
        assertEquals(1, titlesAfterDelete.size());
        assertTrue(titlesAfterDelete.stream()
                .allMatch(title -> title.getId().equals(firstId) &&
                        title.getTitle().equals("Lord Of The Rings") &&
                        title.getReleaseDate().equals(2005) &&
                        title.getAuthor().equals("Tolkien")));
    }
}
