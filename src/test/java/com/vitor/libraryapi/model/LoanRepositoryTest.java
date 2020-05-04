package com.vitor.libraryapi.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.vitor.libraryapi.model.entity.Book;
import com.vitor.libraryapi.model.entity.Loan;
import com.vitor.libraryapi.model.repository.LoanRepository;

import static com.vitor.libraryapi.model.BookRepositoryTest.createBook;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

	@Autowired
	LoanRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	@DisplayName("Deve verificar se existe empréstimo não devolvido para o livro")
	public void existsByBookAndNotReturnedTest() {
		// cenário
		Loan loan = createAndPersistLoan(LocalDate.now());

		Book book = loan.getBook();

		// execução
		boolean exists = repository.existsByBookAndNotReturned(book);

		assertThat(exists).isTrue();

	}

	@Test
	@DisplayName("Deve buscar um empréstimo pelo  Isbn do livro ou pelo customer")
	public void findByBookIsbnOrCustomerTest() {

		Loan loan = createAndPersistLoan(LocalDate.now());

		Page<Loan> result = repository.findByBookIsbnOrCustomer("123", "José", PageRequest.of(0, 10));

		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent()).contains(loan);
		assertThat(result.getPageable().getPageSize()).isEqualTo(10);
		assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
		assertThat(result.getTotalElements()).isEqualTo(1);
	}

	@Test
	@DisplayName("Deve obter empréstimo em que a data do empréstimo for menor ou igual a tres dias atras e nao retornados")
	public void findByLoanDateLessThanAndNotReturnedTest() {

		Loan loan = createAndPersistLoan(LocalDate.now().minusDays(5));

		List<Loan> result = repository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

		assertThat(result).hasSize(1).contains(loan);
	}

	@Test
	@DisplayName("Deve retornar vazio quando não houverem empréstimos atrasados")
	public void notfindByLoanDateLessThanAndNotReturnedTest() {

		createAndPersistLoan(LocalDate.now());

		List<Loan> result = repository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

		assertThat(result).isEmpty();
	}

	private Loan createAndPersistLoan(LocalDate date) {
		Book book = createBook("123");

		entityManager.persist(book);

		Loan loan = Loan.builder().book(book).customer("José").loanDate(date).build();

		entityManager.persist(loan);
		return loan;
	}

}
