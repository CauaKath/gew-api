package br.com.gew.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "funcionarios")
public class Funcionario {

    @Id
    private long numero_cracha;

    private String nome;
    private String cpf;
    private String telefone;
    private double valor_hora;

    private String email;

    @JsonIgnore
    private String senha;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "funcionarios_secoes", joinColumns = @JoinColumn(name = "funcionario_cracha", referencedColumnName = "numero_cracha"),
            inverseJoinColumns = @JoinColumn(name = "secao_id", referencedColumnName = "id"))
    private List<Secao> secoes;

}
