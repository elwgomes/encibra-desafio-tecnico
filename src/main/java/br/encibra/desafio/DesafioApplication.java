package br.encibra.desafio;

import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@Slf4j
@SpringBootApplication
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
