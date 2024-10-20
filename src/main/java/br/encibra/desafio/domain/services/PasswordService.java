package br.encibra.desafio.domain.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordService {

	private final PasswordRepository passwordRepository;
	private final PasswordHttpMapper mapper;
	private final UserService userService;
	private final EncryptionService encryptionService;
	private final TokenService tokenService;

	private static final int PASSWORD_LIMIT = 20;

	@Transactional
	public Password addPasswordToUser(PasswordHttpRequest request, String token) {
		Long userId = validateTokenAndGetUserId(token);
		User user = userService.findById(userId);

		validatePasswordLimit(user.getPasswords(), PASSWORD_LIMIT);

		String hashedPassword = encryptPassword(request.getValor());
		Password password = mapper.toDomain(request, user);
		password.setValor(hashedPassword);
		user.getPasswords().add(password);
		userService.save(user);

		Password savedPassword = user.getPasswords()
				.stream()
				.filter(p -> p.getValor().equals(hashedPassword))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Erro ao salvar a senha"));

		log.info("Password successfully added for user ID: {}", user.getId());
		return savedPassword;
	}

	@Transactional
	public Password update(Long id, PasswordHttpRequest obj, String token) {
		Long userId = validateTokenAndGetUserId(token);

		Password entity = findPasswordByIdAndUserId(id, userId);
		updateEntity(entity, obj);

		Password updatedPassword = passwordRepository.save(entity);
		log.info("Password successfully updated, ID: {}", updatedPassword.getId());
		return updatedPassword;
	}

	@Transactional
	public void delete(Long id, String token) {
		Long userId = validateTokenAndGetUserId(token);
		Password password = findPasswordByIdAndUserId(id, userId);

		try {
			passwordRepository.deleteById(id);
			log.info("Password successfully deleted, ID: {}", id);
		} catch (DataIntegrityViolationException exception) {
			log.error("Error deleting password: database integrity violation for ID: {}", id);
			throw new DatabaseException(exception.getMessage());
		} catch (EmptyResultDataAccessException exception) {
			log.error("Error deleting password: password not found with ID: {}", id);
			throw new ResourceNotFoundException(id);
		}
	}

	public List<Password> findAll(String token) {
		Long userId = validateTokenAndGetUserId(token);
		log.info("Starting to find all passwords for user ID: {}", userId);
		List<Password> list = passwordRepository.findAllByUserId(userId);

		List<Password> decryptedList = list.stream()
				.map(password -> {
					String decryptedValue = decryptPassword(password.getValor());
					password.setValor(decryptedValue);
					return password;
				})
				.collect(Collectors.toList());

		log.info("Passwords found for user ID: {}", userId);
		return decryptedList;
	}

	private void updateEntity(Password entity, PasswordHttpRequest obj) {
		Optional.ofNullable(obj.getDescription()).ifPresent(entity::setDescription);
		Optional.ofNullable(obj.getTags()).ifPresent(entity::setTags);
		Optional.ofNullable(obj.getValor()).ifPresent(rawPassword -> {
			String hashedPassword = encryptPassword(rawPassword);
			entity.setValor(hashedPassword);
		});
	}

	private String decryptPassword(String encryptedPassword) {
		try {
			return encryptionService.decrypt(encryptedPassword);
		} catch (Exception e) {
			log.error("Error decrypting the password", e);
			throw new RuntimeException(e);
		}
	}

	private String encryptPassword(String rawPassword) {
		try {
			return encryptionService.encrypt(rawPassword);
		} catch (Exception e) {
			log.error("Error encrypting the password", e);
			throw new RuntimeException(e);
		}
	}

	private Password findPasswordByIdAndUserId(Long passwordId, Long userId) {
		return passwordRepository.findById(passwordId).filter(p -> p.getUser().getId().equals(userId))
				.orElseThrow(() -> {
					log.error("Password not found or does not belong to user with ID: {}", userId);
					return new ResourceNotFoundException(passwordId);
				});
	}

	private Long validateTokenAndGetUserId(String token) {
		Long userId = Long.valueOf(tokenService.validateJwtToken(token));
		log.info("User ID from token: {}", userId);
		return userId;
	}

	public void validatePasswordLimit(List<Password> passwords, Integer limit) {
		if (passwords.size() >= limit) {
			log.warn("Password limit exceeded for user: maximum of {} passwords allowed.", limit);
			throw new PasswordLimitExceededException(
					"The user has reached the maximum of " + limit + " passwords allowed.");
		}
	}
}
