package br.encibra.desafio.infra.response;

import br.encibra.desafio.domain.entities.Password;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserHttpResponseWithList {
    private Long id;
    private String name;
    private List<Password> passwords;
}
