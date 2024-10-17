package br.encibra.desafio.infra.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordHttpResponse {
	private Long id;
	private String description;
	private String tags;
	private String valor;
}
