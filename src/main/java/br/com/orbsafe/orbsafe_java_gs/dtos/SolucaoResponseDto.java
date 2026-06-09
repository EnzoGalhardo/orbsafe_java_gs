package br.com.orbsafe.orbsafe_java_gs.dtos;

import lombok.Data;

@Data
public class SolucaoResponseDto {
    private Long id;
    private String nome;
    private String descricao;
    private String areaImpacto;
    private String status; // Agora é String
    private String ods; // Agora é String
    private Integer impacto;
    private Integer urgencia;
    private String prioridade; // Agora é String
}
