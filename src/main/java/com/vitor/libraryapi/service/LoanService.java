package com.vitor.libraryapi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vitor.libraryapi.api.dto.LoanFilterDTO;
import com.vitor.libraryapi.model.entity.Loan;

public interface LoanService {

	Loan save(Loan loan);

	Optional<Loan> getById(Long id);

	Loan update(Loan loan);

	Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable);

}
