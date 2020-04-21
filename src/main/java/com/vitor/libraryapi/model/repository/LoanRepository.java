package com.vitor.libraryapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitor.libraryapi.model.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>{

}
