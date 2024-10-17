package br.encibra.desafio;

import java.util.ArrayList;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.services.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Encibra Desafio Técnico",
				version = "1.0",
				description = "API Documentation",
				contact = @Contact(name = "Leonardo Gomes", email = "contato.elwgomes@gmail.com")
		)
)
public class DesafioApplication {

	public static void main(String[] args) {
		log.info("Starting application");
		SpringApplication.run(DesafioApplication.class, args);
		log.info("Application started");
	}

	@Autowired
	private UserService service;

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			service.save(new User(1L, "Leonardo", new ArrayList<>()));
		};
	}

}
