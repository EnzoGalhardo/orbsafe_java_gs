package br.com.orbsafe.orbsafe_java_gs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_SOLUCAO_ESPACIAL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolucaoEspacial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_solucao")
    @SequenceGenerator(name = "seq_solucao", sequenceName = "SEQ_SOLUCAO_ESPACIAL", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(name = "area_impacto", nullable = false, length = 100)
    private String areaImpacto;

    @Column(nullable = false, length = 20)
    private String status; // Será "ATIVA", "INATIVA", "CANCELADA" ou "VALIDADA"

    @Column(nullable = false, length = 100)
    private String ods; // Ex: "ODS 9", "ODS 12"

    @Column(nullable = false)
    private Integer impacto; // Escala de 1 a 5

    @Column(nullable = false)
    private Integer urgencia; // Escala de 1 a 5

    @Column(nullable = false, length = 10)
    private String prioridade; // Será "BAIXA", "MEDIA" ou "ALTA"
}
