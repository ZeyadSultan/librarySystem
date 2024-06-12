package com.example.librarySystem.repository;

import com.example.librarySystem.model.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    List<BorrowingRecord> findByBookId(Long bookId);
    List<BorrowingRecord> findByCustomerId(Long customerId);
}
