package com.featherbook.featherbookbackend.usermanagement.domain.model.entities;

import com.featherbook.featherbookbackend.usermanagement.domain.model.enums.SubscriptionLevel;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\d{9}$", message = "Phone must be a 9-digit number")
    private String phone;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionLevel subscriptionLevel = SubscriptionLevel.LEVEL_1;

    @Column(name = "role", nullable = false)
    @NotBlank(message = "Role is mandatory")
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BookHistory> bookHistory = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<UploadedBook> uploadedBooks = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "saved_books", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "book_id")
    private List<String> savedBooks = new ArrayList<>();
}