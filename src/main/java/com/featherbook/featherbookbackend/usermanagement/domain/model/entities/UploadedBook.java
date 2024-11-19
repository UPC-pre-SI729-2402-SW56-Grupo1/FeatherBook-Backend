package com.featherbook.featherbookbackend.usermanagement.domain.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "uploaded_books")
public class UploadedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "book_id", nullable = false)
    private String bookId;

    @Column(name = "uploaded_date", nullable = false)
    private LocalDate uploadedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
}
