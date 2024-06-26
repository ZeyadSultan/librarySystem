package serviceTest;

import com.example.librarySystem.exception.ApiError;
import com.example.librarySystem.exception.CustomException;
import com.example.librarySystem.model.Author;
import com.example.librarySystem.model.Book;
import com.example.librarySystem.repository.BookRepository;
import com.example.librarySystem.service.AuthorService;
import com.example.librarySystem.service.BookService;
import com.example.librarySystem.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    private BookRepository bookRepository;

    private AuthorService authorService;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        authorService = mock(AuthorService.class);
        bookService = new BookServiceImpl(bookRepository, authorService);
    }

    @Test
    void findAll_booksExist_returnsAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book 1", "1234567890", null, "Fiction", true, null));
        books.add(new Book(2L, "Book 2", "2345678901", null, "Non-fiction", true, null));
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.findAll();

        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Book 2", result.get(1).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void findById_bookExists_returnsBook() {
        Book book = new Book(1L, "Book 1", "1234567890", null, "Fiction", true, null);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.findById(1L);

        assertNotNull(result);
        assertEquals("Book 1", result.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void findById_bookDoesNotExist_throwsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> bookService.findById(1L));
        assertEquals("Book id not found!", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void save_validBook_returnsSavedBook() {
        Author author = new Author();
        author.setId(1L);
        Book bookToSave = new Book(1L, "Book 1", "1234567890", null, "Fiction", true, author);
        when(authorService.findById(1L)).thenReturn(author);
        when(bookRepository.save(bookToSave)).thenReturn(bookToSave);

        Book result = bookService.save(bookToSave);

        assertNotNull(result);
        assertEquals("Book 1", result.getTitle());
        verify(authorService, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(bookToSave);
    }

    @Test
    void save_bookWithoutAuthor_throwsException() {
        Book bookWithoutAuthor = new Book();
        bookWithoutAuthor.setTitle("Book Without Author");

        CustomException exception = assertThrows(CustomException.class, () -> bookService.save(bookWithoutAuthor));
        assertEquals("Author ID is required to create a book.", exception.getMessage());
        verify(authorService, never()).findById(anyLong());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_bookExists_updatesAndReturnsBook() {
        Author existingAuthor = new Author();
        existingAuthor.setId(1L);
        Book existingBook = new Book(1L, "Existing Book", "1234567890", null, "Fiction", true, existingAuthor);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(authorService.findById(existingAuthor.getId())).thenReturn(existingAuthor);

        Book updatedBook = new Book(1L, "Updated Book", "0987654321", null, "Non-fiction", false, existingAuthor);
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        Book result = bookService.updateBook(1L, updatedBook);

        assertNotNull(result);
        assertEquals("Updated Book", result.getTitle());
        assertEquals("0987654321", result.getIsbn());
        assertFalse(result.isAvailable());

        verify(bookRepository, times(1)).findById(1L);
        verify(authorService, times(1)).findById(existingAuthor.getId());
        verify(bookRepository, times(1)).save(existingBook); // Ensure save was called with existingBook
    }



    @Test
    void updateBook_bookDoesNotExist_throwsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> bookService.updateBook(1L, new Book()));
        assertEquals("book id not found!", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
        verify(authorService, never()).findById(anyLong());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void deleteById_bookDoesNotExist_throwsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> bookService.deleteById(1L));
        assertEquals("Id not found!", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void findByTitle_booksExistWithGivenTitle_returnsBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Java Programming", "1234567890", null, "Programming", true, null));
        books.add(new Book(2L, "Advanced Java", "2345678901", null, "Programming", true, null));
        when(bookRepository.findByTitleContaining("Java")).thenReturn(books);

        List<Book> result = bookService.findByTitle("Java");

        assertEquals(2, result.size());
        assertEquals("Java Programming", result.get(0).getTitle());
        assertEquals("Advanced Java", result.get(1).getTitle());
        verify(bookRepository, times(1)).findByTitleContaining("Java");
    }

    @Test
    void findByTitle_noBooksExistWithGivenTitle_throwsException() {
        when(bookRepository.findByTitleContaining("Python")).thenReturn(new ArrayList<>());

        CustomException exception = assertThrows(CustomException.class, () -> bookService.findByTitle("Python"));
        assertEquals("Book with this title not found!", exception.getMessage());
        verify(bookRepository, times(1)).findByTitleContaining("Python");
    }

    @Test
    void findByAuthor_booksExistWithGivenAuthor_returnsBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book 1", "1234567890", null, "Fiction", true, null));
        when(bookRepository.findByAuthorNameContaining("John Doe")).thenReturn(books);

        List<Book> result = bookService.findByAuthor("John Doe");

        assertEquals(1, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        verify(bookRepository, times(1)).findByAuthorNameContaining("John Doe");
    }

    @Test
    void findByAuthor_noBooksExistWithGivenAuthor_throwsException() {
        when(bookRepository.findByAuthorNameContaining("Jane Smith")).thenReturn(new ArrayList<>());

        CustomException exception = assertThrows(CustomException.class, () -> bookService.findByAuthor("Jane Smith"));
        assertEquals("No book found with this author!", exception.getMessage());
        verify(bookRepository, times(1)).findByAuthorNameContaining("Jane Smith");
    }

    @Test
    void findByIsbn_booksExistWithGivenIsbn_returnsBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book 1", "1234567890", null, "Fiction", true, null));
        when(bookRepository.findByIsbnContaining("1234567890")).thenReturn(books);

        List<Book> result = bookService.findByIsbn("1234567890");

        assertEquals(1, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        verify(bookRepository, times(1)).findByIsbnContaining("1234567890");
    }

    @Test
    void findByIsbn_noBooksExistWithGivenIsbn_throwsException() {
        when(bookRepository.findByIsbnContaining("0987654321")).thenReturn(new ArrayList<>());

        CustomException exception = assertThrows(CustomException.class, () -> bookService.findByIsbn("0987654321"));
        assertEquals("No book found with this isbn!", exception.getMessage());
        verify(bookRepository, times(1)).findByIsbnContaining("0987654321");
    }

}
