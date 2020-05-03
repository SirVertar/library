package com.jakuszko.mateusz.library.repository;

import com.jakuszko.mateusz.library.domain.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {
    @Override
    List<Reader> findAll();
    @Override
    Optional<Reader> findById(Long id);
    @Override
    Reader save(Reader reader);
}

