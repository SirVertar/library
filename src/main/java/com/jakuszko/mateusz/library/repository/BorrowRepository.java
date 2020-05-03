package com.jakuszko.mateusz.library.repository;

import com.jakuszko.mateusz.library.domain.Borrow;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface BorrowRepository extends CrudRepository<Borrow, Long> {
    @Override
    List<Borrow> findAll();
    @Override
    Optional<Borrow> findById(Long id);
    @Override
    Borrow save(Borrow borrow);
}
