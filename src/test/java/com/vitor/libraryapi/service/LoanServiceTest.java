package com.vitor.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
		Loan loan = Loan.builder()
				.customer(customer)
				.book(book)
				.loanDate(LocalDate.now())
				.build();
		
		
		Loan savedLoan = Loan.builder().id(1l).loanDate(LocalDate.now()).customer(customer).book(book).build();
		
		when(repository.save(loan)).thenReturn(savedLoan);
		
		
		Loan save = service.save(loan);
		
		
		assertThat(save.getId()).isEqualTo(savedLoan.getId());
		assertThat(save.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
		assertThat(save.getCustomer()).isEqualTo(savedLoan.getCustomer());
		assertThat(save.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
		
		
	}

}
