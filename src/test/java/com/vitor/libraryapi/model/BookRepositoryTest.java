package com.vitor.libraryapi.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

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

		Book book = createBook(isbn);

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

	@Test
	@DisplayName("Deve obter um livro por id")
	public void findByIdTest() {

		Book book = createBook("123");
		tem.persist(book);

		Optional<Book> foundBook = repository.findById(book.getId());

		assertThat(foundBook.isPresent()).isTrue();

	}

	@Test
	@DisplayName("Deve deletar um livro pelo id informado.")
	public void deleteBookById() {

		Book book = createBook("12");
		tem.persist(book);

		repository.delete(book);

		assertThat(repository.findById(book.getId())).isEmpty();

	}

	@Test
	@DisplayName("Deve salvar um livro.")
	public void saveBookTest() {

		Book book = createBook("12");

		Book savedBook = repository.save(book);

		assertThat(savedBook.getId()).isNotNull();
	}

	@Test
	@DisplayName("Deve deletar um livro.")
	public void deleteBookTest() {

		Book book = createBook("12");

		tem.persist(book);

		Book foundBook = tem.find(Book.class, book.getId());

		repository.delete(foundBook);

		Book deletedBook = tem.find(Book.class, book.getId());

		assertThat(deletedBook).isNull();
	}

	public static Book createBook(String isbn) {
		return Book.builder().title("Aventuras").autor("Joao").isbn(isbn).build();
	}

}
