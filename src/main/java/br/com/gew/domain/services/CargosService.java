package br.com.gew.domain.services;

import br.com.gew.domain.entities.Cargo;
import br.com.gew.domain.exception.EntityNotFoundException;
import br.com.gew.domain.exception.ExceptionTratement;
import br.com.gew.domain.repositories.CargosRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CargosService {

    private CargosRepository cargosRepository;

    public Cargo buscar(long id) {
        return cargosRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cargo não encontrado"));
    }

    public Cargo buscarPorNome(String nome) {
        return cargosRepository
                .findByNome(nome)
                .orElseThrow(() -> new EntityNotFoundException("Cargo com esse nome não encontrado"));
    }

}
