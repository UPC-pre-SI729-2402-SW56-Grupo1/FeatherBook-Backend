package com.example.featherbookbackend.library.interfaces.rest.controller;

import com.example.featherbookbackend.library.application.commandservices.BookCommandService;
import com.example.featherbookbackend.library.application.queryservices.BookQueryService;
import com.example.featherbookbackend.library.domain.model.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

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
    @Operation(summary = "Add a new book", description = "Adds a new book to the library.")
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
    @Operation(summary = "Get book by ID", description = "Retrieves a book by its ID.")
    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(
            @Parameter(description = "ID of the book to retrieve", required = true)
            @PathVariable String bookId) {
        Optional<Book> book = bookQueryService.getBookById(bookId);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para listar todos los libros.
     *
     * @return Lista de todos los libros.
     */
    @Operation(summary = "Get all books", description = "Retrieves a list of all books in the library.")
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
    @Operation(summary = "Update a book", description = "Updates an existing book's details.")
    @PutMapping("/{bookId}")
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "ID of the book to update", required = true)
            @PathVariable String bookId,
            @RequestBody Book book) {
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
    @Operation(summary = "Delete a book", description = "Deletes a book from the library.")
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID of the book to delete", required = true)
            @PathVariable String bookId) {
        Optional<Book> book = bookQueryService.getBookById(bookId);
        if (book.isPresent()) {
            bookCommandService.deleteBook(bookId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
