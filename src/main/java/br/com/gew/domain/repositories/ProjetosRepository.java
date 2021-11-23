package br.com.gew.domain.repositories;

import br.com.gew.domain.entities.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjetosRepository extends JpaRepository<Projeto, Long> {

    Optional<Projeto> findByNumeroDoProjeto(long numeroDoProjeto);

    Optional<Projeto> findByTitulo(String titulo);

}
