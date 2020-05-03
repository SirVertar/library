package com.jakuszko.mateusz.library.service;

import com.jakuszko.mateusz.library.domain.Copy;
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

    public List<Optional<Copy>> getCopiesByIdList(List<Long> ids) {
        return ids.stream()
                .map(copyRepository::findById)
                .collect(Collectors.toList());
    }

    public void updateStatusOfCopy(Long id) {
    }

    public void setBorrowToNull(List<Copy> copys) {
        copys.forEach(e -> e.setBorrow(null));
        copys.forEach(this::update);
    }
}
