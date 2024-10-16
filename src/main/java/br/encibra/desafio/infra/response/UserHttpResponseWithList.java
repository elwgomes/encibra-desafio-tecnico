package br.encibra.desafio.infra.response;

import java.util.List;

import br.encibra.desafio.domain.entities.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserHttpResponseWithList {
	private Long id;
	private String name;
	private List<Password> passwords;
}
