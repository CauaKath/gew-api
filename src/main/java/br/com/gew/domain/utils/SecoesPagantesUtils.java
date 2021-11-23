package br.com.gew.domain.utils;

import br.com.gew.api.assembler.SecaoPaganteAssembler;
import br.com.gew.api.model.input.SecaoPaganteInputDTO;
import br.com.gew.api.model.output.SecaoPaganteOutputDTO;
import br.com.gew.domain.entities.SecaoPagante;
import br.com.gew.domain.services.ProjetoService;
import br.com.gew.domain.services.SecaoPaganteService;
import br.com.gew.domain.services.SecaoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class SecoesPagantesUtils {

    private SecaoPaganteAssembler secaoPaganteAssembler;

    private SecaoService secaoService;
    private ProjetoService projetoService;
    private SecaoPaganteService secaoPaganteService;

    public void cadastrar(
            List<SecaoPaganteInputDTO> secaoPaganteInputDTOS,
            long numeroDoProjeto,
            double total
    ) throws Exception {
        for (SecaoPaganteInputDTO secaoPaganteInputDTO : secaoPaganteInputDTOS) {
            SecaoPagante secaoPagante = secaoPaganteAssembler.toEntity(secaoPaganteInputDTO);

            secaoPagante.setSecao(secaoService.buscar(secaoPaganteInputDTO.getSecao_id()));
            secaoPagante.setPercentual(calcularPercentual(secaoPagante.getValor(), total));
            secaoPagante.setProjeto(projetoService.buscar(numeroDoProjeto));

            secaoPaganteService.cadastrar(secaoPagante);
        }
    }

    private double calcularPercentual(double valor, double total) {
        return Math.floor((valor / total) * 100);
    }

    public List<SecaoPaganteOutputDTO> listar(long projetoId) throws Exception {
        return secaoPaganteAssembler.toCollectionModel(
                secaoPaganteService.listarPorProjeto(projetoId)
        );
    }

    public void editar(
            List<SecaoPaganteInputDTO> secaoPaganteInputDTOS,
            long numeroDoProjeto,
            double total
    ) throws Exception {
        List<SecaoPagante> secaoPagantes = secaoPaganteAssembler.toCollectionEntity(secaoPaganteInputDTOS);
        List<SecaoPagante> secaoPagantesDB = secaoPaganteService.listarPorProjeto(
                projetoService.buscar(numeroDoProjeto).getId()
        );

        for (int i = 0; i < secaoPagantesDB.size(); i ++) {
            secaoPagantes.get(i).setProjeto(projetoService.buscar(numeroDoProjeto));
            secaoPagantes.get(i).setPercentual(calcularPercentual(secaoPagantes.get(i).getValor(), total));
            secaoPagantes.get(i).setSecao(
                    secaoService.buscar(secaoPaganteInputDTOS.get(i).getSecao_id())
            );

            secaoPaganteService.editar(secaoPagantes.get(i), secaoPagantesDB.get(i).getId());
        }

        if (secaoPagantes.size() > secaoPagantesDB.size()) {
            for (int i = secaoPagantesDB.size(); i < secaoPagantes.size(); i ++) {
                secaoPagantes.get(i).setProjeto(projetoService.buscar(numeroDoProjeto));
                secaoPagantes.get(i).setPercentual(
                        calcularPercentual(secaoPagantes.get(i).getValor(), total)
                );
                secaoPagantes.get(i).setSecao(
                        secaoService.buscar(secaoPaganteInputDTOS.get(i).getSecao_id())
                );

                secaoPaganteService.cadastrar(secaoPagantes.get(i));
            }
        }
    }

    public void remover(long numeroDoProjeto) throws Exception {
        List<SecaoPagante> secaoPagantesDB = secaoPaganteService.listarPorProjeto(
                projetoService.buscar(numeroDoProjeto).getId()
        );

        for (SecaoPagante secaoPagante : secaoPagantesDB) {
            secaoPaganteService.remover(secaoPagante.getId());
        }
    }
}
