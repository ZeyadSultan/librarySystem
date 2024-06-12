package com.example.librarySystem.service;


import com.example.librarySystem.model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
    Author findById(Long id);
    Author save(Author author);
    Author updateAuthor(Long id, Author author);
    void deleteById(Long id);
}
