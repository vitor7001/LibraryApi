package com.vitor.libraryapi.service;

import java.util.Optional;

import com.vitor.libraryapi.model.entity.Book;

public interface BookService {

	Book save(Book any);

	Optional<Book> getById(Long id);

	void delete(Book book);

	Book update(Book book);

}
