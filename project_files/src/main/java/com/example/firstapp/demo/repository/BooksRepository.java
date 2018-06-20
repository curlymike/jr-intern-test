package com.example.firstapp.demo.repository;

import com.example.firstapp.demo.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor {
    List<Book> findByTitleContaining(String string);
    List<Book> findByDescriptionContaining(String string);
    List<Book> findByTitleContainingOrDescriptionContaining(String string, String string2);
    List<Book> findByIsbnEquals(String string);
    Book findFirstByIsbnEquals(String isbn);
    Book findFirstByIdIsNotAndIsbnEquals(Long id, String isbn);
}
