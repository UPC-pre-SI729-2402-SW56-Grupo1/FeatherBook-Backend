package com.example.featherbookbackend.library.interfaces.rest.controller;

import com.example.featherbookbackend.library.application.commandservices.CommentCommandService;
import com.example.featherbookbackend.library.application.queryservices.CommentQueryService;
import com.example.featherbookbackend.library.domain.model.entities.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentCommandService commentCommandService;

    @Autowired
    private CommentQueryService commentQueryService;

    /**
     * Endpoint para agregar un nuevo comentario a un libro.
     *
     * @param comment El comentario a agregar.
     * @return El comentario creado.
     */
    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        Comment createdComment = commentCommandService.createComment(comment);
        return ResponseEntity.ok(createdComment);
    }

    /**
     * Endpoint para actualizar un comentario existente.
     *
     * @param commentId El ID del comentario a actualizar.
     * @param comment Datos actualizados del comentario.
     * @return El comentario actualizado, o not found si no existe.
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable String commentId, @RequestBody Comment comment) {
        Comment updatedComment = commentCommandService.updateComment(commentId, comment);
        if (updatedComment != null) {
            return ResponseEntity.ok(updatedComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para eliminar un comentario por su ID.
     *
     * @param commentId El ID del comentario a eliminar.
     * @return Respuesta no content o not found.
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId) {
        Optional<Comment> comment = commentQueryService.getCommentById(commentId);
        if (comment.isPresent()) {
            commentCommandService.deleteComment(commentId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para obtener todos los comentarios.
     *
     * @return Lista de todos los comentarios.
     */
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentQueryService.getAllComments();
        return ResponseEntity.ok(comments);
    }
}
