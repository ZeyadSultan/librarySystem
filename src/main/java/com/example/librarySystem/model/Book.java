package com.example.librarySystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @NotBlank
    private String title;

    @NotBlank
    private String isbn;

    private Date publicationDate;

    private String genre;

    private boolean available;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

}
