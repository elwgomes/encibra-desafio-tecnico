package br.encibra.desafio.domain.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.repositories.UserRepository;
import br.encibra.desafio.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final EncryptionService encryptionService;

	public User save(User request) {
		return userRepository.save(request);
	}

	public User findById(Long id) throws Exception {
		Optional<User> obj = userRepository.findById(id);
		User user = obj.orElseThrow(() -> new ResourceNotFoundException(id));

		// if (user.getPasswords() != null) {
		// List<Password> passwords = user.getPasswords();
		// for (Password password : passwords) {
		// }
		// }

		return user;
	}

}
