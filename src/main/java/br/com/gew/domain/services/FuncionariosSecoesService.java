package br.com.gew.domain.services;

import br.com.gew.domain.entities.FuncionarioSecao;
import br.com.gew.domain.exception.EntityNotFoundException;
import br.com.gew.domain.repositories.FuncionariosSecoesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FuncionariosSecoesService {

    private FuncionariosSecoesRepository funcionariosSecoesRepository;

    public FuncionarioSecao buscarPorFuncionario(long funcionarioCracha) {
        return funcionariosSecoesRepository.findByFuncionarioCracha(funcionarioCracha)
                .orElseThrow(() -> new EntityNotFoundException("Funcionario não pertence a nenhuma seção"));
    }

}
