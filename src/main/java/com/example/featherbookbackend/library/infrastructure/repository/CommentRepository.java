package com.example.featherbookbackend.library.infrastructure.repository;

import com.example.featherbookbackend.library.domain.model.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
}