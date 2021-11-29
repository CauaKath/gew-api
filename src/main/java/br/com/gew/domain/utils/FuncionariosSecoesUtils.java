package br.com.gew.domain.utils;

import br.com.gew.domain.entities.FuncionarioSecao;
import br.com.gew.domain.services.FuncionariosSecoesService;
import br.com.gew.domain.services.SecoesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FuncionariosSecoesUtils {

    private FuncionariosSecoesService funcionariosSecoesService;
    private SecoesService secoesService;

    public void cadastrar(String secaoNome, long funcionarioCracha) throws Exception {
        FuncionarioSecao funcionarioSecao = new FuncionarioSecao();

        funcionarioSecao.setSecao_id(
                secoesService.buscarPorNome(secaoNome).getId()
        );
        funcionarioSecao.setFuncionario_cracha(funcionarioCracha);

        funcionariosSecoesService.cadastrar(funcionarioSecao);
    }

    public void editar(String secaoNome, long funcionarioCracha) throws Exception {
        FuncionarioSecao funcionarioSecao = new FuncionarioSecao();

        funcionarioSecao.setSecao_id(
                secoesService.buscarPorNome(secaoNome).getId()
        );
        funcionarioSecao.setFuncionario_cracha(funcionarioCracha);

        funcionariosSecoesService.editar(funcionarioSecao);
    }

}