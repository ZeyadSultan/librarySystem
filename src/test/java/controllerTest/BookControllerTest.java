package controllerTest;

import com.example.librarySystem.controller.BookController;
import com.example.librarySystem.model.Author;
import com.example.librarySystem.model.Book;
import com.example.librarySystem.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BookControllerTest {

    private BookService bookService;

    private BookController bookController;

    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        bookService = mock(BookService.class);
        bookController = new BookController(bookService);
        book1 = new Book(1L,"Title One","ISBN001",new Date(2023,1,1),"Action",true,new Author(1L,"Author One",new Date(),"American",new ArrayList<Book>()));
        book2 = new Book(1L,"Title Two","ISBN002",new Date(2023,1,1),"Action",true,new Author());
    }

    @Test
    public void testGetAllBooks() {
        when(bookService.findAll()).thenReturn(Arrays.asList(book1, book2));
        List<Book> books = bookController.getAllBooks();
        assertEquals(2, books.size());
        assertEquals("Title One", books.get(0).getTitle());
        assertEquals("Title Two", books.get(1).getTitle());
    }

    @Test
    public void testGetBookById() {
        when(bookService.findById(1L)).thenReturn(book1);
        ResponseEntity<Book> response = bookController.getBookById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Title One", response.getBody().getTitle());
    }

    @Test
    public void testCreateBook() {
        when(bookService.save(book1)).thenReturn(book1);
        ResponseEntity<Book> response = bookController.createBook(book1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Title One", response.getBody().getTitle());
    }

    @Test
    public void testUpdateBook() {
        when(bookService.updateBook(1L, book1)).thenReturn(book1);
        ResponseEntity<Book> response = bookController.updateBook(1L, book1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Title One", response.getBody().getTitle());
    }

    @Test
    public void testDeleteBook() {
        doNothing().when(bookService).deleteById(1L);
        ResponseEntity<String> response = bookController.deleteBook(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book deleted successfully!", response.getBody());
    }

    @Test
    public void testSearchBooksByTitle() {
        when(bookService.findByTitle("Title One")).thenReturn(Arrays.asList(book1));
        ResponseEntity<List<Book>> response = bookController.searchBooks("Title One", null, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Title One", response.getBody().get(0).getTitle());
    }

    @Test
    public void testSearchBooksByAuthor() {
        when(bookService.findByAuthor("Author One")).thenReturn(Arrays.asList(book1));
        ResponseEntity<List<Book>> response = bookController.searchBooks(null, "Author One", null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Author One", response.getBody().get(0).getAuthor().getName());
    }

    @Test
    public void testSearchBooksByIsbn() {
        when(bookService.findByIsbn("ISBN001")).thenReturn(Arrays.asList(book1));
        ResponseEntity<List<Book>> response = bookController.searchBooks(null, null, "ISBN001");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("ISBN001", response.getBody().get(0).getIsbn());
    }

    @Test
    public void testSearchBooksBadRequest() {
        ResponseEntity<List<Book>> response = bookController.searchBooks(null, null, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
