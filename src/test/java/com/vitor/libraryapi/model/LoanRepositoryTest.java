package com.vitor.libraryapi.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.vitor.libraryapi.model.entity.Book;
import com.vitor.libraryapi.model.entity.Loan;
import com.vitor.libraryapi.model.repository.LoanRepository;

import static com.vitor.libraryapi.model.BookRepositoryTest.createBook;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

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
		Book book = createBook("123");

		entityManager.persist(book);

		Loan loan = Loan.builder().book(book).customer("José").loanDate(LocalDate.now()).build();

		entityManager.persist(loan);

		// execução
		boolean exists = repository.existsByBookAndNotReturned(book);

		assertThat(exists).isTrue();

	}

}
