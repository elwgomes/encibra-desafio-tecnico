package br.encibra.desafio.domain.services;

import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.repositories.UserRepository;
import br.encibra.desafio.infra.mapper.UserHttpMapper;
import br.encibra.desafio.infra.request.UserHttpRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserHttpMapper mapper;

    public UserService(UserRepository userRepository, UserHttpMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public User create(UserHttpRequest request) {
        User user = mapper.toDomain(request);
        return userRepository.save(user);
    }

}
