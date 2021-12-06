package br.com.gew.domain.repositories;

import br.com.gew.domain.entities.LogHoras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogHorasRepository extends JpaRepository<LogHoras, Long> {
}
