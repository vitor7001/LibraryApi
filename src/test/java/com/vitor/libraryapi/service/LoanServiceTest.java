package com.vitor.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.vitor.libraryapi.exception.BusinessException;
import com.vitor.libraryapi.model.entity.Book;
import com.vitor.libraryapi.model.entity.Loan;
import com.vitor.libraryapi.model.repository.LoanRepository;
import com.vitor.libraryapi.service.implementacao.LoanServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

	@MockBean
	LoanRepository repository;

	LoanService service;

	@BeforeEach
	public void setUp() {
		this.service = new LoanServiceImpl(repository);
	}

	@Test
	@DisplayName("Deve salvar um empréstimo.")
	public void saveLoanTest() {

		Book book = Book.builder().id(1l).build();
		String customer = "José";
		Loan loan = Loan.builder().customer(customer).book(book).loanDate(LocalDate.now()).build();

		Loan savedLoan = Loan.builder().id(1l).loanDate(LocalDate.now()).customer(customer).book(book).build();

		when(repository.existsByBookAndNotReturned(book)).thenReturn(false);
		when(repository.save(loan)).thenReturn(savedLoan);

		Loan save = service.save(loan);

		assertThat(save.getId()).isEqualTo(savedLoan.getId());
		assertThat(save.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
		assertThat(save.getCustomer()).isEqualTo(savedLoan.getCustomer());
		assertThat(save.getLoanDate()).isEqualTo(savedLoan.getLoanDate());

	}

	@Test
	@DisplayName("Deve lançar erro de negócio ao salvar empréstimo com o livro já emprestado.")
	public void LoanedBookSaveTest() {

		Book book = Book.builder().id(1l).build();
		String customer = "José";
		Loan loan = Loan.builder().customer(customer).book(book).loanDate(LocalDate.now()).build();

		when(repository.existsByBookAndNotReturned(book)).thenReturn(true);

		Throwable erro = catchThrowable(() -> service.save(loan));

		assertThat(erro).isInstanceOf(BusinessException.class).hasMessage("Book already loaned!");

		verify(repository, never()).save(loan);
	}

}
