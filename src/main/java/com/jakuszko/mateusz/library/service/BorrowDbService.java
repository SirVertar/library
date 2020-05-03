package com.jakuszko.mateusz.library.service;

import com.jakuszko.mateusz.library.domain.Borrow;
import com.jakuszko.mateusz.library.domain.Reader;
import com.jakuszko.mateusz.library.exceptions.BorrowNotFoundException;
import com.jakuszko.mateusz.library.repository.BorrowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class BorrowDbService {
    private final BorrowRepository borrowRepository;

    @Autowired
    public BorrowDbService(BorrowRepository borrowRepository) {
        this.borrowRepository = borrowRepository;
    }

    public List<Borrow> getBorrowList() {
        return borrowRepository.findAll();
    }

    public Optional<Borrow> getBorrow(Long id) {
        return borrowRepository.findById(id);
    }

    public void create(Borrow borrow) {
        if (borrow.getCopies() == null) {
            borrow.setCopies(new ArrayList<>());
        }
        borrowRepository.save(borrow);
    }

    public void update(Borrow borrow) {
        borrowRepository.save(borrow);
    }

    public void delete(Long id) {
        borrowRepository.deleteById(id);
    }

    public List<Borrow> getBorrowListByReaderId(Long id) {
        return borrowRepository.findAll().stream()
                .filter(borrow -> borrow.getReader().getId().equals(id))
                .collect(Collectors.toList());
    }

    public List<Borrow> getBorrowListByReadersList(List<Reader> readers) {
        List<Borrow> borrows;
        borrows = readers.stream().flatMap(reader -> reader.getBorrowList().stream()).map(Borrow::getId).map(e -> {
            try {
                return borrowRepository.findById(e).orElseThrow(BorrowNotFoundException::new);
            } catch (BorrowNotFoundException borrowNotFoundException) {
                borrowNotFoundException.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
       return borrows;
    }

    public void setCopiesAndReaderToNull(Borrow borrow) {
        borrow.setCopies(null);
        borrow.setReader(null);
        update(borrow);
    }

}
