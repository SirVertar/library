package com.jakuszko.mateusz.library.mapper;

import com.jakuszko.mateusz.library.domain.Copy;
import com.jakuszko.mateusz.library.domain.Title;
import com.jakuszko.mateusz.library.domain.TitleDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class TitleMapperTestSuite {
    @Autowired
    private TitleMapper titleMapper;
    
    @Test
    public void shouldMapTitleToTitleDto() {
        //Given
        List<Copy> copies = new ArrayList<>();

        Title title = Title.builder()
                .id(21L)
                .title("Lord")
                .author("Tolkien")
                .releaseDate(2010)
                .copies(copies)
                .build();
        TitleDto expectedTitleDto = TitleDto.builder()
                .id(21L)
                .title("Lord")
                .author("Tolkien")
                .releaseDate(2010)
                .build();
        //When
        TitleDto titleDto = titleMapper.mapToTitleDto(title);
        //Then
        assertEquals(expectedTitleDto.getId(), titleDto.getId());
        assertEquals(expectedTitleDto.getReleaseDate(), titleDto.getReleaseDate());
        assertEquals(expectedTitleDto.getTitle(), titleDto.getTitle());
        assertEquals(expectedTitleDto.getAuthor(), titleDto.getAuthor());
    }

    @Test
    public void shouldMapTitleDtoToTitle() {
        //Given
        List<Copy> copies = new ArrayList<>();

        Title expectedTitle = Title.builder()
                .id(21L)
                .title("Lord")
                .author("Tolkien")
                .releaseDate(2010)
                .copies(copies)
                .build();
        TitleDto titleDto = TitleDto.builder()
                .id(21L)
                .title("Lord")
                .author("Tolkien")
                .releaseDate(2010)
                .build();
        //When
        Title title = titleMapper.mapToTitle(titleDto, copies);
        //Then
        assertEquals(expectedTitle.getId(), title.getId());
        assertEquals(expectedTitle.getReleaseDate(), title.getReleaseDate());
        assertEquals(expectedTitle.getTitle(), title.getTitle());
        assertEquals(expectedTitle.getAuthor(), title.getAuthor());
        assertEquals(expectedTitle.getCopies().size(), title.getCopies().size());
    }

    @Test
    public void shouldMapTitlesToTitleDtos() {
        //Given
        List<Copy> copies = new ArrayList<>();

        Title title = Title.builder()
                .id(21L)
                .title("Lord")
                .author("Tolkien")
                .releaseDate(2010)
                .copies(copies)
                .build();
        TitleDto titleDto = TitleDto.builder()
                .id(21L)
                .title("Lord")
                .author("Tolkien")
                .releaseDate(2010)
                .build();

        List<Title> titles = new ArrayList<>();
        titles.add(title);

        List<TitleDto> expectedTitleDtos = new ArrayList<>();
        expectedTitleDtos.add(titleDto);

        //When
        List<TitleDto> titleDtos = titleMapper.mapToTitleDtoList(titles);

        //Then
        assertEquals(expectedTitleDtos.size(), titleDtos.size());
        assertEquals(expectedTitleDtos.get(0).getAuthor(), titleDtos.get(0).getAuthor());
        assertEquals(expectedTitleDtos.get(0).getTitle(), titleDtos.get(0).getTitle());
        assertEquals(expectedTitleDtos.get(0).getId(), titleDtos.get(0).getId());
        assertEquals(expectedTitleDtos.get(0).getReleaseDate(), titleDtos.get(0).getReleaseDate());
    }

}
