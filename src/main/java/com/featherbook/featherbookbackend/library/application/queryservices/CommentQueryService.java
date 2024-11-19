package com.featherbook.featherbookbackend.library.application.queryservices;

import com.featherbook.featherbookbackend.library.domain.model.entities.Comment;
import com.featherbook.featherbookbackend.library.infrastructure.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * CommentQueryService
 * <p>
 *     Service responsible for handling queries related to the `Comment` entity.
 * </p>
 */
@Service
public class CommentQueryService {

    private final CommentRepository commentRepository;

    /**
     * Constructor.
     *
     * @param commentRepository The repository for the `Comment` entity.
     */
    public CommentQueryService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Finds a comment by ID.
     *
     * @param commentId The ID of the comment to find.
     * @return The found comment.
     */
    public Optional<Comment> getCommentById(String commentId) {
        return commentRepository.findById(commentId);
    }

    /**
     * Finds all comments.
     *
     * @return The list of all comments.
     */
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}
