package br.encibra.desafio.domain.services;

import br.encibra.desafio.domain.entities.Password;
import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.repositories.PasswordRepository;
import br.encibra.desafio.infra.mapper.PasswordHttpMapper;
import br.encibra.desafio.infra.request.PasswordHttpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordRepository passwordRepository;
    private final PasswordHttpMapper passwordHttpMapper;
    private final UserService userService;

    public Password create(PasswordHttpRequest request) {
        User user = userService.findById(request.getUserId());
        Password password = passwordHttpMapper.toDomain(request, user);
        return passwordRepository.save(password);
    }

}
