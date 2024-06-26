package com.example.librarySystem.repository;

import com.example.librarySystem.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    void deleteById(long id);

    List<Book> findByTitleContaining(String title);

    List<Book> findByAuthorNameContaining(String author);

    List<Book> findByIsbnContaining(String isbn);
}
