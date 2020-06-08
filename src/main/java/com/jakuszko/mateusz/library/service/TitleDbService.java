package com.jakuszko.mateusz.library.service;

import com.jakuszko.mateusz.library.domain.Copy;
import com.jakuszko.mateusz.library.domain.Title;
import com.jakuszko.mateusz.library.exceptions.TitleNotFoundException;
import com.jakuszko.mateusz.library.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TitleDbService {
    private final TitleRepository titleRepository;

    @Autowired
    public TitleDbService(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    public List<Title> getTitles() {
        return titleRepository.findAll();
    }

    public Optional<Title> getTitle(Long id) throws TitleNotFoundException {
        return titleRepository.findById(id);
    }

    public void create(Title title) {
        if (title.getCopies() == null) {
            title.setCopies(new ArrayList<>());
        }
        titleRepository.save(title);
    }

    public void update(Title title) {
        titleRepository.save(title);
    }

    public void delete(Long id) {
        titleRepository.deleteById(id);
    }

    public List<Title> getTitlesByCopyLists(List<Copy> copies) {
        List<Long> titlesIdsInCopies = copies.stream().map(Copy::getTitle).map(Title::getId).collect(Collectors.toList());
        return titleRepository.findAll().stream()
                .filter(title -> titlesIdsInCopies.contains(title.getId()))
                .collect(Collectors.toList());
    }
}
