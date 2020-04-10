package com.vitor.libraryapi.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vitor.libraryapi.api.dto.BookDTO;
import com.vitor.libraryapi.model.entity.Book;
import com.vitor.libraryapi.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private BookService service;

	public BookController(BookService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO create(@RequestBody BookDTO dto) {
		Book entity = Book.builder()
				.autor(dto.getAutor())
				.title(dto.getTitle())
				.isbn(dto.getIsbn())
				.build();
		
		entity = service.save(entity);
		
		return BookDTO.builder()
				.id(entity.getId())
				.autor(entity.getAutor())
				.title(entity.getTitle())
				.isbn(entity.getIsbn())
				.build();
	}

}
