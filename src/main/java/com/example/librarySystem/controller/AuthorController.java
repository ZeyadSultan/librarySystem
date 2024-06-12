package com.example.librarySystem.controller;

import com.example.librarySystem.model.Author;
import com.example.librarySystem.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<Author>> getAuthors() {
        List<Author> authors = authorService.findAll();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable("id") Long id) {
        Author author = authorService.findById(id);
        return ResponseEntity.ok(author);
    }

    @PostMapping
    public ResponseEntity<Author> postAuthor(@Valid @RequestBody Author author) {
        Author author1 = authorService.save(author);
        return ResponseEntity.ok(author1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> postAuthor(@PathVariable("id") Long id, @Valid @RequestBody Author author) {
        Author author1 = authorService.updateAuthor(id,author);
        return ResponseEntity.ok(author1);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteById(id);
        return ResponseEntity.ok("Author deleted Successfully!");
    }
}
