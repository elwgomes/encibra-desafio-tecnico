package br.encibra.desafio.domain.services;

import org.springframework.stereotype.Service;

import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.repositories.UserRepository;
import br.encibra.desafio.exceptions.PasswordDecryptionException;
import br.encibra.desafio.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final EncryptionService encryptionService;

	public User save(User request) {
		log.info("Saving user: {}", request.getId());
		User savedUser = userRepository.save(request);
		log.info("User saved successfully: {}", savedUser.getId());
		return savedUser;
	}

	public User findById(Long id) {
		log.info("Finding user with ID: {}", id);
		User user = userRepository.findById(id)
				.orElseThrow(() -> {
					log.error("User not found with ID: {}", id);
					return new ResourceNotFoundException(id);
				});
		log.info("User found: {}", user.getId());
		return user;
	}

	public User findByIdWithDecrypt(Long id) throws Exception {
		log.info("Finding and decrypting user with ID: {}", id);
		User user = userRepository.findById(id)
				.orElseThrow(() -> {
					log.error("User not found with ID: {}", id);
					return new ResourceNotFoundException(id);
				});
		decryptUserPasswords(user);
		log.info("User found and passwords decrypted for user ID: {}", user.getId());
		return user;
	}

	private void decryptUserPasswords(User user) {
		if (user.getPasswords() != null && !user.getPasswords().isEmpty()) {
			user.getPasswords().forEach(password -> {
				try {
					String decryptedValue = encryptionService.decrypt(password.getValor());
					password.setValor(decryptedValue);
					log.info("Password decrypted for user ID: {}, password value: {}", user.getId(), decryptedValue);
				} catch (Exception e) {
					log.error("Error decrypting password with value: {} for user ID: {}", password.getValor(),
							user.getId(), e);
					throw new PasswordDecryptionException("Error decrypting password with value " + password.getValor(),
							e);
				}
			});
		} else {
			log.warn("No passwords to decrypt for user ID: {}", user.getId());
		}
	}
}
