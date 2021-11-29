package br.com.gew.api.controller;

import br.com.gew.api.assembler.FuncionarioAssembler;
import br.com.gew.api.model.input.FuncionarioInputDTO;
import br.com.gew.api.model.output.FuncionarioOutputDTO;
import br.com.gew.domain.entities.Funcionario;
import br.com.gew.domain.services.FuncionariosService;
import br.com.gew.domain.utils.CargosFuncionariosUtils;
import br.com.gew.domain.utils.FuncionariosSecoesUtils;
import br.com.gew.domain.utils.FuncionariosUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("funcionarios")
public class FuncionarioController {

    private FuncionariosUtils funcionariosUtils;
    private CargosFuncionariosUtils cargosFuncionariosUtils;
    private FuncionariosSecoesUtils funcionariosSecoesUtils;

    private FuncionariosService funcionariosService;

    private FuncionarioAssembler funcionarioAssembler;

    @PostMapping
    public ResponseEntity<FuncionarioOutputDTO> cadastrar(
            @RequestBody FuncionarioInputDTO funcionarioInputDTO
    ) throws Exception {
        if (funcionariosUtils.verifyExceptionCadastro(funcionarioInputDTO)) {
            return ResponseEntity.badRequest().build();
        }

        Funcionario novoFuncionario = funcionarioAssembler
                .toEntity(funcionarioInputDTO.getFuncionario());

        Funcionario funcionario = funcionariosService.cadastrar(novoFuncionario);

        funcionariosSecoesUtils.cadastrar(
                funcionarioInputDTO.getSecao(), funcionario.getNumero_cracha()
        );

        cargosFuncionariosUtils.cadastrar(
                funcionarioInputDTO.getCargo(), funcionario.getNumero_cracha()
        );

        return ResponseEntity.ok(funcionarioAssembler.toModel(funcionario));
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioOutputDTO>> listar() throws Exception {
        return ResponseEntity.ok(
                funcionarioAssembler.toCollectionModel(funcionariosService.listar()));
    }

    @GetMapping("/{numero_cracha}")
    public ResponseEntity<FuncionarioOutputDTO> buscar(
        @PathVariable long numero_cracha
    ) throws Exception {
        return ResponseEntity.ok(
                funcionarioAssembler.toModel(funcionariosService.buscar(numero_cracha).get()));
    }

    @PutMapping("/{numeroCracha}")
    public ResponseEntity<FuncionarioOutputDTO> editar(
            @RequestBody FuncionarioInputDTO funcionarioInputDTO,
            @PathVariable long numeroCracha
    ) throws Exception {
        if (funcionariosUtils.verifyExceptionEdicao(funcionarioInputDTO, numeroCracha)) {
            return ResponseEntity.badRequest().build();
        }

        Funcionario novoFuncionario = funcionarioAssembler
                .toEntity(funcionarioInputDTO.getFuncionario());

        Funcionario funcionario = funcionariosService.editar(novoFuncionario, numeroCracha);

        funcionariosSecoesUtils.editar(
                funcionarioInputDTO.getSecao(), funcionario.getNumero_cracha()
        );

        cargosFuncionariosUtils.editar(
                funcionarioInputDTO.getCargo(), funcionario.getNumero_cracha()
        );

        return ResponseEntity.ok(funcionarioAssembler.toModel(funcionario));
    }

}
