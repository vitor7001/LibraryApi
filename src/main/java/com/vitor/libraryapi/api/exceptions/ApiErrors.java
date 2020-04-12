package com.vitor.libraryapi.api.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.validation.BindingResult;

import com.vitor.libraryapi.exception.BussinesException;

public class ApiErrors {

	private List<String> errors;

	public ApiErrors(BindingResult bindingResult) {

		this.errors = new ArrayList<>();

		bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));

	}

	public ApiErrors(BussinesException ex) {
		this.errors = Arrays.asList(ex.getMessage());

	}

	public List<String> getErros() {
		return this.errors;
	}

}
