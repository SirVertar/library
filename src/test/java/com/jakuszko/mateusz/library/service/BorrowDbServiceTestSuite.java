package com.jakuszko.mateusz.library.service;

import com.jakuszko.mateusz.library.domain.Borrow;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BorrowDbServiceTestSuite {
    @Autowired
    private BorrowDbService borrowDbService;
    @Autowired
    private ReaderDbService readerDbService;
    @Autowired
    private CopyDbService copyDbService;


    private List<Borrow> createBorrows() {
        return new ArrayList<Borrow>();
    }
}
