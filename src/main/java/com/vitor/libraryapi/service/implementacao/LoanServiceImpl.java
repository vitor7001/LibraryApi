package com.vitor.libraryapi.service.implementacao;

import com.vitor.libraryapi.model.entity.Loan;
import com.vitor.libraryapi.model.repository.LoanRepository;
import com.vitor.libraryapi.service.LoanService;

public class LoanServiceImpl implements LoanService {

	private LoanRepository repository;

	public LoanServiceImpl(LoanRepository repository) {

		this.repository = repository;

	}

	@Override
	public Loan save(Loan loan) {
		return repository.save(loan);
	}

}
