package com.vitor.libraryapi.api.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
	private Long id;

	@NotEmpty
	private String title;
	@NotEmpty
	private String autor;
	@NotEmpty
	private String isbn;

}
