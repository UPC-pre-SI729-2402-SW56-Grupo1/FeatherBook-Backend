package com.example.featherbookbackend.library.interfaces.rest.controller;

import com.example.featherbookbackend.library.application.commandservices.BookCommandService;
import com.example.featherbookbackend.library.application.queryservices.BookQueryService;
import com.example.featherbookbackend.library.domain.model.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookCommandService bookCommandService;

    @Autowired
    private BookQueryService bookQueryService;

    /**
     * Endpoint para crear un nuevo libro.
     *
     * @param book Información del libro a crear.
     * @return El libro creado.
     */
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookCommandService.createBook(book);
        return ResponseEntity.ok(createdBook);
    }

    /**
     * Endpoint para obtener detalles de un libro específico.
     *
     * @param bookId ID del libro a obtener.
     * @return El libro, si se encuentra.
     */
    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable String bookId) {
        Optional<Book> book = bookQueryService.getBookById(bookId);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para listar todos los libros.
     *
     * @return Lista de todos los libros.
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookQueryService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * Endpoint para actualizar un libro existente.
     *
     * @param bookId ID del libro a actualizar.
     * @param book Datos actualizados del libro.
     * @return El libro actualizado, si se encuentra.
     */
    @PutMapping("/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable String bookId, @RequestBody Book book) {
        Book updatedBook = bookCommandService.updateBook(bookId, book);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para eliminar un libro por su ID.
     *
     * @param bookId ID del libro a eliminar.
     * @return Respuesta de éxito o not found.
     */
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable String bookId) {
        Optional<Book> book = bookQueryService.getBookById(bookId);
        if (book.isPresent()) {
            bookCommandService.deleteBook(bookId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
