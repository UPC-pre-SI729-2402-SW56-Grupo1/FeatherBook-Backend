package com.example.featherbookbackend.library.application.queryservices;

import com.example.featherbookbackend.library.domain.model.entities.Comment;
import com.example.featherbookbackend.library.infrastructure.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentQueryService {

    @Autowired
    private CommentRepository commentRepository;

    public Optional<Comment> getCommentById(String commentId) {
        return commentRepository.findById(commentId);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}
