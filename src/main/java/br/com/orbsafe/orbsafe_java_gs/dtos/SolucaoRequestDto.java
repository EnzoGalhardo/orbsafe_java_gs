package br.com.orbsafe.orbsafe_java_gs.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SolucaoRequestDto {

    @NotBlank(message = "O nome da solução é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "A descrição da solução é obrigatória.")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
    private String descricao;

    @NotBlank(message = "A área de impacto é obrigatória.")
    @Size(max = 100, message = "A área de impacto deve ter no máximo 100 caracteres.")
    private String areaImpacto;

    @NotBlank(message = "O ODS relacionado é obrigatório.")
    private String ods; // Agora é uma String comum (Ex: "ODS 9 - Indústria e Inovação")

    @NotNull(message = "O valor de impacto é obrigatório.")
    @Min(value = 1, message = "O impacto mínimo deve ser 1.")
    @Max(value = 5, message = "O impacto máximo deve ser 5.")
    private Integer impacto;

    @NotNull(message = "O valor de urgência é obrigatório.")
    @Min(value = 1, message = "A urgência mínima deve ser 1.")
    @Max(value = 5, message = "A urgência máxima deve ser 5.")
    private Integer urgencia;
}
