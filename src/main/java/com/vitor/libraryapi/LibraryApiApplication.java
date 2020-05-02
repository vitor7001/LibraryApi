package com.vitor.libraryapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class LibraryApiApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Scheduled(cron = "0 17 20 1/1 * ?")
	public void testeAgendamentoTarefas() {
		System.out.println("Sou uma tarefa agendada!");
	}

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
