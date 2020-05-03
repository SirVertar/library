package com.jakuszko.mateusz.library.service;

import com.jakuszko.mateusz.library.domain.Borrow;
import com.jakuszko.mateusz.library.repository.BorrowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public void setCopiesAndReaderToNull(Borrow borrow) {
        borrow.setCopies(null);
        borrow.setReader(null);
        update(borrow);
    }
}
