package com.vitor.libraryapi.service.implementacao;

import org.springframework.stereotype.Service;

import com.vitor.libraryapi.model.entity.Book;
import com.vitor.libraryapi.model.repository.BookRepository;
import com.vitor.libraryapi.service.BookService;

@Service
public class BookServiceImplementacao implements BookService {

	private BookRepository repository;

	public BookServiceImplementacao(BookRepository repository) {
		this.repository = repository;
	}

	@Override
	public Book save(Book book) {

		return repository.save(book);
	}

}
