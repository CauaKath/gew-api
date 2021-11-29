package br.com.gew.domain.utils;

import br.com.gew.api.assembler.ProjetoAssembler;
import br.com.gew.api.model.input.DespesaInputDTO;
import br.com.gew.api.model.input.ProjetoDataInputDTO;
import br.com.gew.api.model.input.ProjetoInputDTO;
import br.com.gew.api.model.input.SecaoPaganteInputDTO;
import br.com.gew.api.model.output.ProjetoOutputDTO;
import br.com.gew.domain.entities.Projeto;
import br.com.gew.domain.entities.StatusProjeto;
import br.com.gew.domain.exception.ExceptionTratement;
import br.com.gew.domain.repositories.FuncionariosRepository;
import br.com.gew.domain.repositories.ProjetosRepository;
import br.com.gew.domain.services.ProjetosService;
import br.com.gew.domain.services.SecoesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ProjetosUtils {

    private ProjetoAssembler projetoAssembler;

    private FuncionariosRepository funcionariosRepository;
    private ProjetosRepository projetosRepository;

    private SecoesService secoesService;
    private ProjetosService projetosService;

    private DespesasUtils despesasUtils;
    private SecoesPagantesUtils secoesPagantesUtils;

    public Projeto setDadosPadrao(ProjetoDataInputDTO projetoDataInputDTO) {
        Projeto projeto = projetoAssembler.toEntity(projetoDataInputDTO);

        projeto.setStatusProjeto(StatusProjeto.NAO_INICIADO);
        projeto.setDataDoCadastro(LocalDate.now());
        projeto.setHoras_apontadas(0);

        setResponsaveis(projetoDataInputDTO, projeto);

        return projeto;
    }

    public Projeto setDatabaseData(ProjetoDataInputDTO projetoDataInputDTO, long numeroDoProjeto) {
        Projeto projeto = projetoAssembler.toEntity(projetoDataInputDTO);
        Projeto projetoDB = projetosRepository.findByNumeroDoProjeto(numeroDoProjeto).get();

        projeto.setStatusProjeto(projetoDB.getStatusProjeto());
        projeto.setDataDoCadastro(projetoDB.getDataDoCadastro());
        projeto.setHoras_apontadas(projetoDB.getHoras_apontadas());

        setResponsaveis(projetoDataInputDTO, projeto);

        return projeto;
    }

    private void setResponsaveis(ProjetoDataInputDTO projetoDataInputDTO, Projeto projeto) {
        projeto.setResponsavel(funcionariosRepository.findById(
                projetoDataInputDTO.getCracha_responsavel()
        ).get());
        projeto.setSolicitante(funcionariosRepository.findById(
                projetoDataInputDTO.getCracha_solicitante()
        ).get());
        projeto.setSecao(
                secoesService.buscarPorFuncionario(projetoDataInputDTO.getCracha_solicitante()).getNome()
        );
    }

    public List<ProjetoOutputDTO> listar() throws Exception {
        List<ProjetoOutputDTO> projetoOutputDTOS = new ArrayList<>();

        List<Projeto> projetos = projetosService.listar();

        for (Projeto projeto : projetos) {
            projetoOutputDTOS.add(montarProjeto(projeto));
        }

        return projetoOutputDTOS;
    }

    public ProjetoOutputDTO buscar(long numeroDoProjeto) throws Exception {
        return montarProjeto(projetosService.buscar(numeroDoProjeto));
    }

    public ResponseEntity<ProjetoOutputDTO> remover(long numeroDoProjeto) throws Exception {
        if (!projetosRepository.findByNumeroDoProjeto(numeroDoProjeto).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        projetosService.remover(
                projetosRepository.findByNumeroDoProjeto(numeroDoProjeto).get().getId()
        );

        return ResponseEntity.ok().build();
    }

    private ProjetoOutputDTO montarProjeto(Projeto projeto) throws Exception {
        ProjetoOutputDTO projetoOutputDTO = new ProjetoOutputDTO();

        projetoOutputDTO.setProjetoData(
                projetoAssembler.toModel(projeto)
        );

        projetoOutputDTO.setDespesas(
                despesasUtils.listar(projeto.getId())
        );

        projetoOutputDTO.setSecoesPagantes(
                secoesPagantesUtils.listar(projeto.getId())
        );

        return projetoOutputDTO;
    }

    public boolean verifyExceptionCadastro(ProjetoInputDTO projetoInputDTO) throws Exception {
        verifyAlreadyExists(projetoInputDTO.getProjetoData().getNumeroDoProjeto(),
                projetoInputDTO.getProjetoData().getTitulo());

        verifyResponsavel(projetoInputDTO.getProjetoData().getCracha_responsavel(),
                projetoInputDTO.getProjetoData().getCracha_solicitante());

        verifyDatas(projetoInputDTO.getProjetoData().getData_de_inicio(),
                projetoInputDTO.getProjetoData().getData_de_termino());

        int somaDespesas = verifyDespesas(projetoInputDTO.getDespesas());
        int somaCcPagantes = verifyCcPagantes(projetoInputDTO.getSecoesPagantes());

        verifyValores(somaDespesas, somaCcPagantes);

        return true;
    }

    public boolean verifyExceptionEdicao(
            ProjetoInputDTO projetoInputDTO, long numeroDoProjeto
    ) throws Exception {
        long projetoId = projetosRepository.findByNumeroDoProjeto(numeroDoProjeto).get().getId();

        verifyEditAlreadyExists(projetoInputDTO.getProjetoData().getNumeroDoProjeto(),
                projetoInputDTO.getProjetoData().getTitulo(), projetoId);

        verifyResponsavel(projetoInputDTO.getProjetoData().getCracha_responsavel(),
                projetoInputDTO.getProjetoData().getCracha_solicitante());

        verifyDatas(projetoInputDTO.getProjetoData().getData_de_inicio(),
                projetoInputDTO.getProjetoData().getData_de_termino());

        int somaDespesas = verifyDespesas(projetoInputDTO.getDespesas());
        int somaCcPagantes = verifyCcPagantes(projetoInputDTO.getSecoesPagantes());

        verifyValores(somaDespesas, somaCcPagantes);

        return true;
    }

    private void verifyAlreadyExists(long numeroDoProjeto, String titulo) throws Exception {
        boolean numeroDoProjetoValidation = projetosRepository.findByNumeroDoProjeto(
                numeroDoProjeto).isPresent();

        if (numeroDoProjetoValidation) {
            throw new ExceptionTratement("Projeto com este número já cadastrado");
        }

        boolean tituloValidation = projetosRepository.findByTitulo(
                titulo).isPresent();

        if (tituloValidation) {
            throw new ExceptionTratement("Projeto com este título já cadastrado");
        }
    }

    private void verifyEditAlreadyExists(long numeroDoProjeto, String titulo, long projetoId) {
        if (numeroDoProjeto != projetosRepository.findById(projetoId).get().getNumeroDoProjeto()) {
            boolean numeroDoProjetoValidation = projetosRepository.findByNumeroDoProjeto(
                    numeroDoProjeto).isPresent();

            if (numeroDoProjetoValidation) {
                throw new ExceptionTratement("Projeto com este número já cadastrado");
            }
        }

        if (!Objects.equals(titulo, projetosRepository.findById(projetoId).get().getTitulo())) {
            boolean tituloValidation = projetosRepository.findByTitulo(
                    titulo).isPresent();

            if (tituloValidation) {
                throw new ExceptionTratement("Projeto com este título já cadastrado");
            }
        }
    }

    private void verifyResponsavel(long responsavel, long solicitante) throws Exception {
        if (responsavel == solicitante) {
            throw new ExceptionTratement("O responsável não pode ser o mesmo que solicitou");
        }
    }

    private void verifyDatas(String dataDeInicio, String dataDeTermino) throws Exception {
        String[] data_inicio = sliceDate(dataDeInicio);
        String[] data_termino = sliceDate(dataDeTermino);

        if (Integer.parseInt(data_inicio[2]) > Integer.parseInt(data_termino[2])) {
            throw new ExceptionTratement("Ano de termino anterior ao de inicio");
        }

        if (Integer.parseInt(data_inicio[1]) > Integer.parseInt(data_termino[1])) {
            throw new ExceptionTratement("Mês de termino anterior ao de inicio");
        }

        if (Integer.parseInt(data_inicio[0]) > Integer.parseInt(data_termino[0])) {
            throw new ExceptionTratement("Dia do termino anterior ao de inicio");
        }
    }

    private int verifyDespesas(List<DespesaInputDTO> despesasInputDTOS) throws Exception {
        int somaDespesas = 0;
        String[] despesas = new String[despesasInputDTOS.size()];

        if (despesasInputDTOS.size() > 1) {
            for (int i = 0; i < despesasInputDTOS.size(); i ++) {
                despesas[i] = despesasInputDTOS.get(i).getNome();
                somaDespesas += despesasInputDTOS.get(i).getValor().intValue();
            }

            for (int i = 1; i < despesasInputDTOS.size(); i ++) {
                for (int j = 1; j < i + 1; j ++) {
                    if (despesas[i].equals(despesas[j - 1])) {
                        throw new ExceptionTratement("Despesa já informada anteriormente");
                    }
                }
            }
        }

        return somaDespesas;
    }

    private int verifyCcPagantes(List<SecaoPaganteInputDTO> secaoPaganteInputDTOS) throws Exception {
        int somaCcPagantes = 0;

        long[] ccPagantes = new long[secaoPaganteInputDTOS.size()];

        if (secaoPaganteInputDTOS.size() > 1) {
            for (int i = 0; i < secaoPaganteInputDTOS.size(); i ++) {
                ccPagantes[i] = secaoPaganteInputDTOS.get(i).getSecao_id();
                somaCcPagantes += secaoPaganteInputDTOS.get(i).getValor().intValue();
            }

            for (int i = 1; i < secaoPaganteInputDTOS.size(); i ++) {
                for (int j = 1; j < i; j ++) {
                    if (ccPagantes[i] == ccPagantes[j - 1]) {
                        throw new ExceptionTratement("Secao já informada anteriormente");
                    }
                }
            }
        }

        return somaCcPagantes;
    }

    private void verifyValores(int somaDespesas, int somaCcPagantes) throws Exception {
        if (somaCcPagantes < somaDespesas) {
            throw new ExceptionTratement("Verba dos ccPagantes menor do que a necessária");
        }

        if (somaCcPagantes > somaDespesas) {
            throw new ExceptionTratement("Verba dos ccPagantes maior do que a necessária");
        }
    }

    private String[] sliceDate(String date) {
        return date.split("/");
    }

}
