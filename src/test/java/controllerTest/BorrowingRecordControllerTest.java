package controllerTest;

import com.example.librarySystem.controller.BorrowingRecordController;
import com.example.librarySystem.model.Book;
import com.example.librarySystem.model.BorrowingRecord;
import com.example.librarySystem.model.Customer;
import com.example.librarySystem.service.BorrowingRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BorrowingRecordControllerTest {

    private BorrowingRecordService borrowingRecordService;
    private BorrowingRecordController borrowingRecordController;

    private BorrowingRecord borrowingRecord1;
    private BorrowingRecord borrowingRecord2;

    @BeforeEach
    public void setUp() {
        borrowingRecordService = Mockito.mock(BorrowingRecordService.class);
        borrowingRecordController = new BorrowingRecordController(borrowingRecordService);

        borrowingRecord1 = new BorrowingRecord(1L,new Customer(), new Book(), new Date(2023,1,1), new Date(2024,1,1));
        borrowingRecord1 = new BorrowingRecord(2L,new Customer(), new Book(), new Date(2023,1,1), new Date(2024,1,1));
    }

    @Test
    public void testGetAllBorrowingRecords() {
        when(borrowingRecordService.findAll()).thenReturn(Arrays.asList(borrowingRecord1, borrowingRecord2));
        List<BorrowingRecord> borrowingRecords = borrowingRecordController.getAllBorrowingRecords();
        assertEquals(2, borrowingRecords.size());
    }

    @Test
    public void testGetBorrowingRecordById() {
        when(borrowingRecordService.findById(1L)).thenReturn(borrowingRecord1);
        ResponseEntity<BorrowingRecord> response = borrowingRecordController.getBorrowingRecordById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(borrowingRecord1, response.getBody());
    }

    @Test
    public void testCreateBorrowingRecord() {
        when(borrowingRecordService.save(borrowingRecord1)).thenReturn(borrowingRecord1);
        ResponseEntity<BorrowingRecord> response = borrowingRecordController.createBorrowingRecord(borrowingRecord1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(borrowingRecord1, response.getBody());
    }

    @Test
    public void testUpdateBorrowingRecord() {
        when(borrowingRecordService.updateBorrowingRecord(1L, borrowingRecord1)).thenReturn(borrowingRecord1);
        ResponseEntity<BorrowingRecord> response = borrowingRecordController.updateBorrowingRecord(1L, borrowingRecord1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(borrowingRecord1, response.getBody());
    }

    @Test
    public void testDeleteBorrowingRecord() {
        doNothing().when(borrowingRecordService).deleteById(1L);
        ResponseEntity<Void> response = borrowingRecordController.deleteBorrowingRecord(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testSearchBorrowingRecordsByUserId() {
        when(borrowingRecordService.findByUserId(1L)).thenReturn(Arrays.asList(borrowingRecord1));
        ResponseEntity<List<BorrowingRecord>> response = borrowingRecordController.searchBorrowingRecords(1L, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testSearchBorrowingRecordsByBookId() {
        when(borrowingRecordService.findByBookId(1L)).thenReturn(Arrays.asList(borrowingRecord1));
        ResponseEntity<List<BorrowingRecord>> response = borrowingRecordController.searchBorrowingRecords(null, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testSearchBorrowingRecordsBadRequest() {
        ResponseEntity<List<BorrowingRecord>> response = borrowingRecordController.searchBorrowingRecords(null, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

