package com.vitor.libraryapi.api.resource;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vitor.libraryapi.api.dto.BookDTO;
import com.vitor.libraryapi.api.exceptions.ApiErrors;
import com.vitor.libraryapi.model.entity.Book;
import com.vitor.libraryapi.service.BookService;

import com.vitor.libraryapi.exception.BussinesException;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private BookService service;
	private ModelMapper modelMapper;

	public BookController(BookService service, ModelMapper modelMapper) {
		this.service = service;
		this.modelMapper = modelMapper;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO create(@RequestBody @Valid BookDTO dto) {

		Book entity = modelMapper.map(dto, Book.class);

		entity = service.save(entity);

		return modelMapper.map(entity, BookDTO.class);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleValitionException(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();

		return new ApiErrors(bindingResult);

	}
	
	@ExceptionHandler(BussinesException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleBussinesException(BussinesException ex){
		
		return new ApiErrors(ex);
		
	}
	
	
	

}
