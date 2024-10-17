package br.encibra.desafio.infra.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordHttpRequest {

	private String description;
	private String tags;
	private String valor;

}
