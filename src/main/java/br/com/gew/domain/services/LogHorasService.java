package br.com.gew.domain.services;

import br.com.gew.domain.entities.LogHoras;
import br.com.gew.domain.exception.ExceptionTratement;
import br.com.gew.domain.repositories.LogHorasRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class LogHorasService {

    private LogHorasRepository logHorasRepository;

    @Transactional
    public LogHoras cadastrar(LogHoras logHoras) throws ExceptionTratement {
        try {
            return logHorasRepository.save(logHoras);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public Optional<LogHoras> buscar(long id) throws ExceptionTratement {
        try {
            return logHorasRepository.findById(id);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

}
