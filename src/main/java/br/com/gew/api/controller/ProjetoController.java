package br.com.gew.api.controller;

import br.com.gew.api.assembler.ProjetoAssembler;
import br.com.gew.api.model.input.ProjetoInputDTO;
import br.com.gew.api.model.output.ProjetoDataOutputDTO;
import br.com.gew.api.model.output.ProjetoOutputDTO;
import br.com.gew.domain.entities.Projeto;
import br.com.gew.domain.services.ProjetoService;
import br.com.gew.domain.utils.DespesaUtils;
import br.com.gew.domain.utils.ProjetoUtils;
import br.com.gew.domain.utils.SecoesPagantesUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    private ProjetoService projetoService;

    private ProjetoAssembler projetoAssembler;

    private ProjetoUtils projetoUtils;
    private SecoesPagantesUtils secoesPagantesUtils;
    private DespesaUtils despesaUtils;

    @PostMapping
    public ResponseEntity<ProjetoDataOutputDTO> cadastrar(
            @RequestBody ProjetoInputDTO projetoInputDTO
    ) throws Exception {

        if (!projetoUtils.verifyExceptionCadastro(projetoInputDTO)) {
            return ResponseEntity.badRequest().build();
        }

        Projeto novoProjeto = projetoUtils.setDadosPadrao(projetoInputDTO.getProjetoData());

        Projeto projeto = projetoService.cadastrar(novoProjeto);

        double totalDespesas = despesaUtils.cadastrar(projetoInputDTO.getDespesas(), projeto.getNumeroDoProjeto());

        secoesPagantesUtils.cadastrar(projetoInputDTO.getSecoesPagantes(),
                projeto.getNumeroDoProjeto(), totalDespesas);

        return ResponseEntity.ok(projetoAssembler.toModel(novoProjeto));
    }

    @GetMapping
    public ResponseEntity<List<ProjetoOutputDTO>> listar() throws Exception {
        return ResponseEntity.ok(projetoUtils.listar());
    }

    @GetMapping("/{numeroDoProjeto}")
    public ResponseEntity<ProjetoOutputDTO> buscar(
            @PathVariable long numeroDoProjeto
    ) throws Exception {
        return ResponseEntity.ok(projetoUtils.buscar(numeroDoProjeto));
    }

    @PutMapping("/{numeroDoProjeto}")
    public ResponseEntity<ProjetoDataOutputDTO> editar(
            @RequestBody ProjetoInputDTO projetoInputDTO,
            @PathVariable long numeroDoProjeto
    ) throws Exception {
        Projeto novoProjeto = projetoUtils.setDatabaseData(projetoInputDTO.getProjetoData(), numeroDoProjeto);

        Projeto projeto = projetoService.editar(novoProjeto, numeroDoProjeto);

        double totalDespesas = despesaUtils.editar(projetoInputDTO.getDespesas(), numeroDoProjeto);

        secoesPagantesUtils.editar(projetoInputDTO.getSecoesPagantes(), numeroDoProjeto, totalDespesas);

        return ResponseEntity.ok(projetoAssembler.toModel(projeto));
    }

    @DeleteMapping("/{numeroDoProjeto}")
    public ResponseEntity<ProjetoOutputDTO> remover(
            @PathVariable long numeroDoProjeto
    ) throws Exception {

        despesaUtils.remover(numeroDoProjeto);

        secoesPagantesUtils.remover(numeroDoProjeto);

        return projetoUtils.remover(numeroDoProjeto);
    }

}
