package br.com.orbsafe.orbsafe_java_gs.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StatusUpdateRequestDto {
    @NotBlank(message = "O novo status da solução é obrigatório.")
    private String status; // Agora é String
}