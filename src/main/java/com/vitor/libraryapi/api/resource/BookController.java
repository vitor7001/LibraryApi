package com.vitor.libraryapi.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vitor.libraryapi.api.dto.BookDTO;

@RestController
@RequestMapping("/api/books")
public class BookController {

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO create() {

		BookDTO dto = new BookDTO();

		dto.setAutor("Autor");
		dto.setId(11l);
		dto.setIsbn("123321");
		dto.setTitle("Meu Livro");
		return dto;
	}

}
