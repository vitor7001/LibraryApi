package com.vitor.libraryapi.service.implementacao;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vitor.libraryapi.exception.BusinessException;
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

		if (repository.existsByIsbn(book.getIsbn())) {
			throw new BusinessException("Isbn já cadastrado.");
		}

		return repository.save(book);
	}

	@Override
	public Optional<Book> getById(Long id) {
		return this.repository.findById(id);
	}

	@Override
	public void delete(Book book) {

		if (book == null || book.getId() == null) {
			throw new IllegalArgumentException("Book id cant be null");
		}

		this.repository.delete(book);
	}

	@Override
	public Book update(Book book) {

		if (book == null || book.getId() == null) {
			throw new IllegalArgumentException("Book id cant be null");
		}

		return this.repository.save(book);
	}

}
