package com.vitor.libraryapi.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vitor.libraryapi.model.entity.Book;
import com.vitor.libraryapi.model.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {

	@Query(value = " select case when ( count(loan.id) > 0 ) then true else false end "
			+ " from Loan as loan where loan.book = :book and ( loan.returned is null or loan.returned is false ) ")
	boolean existsByBookAndNotReturned(@Param("book") Book book);

	@Query(value = " select loan from Loan as loan join loan.book as book where book.isbn = :isbn"
			+ " or loan.customer = :customer ")
	Page<Loan> findByBookIsbnOrCustomer(@Param("isbn") String isbn, @Param("customer") String customer,
			Pageable request);

	Page<Loan> findByBook(Book book, Pageable page);

}
