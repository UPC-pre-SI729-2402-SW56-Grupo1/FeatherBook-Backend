package com.featherbook.featherbookbackend.library.application.commandservices;

import com.featherbook.featherbookbackend.library.domain.exceptions.CommentNotFoundException;
import com.featherbook.featherbookbackend.library.domain.model.entities.Comment;
import com.featherbook.featherbookbackend.library.infrastructure.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * CommentCommandService
 * <p>
 *     Service responsible for handling commands related to the `Comment` entity.
 * </p>
 */
@Service
public class CommentCommandService {

    private final CommentRepository commentRepository;

    /**
     * Constructor.
     *
     * @param commentRepository The repository for the `Comment` entity.
     */
    public CommentCommandService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Creates a new comment.
     *
     * @param comment The comment to create.
     * @return The created comment.
     */
    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    /**
     * Finds a comment by ID.
     *
     * @param commentId The ID of the comment to find.
     * @return The found comment.
     * @throws CommentNotFoundException if the comment is not found.
     */
    @Transactional(readOnly = true)
    public Comment findCommentById(String commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));
    }

    /**
     * Updates a comment by ID.
     *
     * @param commentId      The ID of the comment to update.
     * @param updatedComment The updated comment details.
     * @return The updated comment.
     * @throws CommentNotFoundException if the comment is not found.
     */
    @Transactional
    public Comment updateComment(String commentId, Comment updatedComment) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));

        updatedComment.setId(commentId);
        return commentRepository.save(updatedComment);
    }

    /**
     * Deletes a comment by ID.
     *
     * @param commentId The ID of the comment to delete.
     * @throws CommentNotFoundException if the comment is not found.
     */
    @Transactional
    public void deleteComment(String commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new CommentNotFoundException("Comment not found with ID: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }
}
