package com.example.featherbookbackend.library.domain.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    private String userId;  // ID del usuario que hizo el comentario

    @NotBlank
    @Size(max = 500)
    private String content;  // Contenido del comentario

    @Min(1)
    @Max(10)
    private int score;  // Puntuación dada al libro

    // Relación con la entidad Book
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
