package com.vitor.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

	@Test
	@DisplayName("Deve obter um livro por id.")
	public void getByIdTest() {
		Long id = 1l;

		Book book = createBook();
		book.setId(id);
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

		Optional<Book> foundBook = service.getById(id);

		assertThat(foundBook.isPresent()).isTrue();
		assertThat(foundBook.get().getId()).isEqualTo(id);
		assertThat(foundBook.get().getAutor()).isEqualTo(book.getAutor());
		assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
		assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());

	}

	@Test
	@DisplayName("Deve retornar vazio ao obter um livro por id quando ele não existe na base.")
	public void bookNotFoundByIdTest() {
		Long id = 1l;

		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

		Optional<Book> book = service.getById(id);

		assertThat(book.isPresent()).isFalse();

	}

	@Test
	@DisplayName("Deve deletar um livro")
	public void deleteBookTest() {
		Book book = Book.builder().id(1l).build();

		org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.delete(book));

		Mockito.verify(repository, Mockito.times(1)).delete(book);
	}

	@Test
	@DisplayName("Deve ocorrer erro ao tentar deletar um livro inexistente")
	public void deleteInvalidBookTest() {
		Book book = new Book();

		org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));

		Mockito.verify(repository, Mockito.never()).delete(book);
	}

	@Test
	@DisplayName("Deve atualizar um livro.")
	public void updateBook() {
		Long id = 1l;
		Book actualBook = Book.builder().id(id).build();

		Book updateBook = createBook();
		updateBook.setId(id);

		Mockito.when(repository.save(actualBook)).thenReturn(updateBook);

		Book updateddBook = service.update(actualBook);

		assertThat(updateddBook.getId()).isEqualTo(updateBook.getId());
		assertThat(updateddBook.getAutor()).isEqualTo(updateBook.getAutor());
		assertThat(updateddBook.getTitle()).isEqualTo(updateBook.getTitle());
		assertThat(updateddBook.getIsbn()).isEqualTo(updateBook.getIsbn());
	}

	@Test
	@DisplayName("Deve ocorrer erro ao tentar atualizar um livro inexistente")
	public void updateInvalidBookTest() {
		Book book = new Book();

		org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));

		Mockito.verify(repository, Mockito.never()).save(book);
	}

	private Book createBook() {
		return Book.builder().isbn("123").autor("João").title("João e Maria").build();

	}

	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Deve filtrar livros pelas propriedades.")
	public void findBookTest() throws Exception {

		Book book = createBook();

		PageRequest pageRequest = PageRequest.of(0, 10);
		List<Book> lista = Arrays.asList(book);
		Page<Book> page = new PageImpl<Book>(lista, pageRequest, 1);

		Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);

		Page<Book> result = service.find(book, pageRequest);

		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent()).isEqualTo(lista);
		assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
		assertThat(result.getPageable().getPageSize()).isEqualTo(10);

	}

}
