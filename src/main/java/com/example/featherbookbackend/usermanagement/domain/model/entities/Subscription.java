package com.example.featherbookbackend.usermanagement.domain.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Min(1)
    private int level;

    @NotBlank
    private String name;

    @Min(0)
    private double price;

    private Date startDate;
    private Date endDate;

    // Relación con el usuario
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
