package com.example.featherbookbackend.library.application.commandservices;

import com.example.featherbookbackend.library.domain.model.entities.Book;
import com.example.featherbookbackend.library.infrastructure.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BookCommandService {

    @Autowired
    private BookRepository bookRepository;

    /**
     * Crea un nuevo libro si el usuario tiene el rol adecuado.
     *
     * @param book El libro a crear.
     * @param userRole El rol del usuario.
     * @return El libro creado o null si el rol no es adecuado.
     */
    @Transactional
    public Book createBook(Book book, String userRole) {
        if (!userRole.equals("ADMIN")) {
            System.out.println("User does not have permission to create a book.");
            return null;
        }
        return bookRepository.save(book);
    }

    /**
     * Actualiza un libro existente si el usuario tiene el rol adecuado.
     *
     * @param bookId El ID del libro a actualizar.
     * @param updatedBook Los detalles actualizados del libro.
     * @param userRole El rol del usuario.
     * @return El libro actualizado o null si el rol no es adecuado.
     */
    @Transactional
    public Book updateBook(String bookId, Book updatedBook, String userRole) {
        if (!userRole.equals("ADMIN")) {
            System.out.println("User does not have permission to update a book.");
            return null;
        }

        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            updatedBook.setId(bookId);
            return bookRepository.save(updatedBook);
        }
        return null;
    }

    /**
     * Elimina un libro si el usuario tiene el rol adecuado.
     *
     * @param bookId El ID del libro a eliminar.
     * @param userRole El rol del usuario.
     */
    @Transactional
    public void deleteBook(String bookId, String userRole) {
        if (!userRole.equals("ADMIN")) {
            System.out.println("User does not have permission to delete a book.");
            return;
        }
        bookRepository.deleteById(bookId);
    }
}
