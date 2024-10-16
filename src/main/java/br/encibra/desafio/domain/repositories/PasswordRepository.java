package br.encibra.desafio.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.encibra.desafio.domain.entities.Password;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {
}
