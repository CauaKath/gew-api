package br.com.gew.domain.utils;

import br.com.gew.api.model.output.ContagemOutputDTO;
import br.com.gew.api.model.output.ProjetoConcluidosPorDiaOutputDTO;
import br.com.gew.api.model.output.ProjetoContagemOutputDTO;
import br.com.gew.api.model.output.ProjetoVerbaOutputDTO;
import br.com.gew.domain.entities.Projeto;
import br.com.gew.domain.entities.SecaoPagante;
import br.com.gew.domain.entities.StatusProjeto;
import br.com.gew.domain.services.ProjetoService;
import br.com.gew.domain.services.SecaoPaganteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ProjetoContagemUtils {

    private ProjetoService projetoService;
    private SecaoPaganteService secaoPaganteService;

    public ContagemOutputDTO contar() throws Exception {
        ContagemOutputDTO contagem = new ContagemOutputDTO();

        contagem.setContagem(contarPorStatus());
        contagem.setVerba(contarVerba());

        return contagem;
    }

    private ProjetoContagemOutputDTO contarPorStatus() throws Exception {
        ProjetoContagemOutputDTO projetoContagem = new ProjetoContagemOutputDTO();

        projetoContagem.setConcluidos(projetoService.listarPorStatus(StatusProjeto.CONCLUIDO).size());
        projetoContagem.setAtrasados(projetoService.listarPorStatus(StatusProjeto.ATRASADO).size());
        projetoContagem.setEmAndamento(projetoService.listarPorStatus(StatusProjeto.EM_ANDAMENTO).size());
        projetoContagem.setTotal(projetoService.listar().size());

        return projetoContagem;
    }

    private ProjetoVerbaOutputDTO contarVerba() throws Exception {
        ProjetoVerbaOutputDTO projetoVerba = new ProjetoVerbaOutputDTO();

        projetoVerba.setVerbaAtrasados(contarVerbaPorStatus(StatusProjeto.ATRASADO));
        projetoVerba.setVerbaConcluidos(contarVerbaPorStatus(StatusProjeto.CONCLUIDO));
        projetoVerba.setVerbaEmAndamento(contarVerbaPorStatus(StatusProjeto.EM_ANDAMENTO));
        projetoVerba.setVerbaNaoIniciados(contarVerbaPorStatus(StatusProjeto.NAO_INICIADO));

        return projetoVerba;
    }

    private double contarVerbaPorStatus(StatusProjeto statusProjeto) throws Exception {
        double total = 0;
        List<Projeto> projetos = projetoService.listarPorStatus(statusProjeto);

        for (Projeto projeto : projetos) {
            List<SecaoPagante> secaoPagantes = secaoPaganteService.listarPorProjeto(projeto.getId());

            for (SecaoPagante secaoPagante : secaoPagantes) {
                total += secaoPagante.getValor();
            }
        }

        return total;
    }

    public List<ProjetoConcluidosPorDiaOutputDTO> concluidosUltimosDias() {
        LocalDate hoje = LocalDate.now();

        List<ProjetoConcluidosPorDiaOutputDTO> projetos = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            ProjetoConcluidosPorDiaOutputDTO projeto = new ProjetoConcluidosPorDiaOutputDTO();

            String data_nova = hoje.getDayOfMonth() + "/" + hoje.getMonth().getValue();

            projeto.setProjetosConcluidos(buscarProjetos(hoje).size());
            projeto.setData(data_nova);

            hoje = hoje.minusDays(1);

            projetos.add(projeto);
        }

        return projetos;
    }

    private List<Projeto> buscarProjetos(LocalDate data) {
        return projetoService.listarPorDataConclusao(data);
    }

}
