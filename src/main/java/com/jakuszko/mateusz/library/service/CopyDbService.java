package com.jakuszko.mateusz.library.service;

import com.jakuszko.mateusz.library.domain.Copy;
import com.jakuszko.mateusz.library.domain.Title;
import com.jakuszko.mateusz.library.repository.CopyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class CopyDbService {
    private final CopyRepository copyRepository;

    @Autowired
    public CopyDbService(CopyRepository copyRepository) {
        this.copyRepository = copyRepository;
    }

    public List<Copy> getCopies() {
        return copyRepository.findAll();
    }

    public Optional<Copy> getCopy(Long id) {
        return copyRepository.findById(id);
    }

    public void create(Copy copy) {
        copyRepository.save(copy);
    }

    public void update(Copy copy) {
        copyRepository.save(copy);
    }

    public void delete(Long id) {
        copyRepository.deleteById(id);
    }

    public List<Copy> getCopiesByIdList(List<Long> ids) {
        return ids.stream()
                .map(copyRepository::findById)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void updateStatusOfCopy(Long id) {
    }

    public void setBorrowToNull(List<Copy> copys) {
        copys.forEach(e -> e.setBorrow(null));
        copys.forEach(this::update);
    }

    public List<Copy> getCopiesByTitle(Title title) {
        return copyRepository.findAllByTitle(title);
    }

    public List<Copy> getCopiesByReaderId(Long id) {
        return copyRepository.findAll().stream()
                .filter(copy -> copy.getBorrow().getReader().getId().equals(id)).collect(Collectors.toList());
    }
}
