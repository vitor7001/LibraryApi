package com.vitor.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.vitor.libraryapi.model.entity.Book;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

	BookService service;

	@Test
	@DisplayName("Deve salvar um livro.")
	public void savebookTest() {
		// cenário
		Book book = Book.builder().isbn("123").autor("João").title("João e Maria").build();

		// execução
		Book savedBook = service.save(book);

		// verificação

		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getIsbn()).isEqualTo("123");
		assertThat(savedBook.getTitle()).isEqualTo("João e Maria");
		assertThat(savedBook.getAutor()).isEqualTo("João");

	}

}
