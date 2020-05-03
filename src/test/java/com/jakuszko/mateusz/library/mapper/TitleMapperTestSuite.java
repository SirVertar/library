package com.jakuszko.mateusz.library.mapper;

import com.jakuszko.mateusz.library.domain.Title;
import com.jakuszko.mateusz.library.domain.TitleDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TitleMapperTestSuite {
    @Autowired
    private TitleMapper titleMapper;

    @Test
    public void mapToTitleDtoTest() {
        //Given
        Title title = Title.builder().title("Title test").author("Title test author").copies(new ArrayList<>()).id(1L).releaseDate(2019).build();
        //When
        TitleDto titleDto = titleMapper.mapToTitleDto(title);
        //Then
        assertEquals("Title test", titleDto.getTitle());
    }
}
