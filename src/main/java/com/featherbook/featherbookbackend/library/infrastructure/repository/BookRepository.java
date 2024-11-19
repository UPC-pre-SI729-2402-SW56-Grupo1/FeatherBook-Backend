package com.featherbook.featherbookbackend.library.infrastructure.repository;

import com.featherbook.featherbookbackend.library.domain.model.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findByNameOrBookUrl(String name, String bookUrl);

    @Query("SELECT b FROM Book b WHERE FUNCTION('RAND') <= :accessibilityThreshold")
    List<Book> findBooksByAccessLevel(@Param("accessibilityThreshold") double accessibilityThreshold);

}
