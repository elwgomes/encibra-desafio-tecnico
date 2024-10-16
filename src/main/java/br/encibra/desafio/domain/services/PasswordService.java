package br.encibra.desafio.domain.services;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.encibra.desafio.domain.entities.Password;
import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.repositories.PasswordRepository;
import br.encibra.desafio.exceptions.DatabaseException;
import br.encibra.desafio.exceptions.PasswordLimitExceededException;
import br.encibra.desafio.exceptions.ResourceNotFoundException;
import br.encibra.desafio.infra.mapper.PasswordHttpMapper;
import br.encibra.desafio.infra.request.PasswordHttpRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordService {

	private final PasswordRepository passwordRepository;
	private final PasswordHttpMapper mapper;
	private final UserService userService;
	private final EncryptionService encryptionService;

	@Transactional
	public Password addPasswordToUser(PasswordHttpRequest request) throws Exception {
		log.info("Starting to add password for user with ID: {}", request.getUserId());
		User user = userService.findById(request.getUserId());

		validatePasswordLimit(user.getPasswords(), 20);

		String rawPassword = request.getValor();
		String hashedPassword = encryptionService.encrypt(rawPassword);
		Password password = mapper.toDomain(request, user);
		password.setValor(hashedPassword);
		user.getPasswords().add(password);
		userService.save(user);

		log.info("Password successfully added for user ID: {}", user.getId());
		return password;
	}

	@Transactional
	public Password update(Long id, PasswordHttpRequest obj) {
		log.info("Updating password with ID: {}", id);
		Password entity = passwordRepository.findById(id)
				.orElseThrow(() -> {
					log.error("Password not found with ID: {}", id);
					return new ResourceNotFoundException(id);
				});
		updateEntity(entity, obj);
		Password updatedPassword = passwordRepository.save(entity);
		log.info("Password successfully updated, ID: {}", updatedPassword.getId());
		return passwordRepository.save(entity);
	}

	@Transactional
	public void delete(Long id) {
		log.info("Attempting to delete password with ID: {}", id);
		try {
			passwordRepository.deleteById(id);
			log.info("Password successfully deleted, ID: {}", id);
		} catch (EmptyResultDataAccessException exception) {
			log.error("Error deleting password: password not found with ID: {}", id);
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException exception) {
			log.error("Error deleting password: database integrity violation for ID: {}", id);
			throw new DatabaseException(exception.getMessage());
		}
	}

	private void updateEntity(Password entity, PasswordHttpRequest obj) {
		Optional.ofNullable(obj.getDescription()).ifPresent(entity::setDescription);
		Optional.ofNullable(obj.getTags()).ifPresent(entity::setTags);
		Optional.ofNullable(obj.getValor()).ifPresent(rawPassword -> {
			String hashedPassword;
			try {
				hashedPassword = encryptionService.encrypt(rawPassword);
				entity.setValor(hashedPassword);
				log.info("Password successfully updated for ID: {}", entity.getId());
			} catch (Exception e) {
				log.error("Error encrypting the password for ID: {}", entity.getId(), e);
				throw new RuntimeException(e);
			}
		});
	}

	private void validatePasswordLimit(List<Password> passwords, Integer limit) {
		if (passwords.size() >= limit) {
			log.warn("Password limit exceeded for user: maximum of {} passwords allowed.", limit);
			throw new PasswordLimitExceededException(
					"The user has reached the maximum of " + limit + " passwords allowed.");
		}
	}

}
