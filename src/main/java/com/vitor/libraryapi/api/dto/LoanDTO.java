package com.vitor.libraryapi.api.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {

	private Long id;
	@NotBlank
	private String isbn;
	@NotBlank
	private String customer;
	@NotBlank
	private String email;
	private BookDTO book;

}
