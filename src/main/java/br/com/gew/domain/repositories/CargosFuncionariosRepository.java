package br.com.gew.domain.repositories;

import br.com.gew.domain.entities.CargoFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargosFuncionariosRepository extends JpaRepository<CargoFuncionario, Long> {
}
