package br.encibra.desafio.infra.mapper;

import br.encibra.desafio.domain.entities.Password;
import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.infra.request.PasswordHttpRequest;
import br.encibra.desafio.infra.response.PasswordHttpResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordHttpMapper {

    private final ModelMapper map;

    public Password toDomain (PasswordHttpRequest request, User user) {
        return new Password(
                null,
                request.getDescription(),
                request.getTags(),
                request.getValor(),
                user
        );
    }

    public PasswordHttpResponse toResponse (Password password) {
        return map.map(password, PasswordHttpResponse.class);
    }

}
