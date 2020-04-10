package com.vitor.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.vitor.libraryapi.model.entity.Book;
import com.vitor.libraryapi.model.repository.BookRepository;
import com.vitor.libraryapi.service.implementacao.BookServiceImplementacao;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

	BookService service;

	@MockBean
	BookRepository repository;

	@BeforeEach
	public void setUp() {
		service = new BookServiceImplementacao(repository);
	}

	@Test
	@DisplayName("Deve salvar um livro.")
	public void savebookTest() {
		// cenário
		Book book = Book.builder().isbn("123").autor("João").title("João e Maria").build();

		Mockito.when(repository.save(book))
				.thenReturn(Book.builder().id(1l).isbn("123").title("João e Maria").autor("João").build());

		// execução
		Book savedBook = service.save(book);

		// verificação

		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getIsbn()).isEqualTo("123");
		assertThat(savedBook.getTitle()).isEqualTo("João e Maria");
		assertThat(savedBook.getAutor()).isEqualTo("João");

	}

}
