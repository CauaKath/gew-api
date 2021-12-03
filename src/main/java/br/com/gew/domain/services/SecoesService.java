package br.com.gew.domain.services;

import br.com.gew.domain.entities.Secao;
import br.com.gew.domain.exception.EntityNotFoundException;
import br.com.gew.domain.exception.ExceptionTratement;
import br.com.gew.domain.repositories.SecoesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SecoesService {

    private SecoesRepository secoesRepository;

    private FuncionariosSecoesService funcionariosSecoesService;

    public Secao buscar(long id) throws ExceptionTratement {
        try {
            return secoesRepository.findById(id).get();
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public Secao buscarPorFuncionario(long funcionarioCracha) throws ExceptionTratement {
        try {
            return secoesRepository.findById(
                    funcionariosSecoesService.buscarPorFuncionario(funcionarioCracha).getSecao_id()
            ).get();
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public Secao buscarPorNome(String nome) throws ExceptionTratement {
        try {
            return secoesRepository.findByNome(nome).get();
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

}
