package com.vitor.libraryapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitor.libraryapi.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	boolean existsByIsbn(String isbn);

}
