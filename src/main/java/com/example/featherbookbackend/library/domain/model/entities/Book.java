package com.example.featherbookbackend.library.domain.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Book name is mandatory")
    @Size(min = 3, max = 100, message = "Book name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Summary is mandatory")
    @Size(max = 500, message = "Summary must not exceed 500 characters")
    private String summary;

    @NotBlank(message = "Category is mandatory")
    private String category;

    @NotBlank(message = "Book URL is mandatory")
    private String bookUrl;

    private int views;

    private int totalScore;

    // Relación con comentarios
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> reviews;
}
