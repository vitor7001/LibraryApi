package com.vitor.libraryapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryApiApplication {
	/*
	 * @Autowired private EmailService emailService;
	 */
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	/*
	 * @Bean public CommandLineRunner runner() {
	 * 
	 * return args -> { List<String> emails =
	 * Arrays.asList("libraryapi-ce2836@inbox.mailtrap.io");
	 * 
	 * emailService.sendMails("Testando serviço de emails", emails);
	 * System.out.println("Emails enviados"); };
	 * 
	 * }
	 */
	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
