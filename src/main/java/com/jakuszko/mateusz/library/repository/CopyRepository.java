package com.jakuszko.mateusz.library.repository;

import com.jakuszko.mateusz.library.domain.Copy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface CopyRepository extends CrudRepository<Copy, Long> {
    @Override
    List<Copy> findAll();
    @Override
    Optional<Copy> findById(Long id);
    @Override
    Copy save(Copy copy);
}
