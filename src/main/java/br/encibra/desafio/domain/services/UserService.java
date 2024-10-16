package br.encibra.desafio.domain.services;

import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.repositories.UserRepository;
import br.encibra.desafio.exceptions.ResourceNotFoundException;
import br.encibra.desafio.infra.mapper.UserHttpMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserHttpMapper mapper;

    public User save(User request) {
        return userRepository.save(request);
    }

    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new ResourceNotFoundException(id));
    }

}
