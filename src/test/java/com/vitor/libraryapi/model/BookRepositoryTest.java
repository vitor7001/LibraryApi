package com.vitor.libraryapi.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.vitor.libraryapi.model.entity.Book;
import com.vitor.libraryapi.model.repository.BookRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

	@Autowired
	TestEntityManager tem;

	@Autowired
	BookRepository repository;

	@Test
	@DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado.")
	public void returnTrueWhehIsbnWxists() {
		// cenário
		String isbn = "123";

		Book book = Book.builder().title("Aventuras").autor("Joao").isbn(isbn).build();

		tem.persist(book);

		// execução
		boolean exists = repository.existsByIsbn(isbn);

		// verificação
		assertThat(exists).isTrue();

	}

	@Test
	@DisplayName("Deve retornar falso quando não existir um livro na base com o isbn informado.")
	public void returnFalseWhehIsbnDoesntExists() {
		// cenário
		String isbn = "123";

		// execução
		boolean exists = repository.existsByIsbn(isbn);

		// verificação
		assertThat(exists).isFalse();

	}

}