package br.encibra.desafio.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.encibra.desafio.domain.entities.Password;

import java.util.List;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {

    @Query("SELECT p FROM Password p WHERE p.user.id = :userId")
    List<Password> findAllByUserId(@Param("userId") Long userId);

}
