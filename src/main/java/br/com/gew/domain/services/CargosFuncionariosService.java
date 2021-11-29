package br.com.gew.domain.services;

import br.com.gew.domain.entities.CargoFuncionario;
import br.com.gew.domain.exception.ExceptionTratement;
import br.com.gew.domain.repositories.CargosFuncionariosRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CargosFuncionariosService {

    private CargosFuncionariosRepository cargosFuncionariosRepository;

    @Transactional
    public CargoFuncionario cadastrar(CargoFuncionario cargoFuncionario) throws Exception {
        try {
            return cargosFuncionariosRepository.save(cargoFuncionario);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public Optional<CargoFuncionario> buscarPorFuncionario(long numeroCracha) {
        try {
            return cargosFuncionariosRepository.findByFuncionarioCracha(numeroCracha);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    @Transactional
    public CargoFuncionario editar(CargoFuncionario cargoFuncionario) throws Exception {
        try {
            return cargosFuncionariosRepository.save(cargoFuncionario);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

    public void remover(long id) {
        try {
            cargosFuncionariosRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ExceptionTratement("Error: " + ex);
        }
    }

}
