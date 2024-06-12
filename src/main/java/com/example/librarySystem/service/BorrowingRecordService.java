package com.example.librarySystem.service;

import com.example.librarySystem.model.BorrowingRecord;
import com.example.librarySystem.model.Customer;

import java.util.List;

public interface BorrowingRecordService {
    List<BorrowingRecord> findAll();
    BorrowingRecord findById(Long id);
    BorrowingRecord save(BorrowingRecord borrowingRecord);
    BorrowingRecord updateBorrowingRecord(Long id, BorrowingRecord borrowingRecord);
    void deleteById(Long id);
    List<BorrowingRecord> findByUserId(Long userId);
    List<BorrowingRecord> findByBookId(Long bookId);
    public boolean hasActiveBorrowings(Customer customer);
}
