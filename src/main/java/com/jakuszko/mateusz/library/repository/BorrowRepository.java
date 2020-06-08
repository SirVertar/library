package com.jakuszko.mateusz.library.repository;

import com.jakuszko.mateusz.library.domain.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    @Override
    List<Borrow> findAll();

    @Override
    Optional<Borrow> findById(Long id);

    @Override
    Borrow save(Borrow borrow);
}
