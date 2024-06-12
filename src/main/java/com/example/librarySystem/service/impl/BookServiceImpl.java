package com.example.librarySystem.service.impl;

import com.example.librarySystem.exception.ApiError;
import com.example.librarySystem.model.Author;
import com.example.librarySystem.model.Book;
import com.example.librarySystem.repository.BookRepository;
import com.example.librarySystem.service.AuthorService;
import com.example.librarySystem.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;


    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if(!book.isPresent()) {
            throw ApiError.notFound("Id not found!");
        }
        else {
            return book.get();
        }
    }

    @Override
    public Book save(Book book) {
        if (book.getAuthor() != null) {
            Author author = authorService.findById(book.getAuthor().getId());
            if (author == null) {
                throw ApiError.notFound("Author not found with ID: " + book.getAuthor().getId());
            }
            book.setAuthor(author);
            
            return bookRepository.save(book);
        } else {
            throw ApiError.badRequest("Author ID is required to create a book.");
        }
    }

    @Override
    public Book updateBook(Long id, Book book) {
        Optional<Book> savedBook = bookRepository.findById(id);
        if(!savedBook.isPresent()) {
            throw ApiError.notFound("Id not found!");
        }
        else {
            Book existingBook = savedBook.get();
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setIsbn(book.getIsbn());
            existingBook.setPublicationDate(book.getPublicationDate());
            return bookRepository.save(existingBook);
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if(!book.isPresent()) {
            throw ApiError.notFound("Id not found!");
        }
        else {
            bookRepository.deleteById(id);
        }
    }

    @Override
    public List<Book> findByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContaining(title);
        if(books.isEmpty()) {
            throw ApiError.notFound("Book with this title not found!");
        }
        else {
            return books;
        }
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> books =  bookRepository.findByAuthorNameContaining(author);
        if(books.isEmpty()) {
            throw ApiError.notFound("No book found with this author!");
        }
        else {
            return books;
        }
    }

    @Override
    public List<Book> findByIsbn(String isbn) {
        List<Book> books =  bookRepository.findByIsbnContaining(isbn);
        if(books.isEmpty()) {
            throw ApiError.notFound("No book found with this isbn!");
        }
        else {
            return books;
        }
    }
}
