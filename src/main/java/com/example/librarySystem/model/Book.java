package com.example.librarySystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotBlank(message = "Book title can not be blank!")
    private String title;

    @NotBlank(message = "Book isbn can not be blank!")
    private String isbn;

    private Date publicationDate;

    private String genre;

    private boolean available;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

}
