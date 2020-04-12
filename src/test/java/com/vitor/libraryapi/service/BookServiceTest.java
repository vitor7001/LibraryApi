package com.vitor.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.vitor.libraryapi.exception.BusinessException;
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
		Book book = createBook();

		Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);

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

	@Test
	@DisplayName("Deve lançar um erro de negócio ao tentar salvar um livro com isbn já cadastrado")
	public void shouldNotSaveBookWithDuplicatedIsbn() {
		// cenario
		Book book = createBook();

		Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

		// execucao
		Throwable ex = Assertions.catchThrowable(() -> service.save(book));

		// verificacoes
		assertThat(ex).isInstanceOf(BusinessException.class).hasMessage("Isbn já cadastrado.");

		Mockito.verify(repository, Mockito.never()).save(book);

	}

	private Book createBook() {
		return Book.builder().isbn("123").autor("João").title("João e Maria").build();

	}

}
