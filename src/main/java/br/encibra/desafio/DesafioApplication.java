package br.encibra.desafio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Encibra Desafio Técnico", version = "1.0", description = "API Documentation", contact = @Contact(name = "Leonardo Gomes", email = "contato.elwgomes@gmail.com")))
public class DesafioApplication {

	public static void main(String[] args) {
		log.info("Starting application");
		SpringApplication.run(DesafioApplication.class, args);
		log.info("Application started");
	}

}
