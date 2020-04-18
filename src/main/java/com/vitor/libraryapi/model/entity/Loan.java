package com.vitor.libraryapi.model.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

	private Long id;
	
	private String customer;
	
	private Book book;
	
	private LocalDate loanDate;
	
	private Boolean returned;
	
}
