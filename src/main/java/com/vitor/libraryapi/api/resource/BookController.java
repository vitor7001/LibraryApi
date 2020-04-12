package com.vitor.libraryapi.api.resource;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vitor.libraryapi.api.dto.BookDTO;
import com.vitor.libraryapi.api.exceptions.ApiErrors;
import com.vitor.libraryapi.exception.BusinessException;
import com.vitor.libraryapi.model.entity.Book;
import com.vitor.libraryapi.service.BookService;

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
	
	
	@GetMapping("{id}")
	public BookDTO get(@PathVariable Long id) {
		
		Book book = service.getById(id).get();
		
		return modelMapper.map(book, BookDTO.class);
	}
	
	
	

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleValitionException(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();

		return new ApiErrors(bindingResult);

	}
	
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleBussinesException(BusinessException ex){
		
		return new ApiErrors(ex);
		
	}
	
	
	

}
