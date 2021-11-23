package br.com.gew.domain.services;

import br.com.gew.domain.entities.Despesa;
import br.com.gew.domain.exception.ExceptionTratement;
import br.com.gew.domain.repositories.DespesasRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DespesasService {

    private DespesasRepository despesasRepository;

    public Despesa cadastrar(Despesa despesa) throws Exception {
        try {
            return despesasRepository.save(despesa);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public List<Despesa> listarPorProjeto(long projetoId) throws Exception {
        try {
            return despesasRepository.findAllByProjetoId(projetoId);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public Despesa editar(Despesa despesa, long despesa_id) throws Exception {
        try {
            despesa.setId(despesa_id);

            return despesasRepository.save(despesa);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public void remover(long despesa_id) throws Exception {
        try {
            despesasRepository.deleteById(despesa_id);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

}
