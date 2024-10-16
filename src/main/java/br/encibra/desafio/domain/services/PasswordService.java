package br.encibra.desafio.domain.services;

import br.encibra.desafio.domain.entities.Password;
import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.repositories.PasswordRepository;
import br.encibra.desafio.exceptions.DatabaseException;
import br.encibra.desafio.exceptions.PasswordLimitExceededException;
import br.encibra.desafio.exceptions.ResourceNotFoundException;
import br.encibra.desafio.infra.mapper.PasswordHttpMapper;
import br.encibra.desafio.infra.request.PasswordHttpRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordRepository passwordRepository;
    private final PasswordHttpMapper mapper;
    private final UserService userService;
    private final EncryptionService encryptionService;

    @Transactional
    public Password addPasswordToUser(PasswordHttpRequest request) throws Exception {
        User user = userService.findById(request.getUserId());

        validatePasswordLimit(user.getPasswords(), 20);

        String rawPassword = request.getValor();
        String hashedPassword = encryptionService.encrypt(rawPassword);
        Password password = mapper.toDomain(request, user);
        password.setValor(hashedPassword);
        user.getPasswords().add(password);
        userService.save(user);

        return password;
    }

    @Transactional
    public Password update (Long id, PasswordHttpRequest obj) {
        Password entity = passwordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        updateEntity(entity, obj);
        return passwordRepository.save(entity);
    }

    @Transactional
    public void delete (Long id) {
        try {
            passwordRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException exception) {
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
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            entity.setValor(hashedPassword);
        });
    }

    private void validatePasswordLimit (List<Password> passwords, Integer limit) {
        if (passwords.size() >= limit) {
            throw new PasswordLimitExceededException("O usuário já possui o máximo de " + limit + " senhas cadastradas.");
        }
    }

}
