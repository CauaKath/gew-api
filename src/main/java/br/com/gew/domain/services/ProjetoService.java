package br.com.gew.domain.services;

import br.com.gew.domain.entities.Projeto;
import br.com.gew.domain.entities.StatusProjeto;
import br.com.gew.domain.exception.EntityNotFoundException;
import br.com.gew.domain.exception.ExceptionTratement;
import br.com.gew.domain.repositories.ProjetosRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class ProjetoService {

    private ProjetosRepository projetosRepository;

    public Projeto cadastrar(Projeto projeto) throws Exception {
        try {
            return projetosRepository.save(projeto);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public Projeto buscar(long numeroDoProjeto) throws Exception {
        return projetosRepository.findByNumeroDoProjeto(numeroDoProjeto)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado"));
    }

    public List<Projeto> listar() throws Exception {
        try {
            return projetosRepository.findAll();
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public List<Projeto> listarPorStatus(StatusProjeto statusProjeto) {
        try {
            return projetosRepository.findAllByStatusProjeto(statusProjeto);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public List<Projeto> listarPorDataConclusao(LocalDate data) {
        try {
            return projetosRepository.findAllByDataDaConclusao(data);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public Projeto editar(Projeto projeto, long numeroDoProjeto) throws Exception {
        projeto.setId(
                projetosRepository.findByNumeroDoProjeto(numeroDoProjeto).get().getId()
        );
        projeto.setNumeroDoProjeto(numeroDoProjeto);

        try {
            return projetosRepository.save(projeto);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public void remover(long projetoId) throws Exception {
        try {
            projetosRepository.deleteById(projetoId);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

}
