package br.encibra.desafio.infra.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserHttpResponse {

    private Long id;
    private String name;

}
