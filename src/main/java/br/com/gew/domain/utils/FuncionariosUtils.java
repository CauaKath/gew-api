package br.com.gew.domain.utils;

import br.com.gew.api.model.input.FuncionarioDataInputDTO;
import br.com.gew.api.model.input.FuncionarioInputDTO;
import br.com.gew.domain.exception.ExceptionTratement;
import br.com.gew.domain.services.CargosService;
import br.com.gew.domain.services.FuncionariosService;
import br.com.gew.domain.services.SecoesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FuncionariosUtils {

    private FuncionariosService funcionariosService;
    private CargosService cargosService;
    private SecoesService secoesService;

    public boolean verifyExceptionCadastro(
            FuncionarioInputDTO funcionarioInputDTO
    ) throws Exception {
        verifyFuncionarioInfo(funcionarioInputDTO.getFuncionario());

        verifyCargo(funcionarioInputDTO.getCargo());

        verifySecao(funcionarioInputDTO.getSecao());

        return false;
    }

    public boolean verifyExceptionEdicao(
            FuncionarioInputDTO funcionarioInputDTO, long numeroCracha
    ) throws Exception {
        verifyFuncionarioInfoEdit(funcionarioInputDTO.getFuncionario(), numeroCracha);

        verifyCargo(funcionarioInputDTO.getCargo());

        verifySecao(funcionarioInputDTO.getSecao());

        return false;
    }

    private void verifyFuncionarioInfo(
            FuncionarioDataInputDTO funcionarioDataInputDTO
    ) throws Exception {
        if (funcionariosService.buscar(
                funcionarioDataInputDTO.getNumero_cracha()
        ).isPresent()) {
            throw new ExceptionTratement("Funcionário com esse crachá já cadastrado");
        }

        if (funcionariosService.buscarPorCpf(
                funcionarioDataInputDTO.getCpf()
        ).isPresent()) {
            throw new ExceptionTratement("Funcionário com esse cpf já cadastrado");
        }

        if (funcionariosService.buscarPorTelefone(
                funcionarioDataInputDTO.getTelefone()
        ).isPresent()) {
            throw new ExceptionTratement("Funcionário com esse telefone já cadastrado");
        }

        if (funcionariosService.buscarPorEmail(
                funcionarioDataInputDTO.getEmail()
        ).isPresent()) {
            throw new ExceptionTratement("Funcionário com esse email já cadastrado");
        }
    }

    private void verifyFuncionarioInfoEdit(
            FuncionarioDataInputDTO funcionarioDataInputDTO,
            long numeroCracha
    ) throws Exception {
        if (funcionarioDataInputDTO.getNumero_cracha() !=
                funcionariosService.buscar(numeroCracha).get().getNumero_cracha()
        ) {
            boolean numeroCrachaValidation = funcionariosService.buscar(
                    funcionarioDataInputDTO.getNumero_cracha()).isPresent();

            if (numeroCrachaValidation) {
                throw new ExceptionTratement("Funcionário com este crachá já cadastrado");
            }
        }

        if (!funcionarioDataInputDTO.getCpf()
                .equals(funcionariosService.buscar(numeroCracha).get().getCpf())
        ) {
            boolean cpfValidation = funcionariosService.buscarPorCpf(
                    funcionarioDataInputDTO.getCpf()).isPresent();

            if (cpfValidation) {
                throw new ExceptionTratement("Funcionário com este cpf já cadastrado");
            }
        }

        if (!funcionarioDataInputDTO.getTelefone()
                .equals(funcionariosService.buscar(numeroCracha).get().getTelefone())
        ) {
            boolean telefoneValidation = funcionariosService.buscarPorTelefone(
                    funcionarioDataInputDTO.getTelefone()).isPresent();

            if (telefoneValidation) {
                throw new ExceptionTratement("Funcionário com este telefone já cadastrado");
            }
        }

        if (!funcionarioDataInputDTO.getEmail()
                .equals(funcionariosService.buscar(numeroCracha).get().getEmail())
        ) {
            boolean emailValidation = funcionariosService.buscarPorEmail(
                    funcionarioDataInputDTO.getEmail()).isPresent();

            if (emailValidation) {
                throw new ExceptionTratement("Funcionário com este email já cadastrado");
            }
        }
    }

    private void verifyCargo(String cargo) {
        if (cargo.equalsIgnoreCase("CONSULTOR")) {
            throw new ExceptionTratement("Use a rota de cadastro de consultor para isso");
        }

        if (cargosService.buscarPorNome(
                "ROLE_" + cargo.toUpperCase()
        ) == null) {
            throw new ExceptionTratement("Cargo informado não existe");
        }
    }

    private void verifySecao(String secao) {
        if (secoesService.buscarPorNome(
                secao
        ) == null) {
            throw new ExceptionTratement("Seção informada não encontrada");
        }
    }

}
