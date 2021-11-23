package br.com.gew.domain.services;

import br.com.gew.domain.entities.SecaoPagante;
import br.com.gew.domain.exception.ExceptionTratement;
import br.com.gew.domain.repositories.SecoesPagantesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class SecaoPaganteService {

    private SecoesPagantesRepository secoesPagantesRepository;

    public SecaoPagante cadastrar(SecaoPagante secaoPagante) throws Exception {
        try {
            return secoesPagantesRepository.save(secaoPagante);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public List<SecaoPagante> listarPorProjeto(long projetoId) throws Exception {
        try {
            return secoesPagantesRepository.findAllByProjetoId(projetoId);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public SecaoPagante editar(
            SecaoPagante secaoPagante,
            long secao_pagante_id
    ) throws Exception {
        try {
            secaoPagante.setId(secao_pagante_id);

            return secoesPagantesRepository.save(secaoPagante);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public void remover(long secao_pagante_id) throws Exception {
        try {
            secoesPagantesRepository.deleteById(secao_pagante_id);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

}
