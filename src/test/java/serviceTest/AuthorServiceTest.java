package serviceTest;

import com.example.librarySystem.exception.CustomException;
import com.example.librarySystem.model.Author;
import com.example.librarySystem.repository.AuthorRepository;
import com.example.librarySystem.service.AuthorService;
import com.example.librarySystem.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorServiceTest {

    private AuthorRepository authorRepository;
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorRepository = mock(AuthorRepository.class);
        authorService = new AuthorServiceImpl(authorRepository);
    }

    @Test
    void findAll_authorsExist_returnsAllAuthors() {
        List<Author> authors = List.of(new Author(), new Author());
        when(authorRepository.findAll()).thenReturn(authors);

        List<Author> result = authorService.findAll();

        assertEquals(authors, result);
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    void findById_authorExists_returnsAuthor() {
        Author author = new Author();
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Author result = authorService.findById(1L);

        assertEquals(author, result);
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void findById_authorDoesNotExist_throwsException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> authorService.findById(1L));
        assertEquals("Author id not found!", exception.getMessage());
    }

    @Test
    void save_validAuthor_returnsSavedAuthor() {
        Author author = new Author();
        when(authorRepository.save(author)).thenReturn(author);

        Author result = authorService.save(author);

        assertEquals(author, result);
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void updateAuthor_authorExists_updatesAndReturnsAuthor() {
        Author existingAuthor = new Author();
        existingAuthor.setId(1L);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(existingAuthor)).thenReturn(existingAuthor);

        Author updatedAuthor = new Author();
        updatedAuthor.setName("New Name");

        Author result = authorService.updateAuthor(1L, updatedAuthor);

        assertEquals("New Name", result.getName());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(existingAuthor);
    }

    @Test
    void updateAuthor_authorDoesNotExist_throwsException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> authorService.updateAuthor(1L, new Author()));
        assertEquals("Id not found!", exception.getMessage());
    }

    @Test
    void deleteById_authorExists_deletesAuthor() {
        Author author = new Author();
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        authorService.deleteById(1L);

        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_authorDoesNotExist_throwsException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> authorService.deleteById(1L));
        assertEquals("Id not found!", exception.getMessage());
    }
}
