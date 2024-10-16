package br.encibra.desafio.domain.repositories;

import br.encibra.desafio.domain.entities.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {

}
