package br.com.gew.domain.utils;

import br.com.gew.api.assembler.DespesaAssembler;
import br.com.gew.api.model.input.DespesaInputDTO;
import br.com.gew.api.model.output.DespesaOutputDTO;
import br.com.gew.domain.entities.Despesa;
import br.com.gew.domain.services.DespesasService;
import br.com.gew.domain.services.ProjetoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DespesaUtils {

    private DespesaAssembler despesaAssembler;

    private DespesasService despesasService;

    private ProjetoService projetoService;

    public double cadastrar(
            List<DespesaInputDTO> despesaInputDTOS,
            long numeroDoProjeto
    ) throws Exception {
        List<Despesa> despesas = despesaAssembler.toCollectionEntity(despesaInputDTOS);

        for (Despesa despesa : despesas) {
            despesa.setProjeto(projetoService.buscar(numeroDoProjeto));

            despesasService.cadastrar(despesa);
        }

        return calculaTotalDespesas(despesas);
    }

    private double calculaTotalDespesas(List<Despesa> despesas) {
        double total = 0;

        for (Despesa despesa : despesas) {
            total += despesa.getValor();
        }

        return total;
    }

    public List<DespesaOutputDTO> listar(long projetoId) throws Exception {
        return despesaAssembler.toCollectionModel(
                despesasService.listarPorProjeto(projetoId)
        );
    }

    public double editar(
            List<DespesaInputDTO> despesaInputDTOS,
            long numeroDoProjeto
    ) throws Exception {
        List<Despesa> despesas = despesaAssembler.toCollectionEntity(despesaInputDTOS);
        List<Despesa> despesasDB = despesasService.listarPorProjeto(
                projetoService.buscar(numeroDoProjeto).getId()
        );

        for (int i = 0; i < despesasDB.size(); i ++) {
            despesas.get(i).setProjeto(projetoService.buscar(numeroDoProjeto));

            despesasService.editar(despesas.get(i), despesasDB.get(i).getId());
        }

        if (despesas.size() > despesasDB.size()) {
            for (int i = despesasDB.size(); i < despesas.size(); i ++) {
                despesas.get(i).setProjeto(projetoService.buscar(numeroDoProjeto));

                despesasService.cadastrar(despesas.get(i));
            }
        }

        return calculaTotalDespesas(despesas);
    }

    public void remover(long numeroDoProjeto) throws Exception {
        List<Despesa> despesasDB = despesasService.listarPorProjeto(
                projetoService.buscar(numeroDoProjeto).getId()
        );

        for (Despesa despesa : despesasDB) {
            despesasService.remover(despesa.getId());
        }
    }

}
