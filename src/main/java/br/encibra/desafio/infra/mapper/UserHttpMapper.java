package br.encibra.desafio.infra.mapper;

import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.infra.request.UserHttpRequest;
import br.encibra.desafio.infra.response.UserHttpResponse;
import br.encibra.desafio.infra.response.UserHttpResponseWithList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHttpMapper {

    private final ModelMapper map;

    public User toDomain (UserHttpRequest request){
        return map.map(request, User.class);
    }

    public UserHttpResponse toResponse (User user){
        return map.map(user, UserHttpResponse.class);
    }

    public UserHttpResponseWithList toListResponse (User user) {
        return map.map(user, UserHttpResponseWithList.class);
    }

}
