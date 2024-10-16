package br.encibra.desafio.infra.response;

import br.encibra.desafio.domain.entities.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserHttpResponseWithList extends UserHttpResponse {

    private List<Password> passwords;

}
