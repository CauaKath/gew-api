package br.com.gew.domain.entities;

/*
 * Entidade para manter os dados do projeto
 *
 * ata: número da ATA do projeto
 * solicitante: funcionário solicitante
 * responsavel: funcionário responsável
 * status: status atual do projeto
 * secao: seção ao qual o projeto está atribuído
 * horas_apontadas: horas que já foram apontadas neste projeto
 * */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "projetos")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long numeroDoProjeto;
    private String titulo;
    private String descricao;
    private String ata;

    @ManyToOne
    @JoinColumn(name = "solicitante_id")
    private Funcionario solicitante;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private Funcionario responsavel;

    private String data_de_inicio;
    private String data_de_termino;
    private String data_de_aprovacao;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusProjeto statusProjeto;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String secao;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int horas_apontadas;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate dataDoCadastro;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate dataDaConclusao;

    @OneToMany
    private List<SecaoPagante> secoesPagantes;

    @OneToMany
    private List<Despesa> despesas;

}
