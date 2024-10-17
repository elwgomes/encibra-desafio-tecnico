package br.encibra.desafio.config;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.encibra.desafio.domain.entities.Password;
import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.services.UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

	private final UserService userService;

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			User userA = new User(1L, "Pessoa A", new ArrayList<>());
			User userB = new User(2L, "Pessoa B", new ArrayList<>());
			User userC = new User(3L, "Pessoa C", new ArrayList<>());
			User userD = new User(4L, "Pessoa D", new ArrayList<>());

			Password password1 = new Password(null, "Email account", "email, personal", "p@ssword123", userA);
			Password password2 = new Password(null, "Bank account", "banking, personal", "bankP@ss987", userA);
			Password password3 = new Password(null, "Work VPN", "work, vpn", "workV@pn321", userB);
			Password password4 = new Password(null, "Cloud Service", "cloud, backup", "cl0udS3rv", userC);
			Password password5 = new Password(null, "Social Media", "social, personal", "s0cialP@ss", userD);

			userA.getPasswords().add(password1);
			userA.getPasswords().add(password2);
			userB.getPasswords().add(password3);
			userC.getPasswords().add(password4);
			userD.getPasswords().add(password5);

			userService.save(userA);
			userService.save(userB);
			userService.save(userC);
			userService.save(userD);
		};
	}

}
