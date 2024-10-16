package br.encibra.desafio.domain.services;

import br.encibra.desafio.domain.entities.Password;
import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.util.AESUtil;
import br.encibra.desafio.exceptions.PasswordLimitExceededException;
import br.encibra.desafio.infra.mapper.PasswordHttpMapper;
import br.encibra.desafio.infra.request.PasswordHttpRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PasswordService {

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

    private void validatePasswordLimit (List<Password> passwords, Integer limit) {
        if (passwords.size() >= limit) {
            throw new PasswordLimitExceededException("O usuário já possui o máximo de " + limit + " senhas cadastradas.");
        }
    }

}
