package br.encibra.desafio.infra.response;

import br.encibra.desafio.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordHttpResponse {
    private String description;
    private String tags;
    private String valor;
}
