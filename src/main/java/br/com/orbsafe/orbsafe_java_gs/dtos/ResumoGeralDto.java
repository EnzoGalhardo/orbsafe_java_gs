package br.com.orbsafe.orbsafe_java_gs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@Data
@AllArgsConstructor
public class ResumoGeralDto {
    private Long totalSolucoes;
    private Map<String, Long> quantidadePorStatus;
    private Map<String, Long> quantidadePorAreaImpacto;
    private Long totalPrioridadeAlta;
}
