package br.com.gew.domain.services;

import br.com.gew.api.model.output.FuncionarioOutputDTO;
import br.com.gew.domain.entities.Funcionario;
import br.com.gew.domain.exception.ExceptionTratement;
import br.com.gew.domain.repositories.FuncionariosRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class FuncionariosService {

    private FuncionariosRepository funcionariosRepository;

    @Transactional
    public Funcionario cadastrar(Funcionario funcionario) throws Exception {
        try {
            return funcionariosRepository.save(funcionario);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public List<Funcionario> listar() {
        try {
            return funcionariosRepository.findAll();
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public Optional<Funcionario> buscar(long numeroCracha) throws Exception {
        try {
            return funcionariosRepository.findById(numeroCracha);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public Optional<Funcionario> buscarPorCpf(String cpf) throws Exception {
        try {
            return funcionariosRepository.findByCpf(cpf);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public Optional<Funcionario> buscarPorTelefone(String telefone) throws Exception {
        try {
            return funcionariosRepository.findByTelefone(telefone);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public Optional<Funcionario> buscarPorEmail(String email) throws Exception {
        try {
            return funcionariosRepository.findByEmail(email);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    @Transactional
    public Funcionario editar(Funcionario funcionario, long numeroCracha) {
        try {
            funcionario.setNumero_cracha(numeroCracha);

            return funcionariosRepository.save(funcionario);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public void remover(long numeroCracha) {
        try {
            funcionariosRepository.deleteById(numeroCracha);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

}
