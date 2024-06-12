package com.example.librarySystem.service.impl;

import com.example.librarySystem.exception.ApiError;
import com.example.librarySystem.model.Author;
import com.example.librarySystem.repository.AuthorRepository;
import com.example.librarySystem.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author findById(Long id) {
        Optional<Author> author = authorRepository.findById(id);
        if(!author.isPresent()) {
            throw ApiError.notFound("Author id not found!");
        }
        else {
            return author.get();
        }
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author updateAuthor(Long id, Author author) {
        Optional<Author> savedAuthor = authorRepository.findById(id);
        if(!savedAuthor.isPresent()) {
            throw ApiError.notFound("Id not found!");
        }
        else {
            Author existingAuthor = savedAuthor.get();
            existingAuthor.setName(author.getName());
            existingAuthor.setBirthDate(author.getBirthDate());
            existingAuthor.setNationality(author.getNationality());
            return authorRepository.save(existingAuthor);
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<Author> author = authorRepository.findById(id);
        if(!author.isPresent()) {
            throw ApiError.notFound("Id not found!");
        }
        else {
            authorRepository.deleteById(id);
        }
    }
}
