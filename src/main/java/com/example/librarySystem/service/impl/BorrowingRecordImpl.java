package com.example.librarySystem.service.impl;

import com.example.librarySystem.exception.ApiError;
import com.example.librarySystem.model.Book;
import com.example.librarySystem.model.BorrowingRecord;
import com.example.librarySystem.model.Customer;
import com.example.librarySystem.repository.BorrowingRecordRepository;
import com.example.librarySystem.service.BookService;
import com.example.librarySystem.service.BorrowingRecordService;
import com.example.librarySystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowingRecordImpl implements BorrowingRecordService {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final CustomerService customerService;
    private final BookService bookService;

    public BorrowingRecordImpl(BorrowingRecordRepository borrowingRecordRepository, @Lazy CustomerService customerService, BookService bookService) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.customerService = customerService;
        this.bookService = bookService;
    }

    @Override
    public List<BorrowingRecord> findAll() {
        return borrowingRecordRepository.findAll();
    }

    @Override
    public BorrowingRecord findById(Long id) {
        Optional<BorrowingRecord> borrowingRecord = borrowingRecordRepository.findById(id);
        if (!borrowingRecord.isPresent()) {
            throw ApiError.notFound("Borrowing id not found!");
        } else {
            return borrowingRecord.get();
        }
    }

    @Override
    public BorrowingRecord save(BorrowingRecord borrowingRecord) {

        Long bookId = borrowingRecord.getBook().getId();
        Date borrowingDate = borrowingRecord.getBorrowDate();
        Date returnDate = borrowingRecord.getReturnDate();

        if(!bookService.findById(bookId).isAvailable()) {
            throw ApiError.badRequest("book isn't available to borrow!");
        }

        List<BorrowingRecord> bookRecords = borrowingRecordRepository.findByBookId(bookId);

        if(returnDate.before(borrowingDate)) {
            throw ApiError.badRequest("Return date must be after the borrowing date!");
        }

        for (BorrowingRecord record : bookRecords) {
            if (record.getReturnDate() == null ||
                    (borrowingDate.before(record.getReturnDate()) && returnDate.after(record.getBorrowDate()))) {
                throw ApiError.badRequest("Book is currently borrowed and not available during the specified period!");
            }
        }
        Customer customer = customerService.findById(borrowingRecord.getCustomer().getId());

        Book book = bookService.findById(borrowingRecord.getBook().getId());

        borrowingRecord.setCustomer(customer);
        borrowingRecord.setBook(book);
        return borrowingRecordRepository.save(borrowingRecord);
    }

    @Override
    public BorrowingRecord updateBorrowingRecord(Long id, BorrowingRecord borrowingRecord) {
        Optional<BorrowingRecord> existingRecordOpt = borrowingRecordRepository.findById(id);
        if (!existingRecordOpt.isPresent()) {
            throw ApiError.notFound("Borrowing record not found!");
        }

        BorrowingRecord existingRecord = existingRecordOpt.get();
        Long bookId = borrowingRecord.getBook().getId();
        Date borrowingDate = borrowingRecord.getBorrowDate();
        Date returnDate = borrowingRecord.getReturnDate();

        if(returnDate.before(borrowingDate)) {
            throw ApiError.badRequest("Return date must be after the borrowing date!");
        }

        if(!bookService.findById(bookId).isAvailable()) {
            throw ApiError.badRequest("book isn't available to borrow!");
        }

        List<BorrowingRecord> bookRecords = borrowingRecordRepository.findByBookId(bookId);

        for (BorrowingRecord record : bookRecords) {
            if (!record.getId().equals(id) &&
                    (record.getReturnDate() == null ||
                            borrowingDate.before(record.getReturnDate()))) {
                throw ApiError.badRequest("Book is currently borrowed and not available during the specified period!");
            }
        }

        existingRecord.setBook(borrowingRecord.getBook());
        existingRecord.setCustomer(borrowingRecord.getCustomer());
        existingRecord.setBorrowDate(borrowingRecord.getBorrowDate());
        existingRecord.setReturnDate(borrowingRecord.getReturnDate());

        Customer customer = customerService.findById(borrowingRecord.getCustomer().getId());

        Book book = bookService.findById(borrowingRecord.getBook().getId());

        existingRecord.setCustomer(customer);
        existingRecord.setBook(book);
        return borrowingRecordRepository.save(existingRecord);
    }

    @Override
    public void deleteById(Long id) {
        Optional<BorrowingRecord> borrowingRecord = borrowingRecordRepository.findById(id);
        if (!borrowingRecord.isPresent()) {
            throw ApiError.notFound("Id not found!");
        } else {
            borrowingRecordRepository.deleteById(id);
        }
    }

    @Override
    public List<BorrowingRecord> findByUserId(Long customerId) {
        return borrowingRecordRepository.findByCustomerId(customerId);
    }

    @Override
    public List<BorrowingRecord> findByBookId(Long bookId) {
        return borrowingRecordRepository.findByBookId(bookId);
    }

    @Override
    public boolean hasActiveBorrowings(Customer customer) {
        List<BorrowingRecord> borrowingRecords = borrowingRecordRepository.findByCustomerId(customer.getId());

        if(borrowingRecords.size()>0)
            return true;
        else
           return false;
    }
}
