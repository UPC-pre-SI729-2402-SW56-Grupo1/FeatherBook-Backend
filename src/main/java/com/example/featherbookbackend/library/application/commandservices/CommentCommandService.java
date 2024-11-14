package com.example.featherbookbackend.library.application.commandservices;

import com.example.featherbookbackend.library.domain.model.entities.Comment;
import com.example.featherbookbackend.library.infrastructure.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CommentCommandService {

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(String commentId, Comment updatedComment) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            updatedComment.setId(commentId);
            return commentRepository.save(updatedComment);
        }
        return null;  // Implementar manejo de errores si es necesario
    }

    @Transactional
    public void deleteComment(String commentId) {
        commentRepository.deleteById(commentId);
    }
}
