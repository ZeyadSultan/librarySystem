package controllerTest;

import com.example.librarySystem.controller.AuthorController;
import com.example.librarySystem.model.Author;
import com.example.librarySystem.model.Book;
import com.example.librarySystem.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthorControllerTest {

    private AuthorService authorService;

    private AuthorController authorController;

    private Author author1;
    private Author author2;

    @BeforeEach
    public void setUp() {
        authorService = mock(AuthorService.class);
        authorController = new AuthorController(authorService);
        author1 = new Author(1L, "Author One",new Date(2002,1,1),"Egyptian",new ArrayList<Book>());
        author2 = new Author(1L, "Author Two",new Date(2002,1,1),"American",new ArrayList<Book>());
    }

    @Test
    public void testGetAuthors() {
        when(authorService.findAll()).thenReturn(Arrays.asList(author1, author2));
        ResponseEntity<List<Author>> response = authorController.getAuthors();
        List<Author> authors = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, authors.size());
        assertEquals("Author One", authors.get(0).getName());
        assertEquals("Author Two", authors.get(1).getName());
    }

    @Test
    public void testGetAuthorById() {
        when(authorService.findById(1L)).thenReturn(author1);
        ResponseEntity<Author> response = authorController.getAuthorById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Author One", response.getBody().getName());
    }

    @Test
    public void testPostAuthor() {
        when(authorService.save(author1)).thenReturn(author1);
        ResponseEntity<Author> response = authorController.postAuthor(author1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Author One", response.getBody().getName());
    }

    @Test
    public void testUpdateAuthor() {
        when(authorService.updateAuthor(1L, author1)).thenReturn(author1);
        ResponseEntity<Author> response = authorController.updateAuthor(1L, author1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Author One", response.getBody().getName());
    }

    @Test
    public void testDeleteAuthor() {
        doNothing().when(authorService).deleteById(1L);
        ResponseEntity<String> response = authorController.deleteAuthor(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Author deleted Successfully!", response.getBody());
    }
}
