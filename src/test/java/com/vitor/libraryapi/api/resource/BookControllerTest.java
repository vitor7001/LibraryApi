package com.vitor.libraryapi.api.resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitor.libraryapi.api.dto.BookDTO;
import com.vitor.libraryapi.exception.BusinessException;
import com.vitor.libraryapi.model.entity.Book;
import com.vitor.libraryapi.service.BookService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

	static String BOOK_API = "/api/books";

	@Autowired
	MockMvc mvc;

	@MockBean
	BookService service;

	@Test
	@DisplayName("Deve criar um livro com sucesso.")
	public void createBookTest() throws Exception {

		BookDTO dto = createNewBook();

		Book savedBook = Book.builder().id(1l).autor("Autor").title("Meu Livro").isbn("123321").build();

		BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);

		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		mvc.perform(request).andExpect(status().isCreated()).andExpect(jsonPath("id").isNotEmpty())
				.andExpect(jsonPath("title").value(dto.getTitle())).andExpect(jsonPath("autor").value(dto.getAutor()))
				.andExpect(jsonPath("isbn").value(dto.getIsbn()));

	}

	@Test
	@DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação do livro.")
	public void createInvalidBookTest() throws Exception {
		String json = new ObjectMapper().writeValueAsString(new BookDTO());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		mvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("erros", hasSize(3)));
	}

	@Test
	@DisplayName("Deve lançar erro ao tentar cadastrar um livro com isbn já cadastrado por outro.")
	public void dontCreateBookWithDuplicatedIsbn() throws Exception {

		BookDTO dto = createNewBook();
		String json = new ObjectMapper().writeValueAsString(dto);

		String mensagemDeErro = "Isbn já cadastrado.";

		BDDMockito.given(service.save(Mockito.any(Book.class))).willThrow(new BusinessException(mensagemDeErro));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		mvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("erros", hasSize(1)))
				.andExpect(jsonPath("erros[0]").value(mensagemDeErro));

	}

	@Test
	@DisplayName("Deve obter informações de um livro.")
	public void getBookDetailsTest() throws Exception {
		// cenario (given)
		Long id = 1l;

		Book book = Book.builder().id(id).title(createNewBook().getTitle()).autor(createNewBook().getAutor())
				.isbn(createNewBook().getIsbn()).build();

		BDDMockito.given(service.getById(id)).willReturn(Optional.of(book));

		// execução (when)

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat("/" + id))
				.accept(MediaType.APPLICATION_JSON);

		// verificação

		mvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("id").value(id))
				.andExpect(jsonPath("title").value(createNewBook().getTitle()))
				.andExpect(jsonPath("autor").value(createNewBook().getAutor()))
				.andExpect(jsonPath("isbn").value(createNewBook().getIsbn()));

	}

	@Test
	@DisplayName("Deve retornar resource not found quando o livro procurado não existir")
	public void bookNotFoundTest() throws Exception {

		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat("/" + 1))
				.accept(MediaType.APPLICATION_JSON);

		mvc.perform(request).andExpect(status().isNotFound());

	}

	@Test
	@DisplayName("Deve deletar um livro")
	public void deleteBookTest() throws Exception {

		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.of(Book.builder().id(1l).build()));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(BOOK_API.concat("/" + 1));
		mvc.perform(request).andExpect(status().isNoContent());

	}

	@Test
	@DisplayName("Deve retornar resource not found quando não encontrar o livro para deletar")
	public void deleteInexistentBookTest() throws Exception {

		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(BOOK_API.concat("/" + 1));

		mvc.perform(request).andExpect(status().isNotFound());

	}

	@Test
	@DisplayName("Deve atualizar um livro")
	public void updateBookTest() throws Exception {

		Long id = 1l;

		String json = new ObjectMapper().writeValueAsString(createNewBook());

		Book bookUpdate = Book.builder().id(1l).title("Algum titulo").autor("Algum autor").isbn("12").build();

		BDDMockito.given(service.getById(id)).willReturn(Optional.of(bookUpdate));

		Book bookUpdated = Book.builder().id(id).autor("Autor").title("Meu Livro").isbn("12").build();
		BDDMockito.given(service.update(bookUpdate)).willReturn(bookUpdated);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(BOOK_API.concat("/" + 1)).content(json)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").value(id))
				.andExpect(jsonPath("title").value(createNewBook().getTitle()))
				.andExpect(jsonPath("autor").value(createNewBook().getAutor()))
				.andExpect(jsonPath("isbn").value("12"));
		;

	}

	@Test
	@DisplayName("Deve retornar 404 ao tentar atualizar um livro inexistente")
	public void updateInexistentBookTest() throws Exception {

		String json = new ObjectMapper().writeValueAsString(createNewBook());

		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(BOOK_API.concat("/" + 1)).content(json)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

		mvc.perform(request).andExpect(status().isNotFound());

	}

	private BookDTO createNewBook() {
		return BookDTO.builder().autor("Autor").title("Meu Livro").isbn("123321").build();
	}

}
