package com.vitor.libraryapi.congif;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Bean
	public Docket docket() {

		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.vitor.libraryapi.api.resource"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
		
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
					.title("Library API")
					.description("API para um projeto de bibliotecas para controlar o aluguel de livros")
					.version("1.0")
					.contact(contact()).build();
					
	}

	private Contact contact() {
		return new Contact("Vitor Gomes", "https://github.com/vitor7001/LibraryApi", "vitorgomes7001@gmail.com");
	}
}
