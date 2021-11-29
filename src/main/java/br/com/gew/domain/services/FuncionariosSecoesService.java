package br.com.gew.domain.services;

import br.com.gew.domain.entities.FuncionarioSecao;
import br.com.gew.domain.exception.EntityNotFoundException;
import br.com.gew.domain.exception.ExceptionTratement;
import br.com.gew.domain.repositories.FuncionariosSecoesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class FuncionariosSecoesService {

    private FuncionariosSecoesRepository funcionariosSecoesRepository;

    @Transactional
    public FuncionarioSecao cadastrar(FuncionarioSecao funcionarioSecao) throws Exception {
        try {
            return funcionariosSecoesRepository.save(funcionarioSecao);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public FuncionarioSecao buscarPorFuncionario(long funcionarioCracha) {
        return funcionariosSecoesRepository.findByFuncionarioCracha(funcionarioCracha)
                .orElseThrow(() -> new EntityNotFoundException("Funcionario não pertence a nenhuma seção"));
    }

    @Transactional
    public FuncionarioSecao editar(
            FuncionarioSecao funcionarioSecao
    ) throws Exception {
        try {
            return funcionariosSecoesRepository.save(funcionarioSecao);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

}
