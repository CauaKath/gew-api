package br.com.gew.domain.services;

import br.com.gew.domain.entities.Secao;
import br.com.gew.domain.exception.EntityNotFoundException;
import br.com.gew.domain.repositories.SecoesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SecaoService {

    private SecoesRepository secoesRepository;

    private FuncionariosSecoesService funcionariosSecoesService;

    public Secao buscarPorFuncionario(long funcionarioCracha) {
        return secoesRepository.findById(
                funcionariosSecoesService.buscarPorFuncionario(funcionarioCracha).getSecao_id()
        ).orElseThrow(() -> new EntityNotFoundException("Seção não encontrada para este funcionário"));
    }

    public Secao buscar(long id) {
        return secoesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seção não encontrada"));
    }

}
