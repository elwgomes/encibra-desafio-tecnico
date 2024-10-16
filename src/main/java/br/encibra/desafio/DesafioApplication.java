package br.encibra.desafio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DesafioApplication {

	public static void main(String[] args) {
		log.info("Starting application");
		SpringApplication.run(DesafioApplication.class, args);
		log.info("Application started");
	}

}
