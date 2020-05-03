package com.jakuszko.mateusz.library.service;

import com.jakuszko.mateusz.library.domain.Reader;
import com.jakuszko.mateusz.library.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ReaderDbService {
    private final ReaderRepository readerRepository;

    @Autowired
    public ReaderDbService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }



    public List<Reader> getReaders() {
        return readerRepository.findAll();
    }

    public Optional<Reader> getReader(Long id) {
        return readerRepository.findById(id);
    }

    public void create(Reader reader) {
        readerRepository.save(reader);
    }

    public void update(Reader reader) {
        readerRepository.save(reader);
    }

    public void delete(Long id) {
        readerRepository.deleteById(id);
    }

}
