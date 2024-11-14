package com.example.featherbookbackend.usermanagement.infrastructure.repository;

import com.example.featherbookbackend.usermanagement.domain.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
