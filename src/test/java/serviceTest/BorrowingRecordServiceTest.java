package serviceTest;

import com.example.librarySystem.exception.ApiError;
import com.example.librarySystem.exception.CustomException;
import com.example.librarySystem.model.Book;
import com.example.librarySystem.model.BorrowingRecord;
import com.example.librarySystem.model.Customer;
import com.example.librarySystem.repository.BorrowingRecordRepository;
import com.example.librarySystem.service.BookService;
import com.example.librarySystem.service.CustomerService;
import com.example.librarySystem.service.impl.BorrowingRecordImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BorrowingRecordServiceTest {

    private BorrowingRecordRepository borrowingRecordRepository;
    private CustomerService customerService;
    private BookService bookService;
    private BorrowingRecordImpl borrowingRecordService;

    @BeforeEach
    void setUp() {
        borrowingRecordRepository = mock(BorrowingRecordRepository.class);
        customerService = mock(CustomerService.class);
        bookService = mock(BookService.class);
        borrowingRecordService = new BorrowingRecordImpl(borrowingRecordRepository, customerService, bookService);
    }

    @Test
    void findAll_recordsExist_returnsAllRecords() {
        List<BorrowingRecord> records = new ArrayList<>();
        records.add(new BorrowingRecord());
        records.add(new BorrowingRecord());
        when(borrowingRecordRepository.findAll()).thenReturn(records);

        List<BorrowingRecord> result = borrowingRecordService.findAll();

        assertEquals(records, result);
        verify(borrowingRecordRepository, times(1)).findAll();
    }

    @Test
    void findById_recordExists_returnsRecord() {
        BorrowingRecord record = new BorrowingRecord();
        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.of(record));

        BorrowingRecord result = borrowingRecordService.findById(1L);

        assertEquals(record, result);
        verify(borrowingRecordRepository, times(1)).findById(1L);
    }

    @Test
    void findById_recordDoesNotExist_throwsException() {
        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> borrowingRecordService.findById(1L));

        assertEquals("Borrowing id not found!", exception.getMessage());
    }

    @Test
    void save_validRecord_returnsSavedRecord() {
        BorrowingRecord recordToSave = new BorrowingRecord();
        recordToSave.setCustomer(new Customer());
        recordToSave.setBook(new Book(1L, "Book Title", "1234567890", null, "Fiction", true, null));
        recordToSave.setBorrowDate(new Date(2023,1,1));
        recordToSave.setReturnDate(new Date(2024,1,1));

        when(bookService.findById(1L)).thenReturn(recordToSave.getBook());
        when(customerService.findById(1L)).thenReturn(recordToSave.getCustomer());
        when(borrowingRecordRepository.save(recordToSave)).thenReturn(recordToSave);

        BorrowingRecord result = borrowingRecordService.save(recordToSave);

        assertEquals(recordToSave, result);
    }

    @Test
    void updateBorrowingRecord_recordExists_updatesAndReturnsRecord() {
        BorrowingRecord existingRecord = new BorrowingRecord();

        BorrowingRecord updatedRecord = new BorrowingRecord();
        updatedRecord.setId(1L);
        updatedRecord.setCustomer(new Customer());
        updatedRecord.setBook(new Book(1L, "Updated Book", "0987654321", null, "Non-Fiction", true, null));
        updatedRecord.setBorrowDate(new Date(2023,12,1));
        updatedRecord.setReturnDate(new Date(2024,1,1));

        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.of(existingRecord));
        when(bookService.findById(updatedRecord.getBook().getId())).thenReturn(updatedRecord.getBook());
        when(customerService.findById(updatedRecord.getCustomer().getId())).thenReturn(updatedRecord.getCustomer());
        when(borrowingRecordRepository.save(existingRecord)).thenReturn(existingRecord);

        BorrowingRecord result = borrowingRecordService.updateBorrowingRecord(1L, updatedRecord);

        assertEquals(updatedRecord.getBook().getTitle(), result.getBook().getTitle());

    }

    @Test
    void deleteById_recordExists_deletesRecord() {
        BorrowingRecord existingRecord = new BorrowingRecord();

        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.of(existingRecord));
        doNothing().when(borrowingRecordRepository).deleteById(1L);

        borrowingRecordService.deleteById(1L);

        verify(borrowingRecordRepository, times(1)).findById(1L);
        verify(borrowingRecordRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_recordDoesNotExist_throwsException() {
        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> borrowingRecordService.deleteById(1L));

        assertEquals("Id not found!", exception.getMessage());
    }

}
