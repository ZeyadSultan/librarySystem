package com.example.librarySystem.controller;

import com.example.librarySystem.model.BorrowingRecord;
import com.example.librarySystem.service.BorrowingRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrowings")
@RequiredArgsConstructor
public class BorrowingRecordController {

    private final BorrowingRecordService borrowingRecordService;

    @GetMapping
    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecordService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingRecord> getBorrowingRecordById(@PathVariable Long id) {
        BorrowingRecord borrowingRecord = borrowingRecordService.findById(id);
        return ResponseEntity.ok(borrowingRecord);
    }

    @PostMapping
    public ResponseEntity<BorrowingRecord> createBorrowingRecord(@RequestBody BorrowingRecord borrowingRecord) {
        BorrowingRecord createdBorrowingRecord = borrowingRecordService.save(borrowingRecord);
        return new ResponseEntity<>(createdBorrowingRecord, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowingRecord> updateBorrowingRecord(@PathVariable Long id, @RequestBody BorrowingRecord borrowingRecord) {
        BorrowingRecord updatedBorrowingRecord = borrowingRecordService.updateBorrowingRecord(id, borrowingRecord);
        return ResponseEntity.ok(updatedBorrowingRecord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrowingRecord(@PathVariable Long id) {
        borrowingRecordService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<BorrowingRecord>> searchBorrowingRecords(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookId) {
        if (userId != null) {
            return ResponseEntity.ok(borrowingRecordService.findByUserId(userId));
        } else if (bookId != null) {
            return ResponseEntity.ok(borrowingRecordService.findByBookId(bookId));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}