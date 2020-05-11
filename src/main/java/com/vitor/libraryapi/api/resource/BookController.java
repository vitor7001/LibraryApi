package com.vitor.libraryapi.api.resource;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.vitor.libraryapi.api.dto.BookDTO;
import com.vitor.libraryapi.api.dto.LoanDTO;
import com.vitor.libraryapi.model.entity.Book;
import com.vitor.libraryapi.model.entity.Loan;
import com.vitor.libraryapi.service.BookService;
import com.vitor.libraryapi.service.LoanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Api
public class BookController {

	private final BookService service;
	private final LoanService loanService;
	private final ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation("Create a Book")
	public BookDTO create(@RequestBody @Valid BookDTO dto) {

		Book entity = modelMapper.map(dto, Book.class);

		entity = service.save(entity);

		return modelMapper.map(entity, BookDTO.class);
	}

	@GetMapping("{id}")
	@ApiOperation("Obtains a book details by id")
	public BookDTO get(@PathVariable Long id) {

		return service.getById(id).map(book -> modelMapper.map(book, BookDTO.class))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation("Deletes a Book by id")
	@ApiResponses({ @ApiResponse(code = 204, message = "Book succesfully deleted") })
	public void delete(@PathVariable Long id) {

		Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		service.delete(book);

	}

	@PutMapping("{id}")
	@ApiOperation("Updates a Book by id")
	public BookDTO update(@PathVariable Long id, BookDTO dto) {

		Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		book.setAutor(dto.getAutor());
		book.setTitle(dto.getTitle());

		book = service.update(book);

		return modelMapper.map(book, BookDTO.class);
	}

	@GetMapping
	@ApiOperation("Find Books by params")
	public Page<BookDTO> find(BookDTO dto, Pageable pageRequest) {

		Book filtro = modelMapper.map(dto, Book.class);

		Page<Book> resultado = service.find(filtro, pageRequest);

		List<BookDTO> list = resultado.getContent().stream().map(entity -> modelMapper.map(entity, BookDTO.class))
				.collect(Collectors.toList());

		return new PageImpl<BookDTO>(list, pageRequest, resultado.getTotalElements());
	}

	@GetMapping("{id}/loans")
	@ApiOperation("Obtains loans about a Book by id")
	public Page<LoanDTO> loansByBook(@PathVariable Long idLivro, Pageable page) {

		Book book = service.getById(idLivro).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		Page<Loan> result = loanService.getLoansByBook(book, page);

		List<LoanDTO> list = result.getContent().stream().map(loan -> {

			Book loanBook = loan.getBook();

			BookDTO bookDTO = modelMapper.map(loanBook, BookDTO.class);

			LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);

			loanDTO.setBook(bookDTO);

			return loanDTO;

		}).collect(Collectors.toList());

		return new PageImpl<LoanDTO>(list, page, result.getTotalElements());

	}

}
