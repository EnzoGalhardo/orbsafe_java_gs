package br.com.orbsafe.orbsafe_java_gs.service;

import br.com.orbsafe.orbsafe_java_gs.dtos.ResumoGeralDto;
import br.com.orbsafe.orbsafe_java_gs.dtos.SolucaoRequestDto;
import br.com.orbsafe.orbsafe_java_gs.dtos.SolucaoResponseDto;
import br.com.orbsafe.orbsafe_java_gs.dtos.StatusUpdateRequestDto;
import br.com.orbsafe.orbsafe_java_gs.exception.BusinessException;
import br.com.orbsafe.orbsafe_java_gs.exception.ResourceNotFoundException;
import br.com.orbsafe.orbsafe_java_gs.model.SolucaoEspacial;
import br.com.orbsafe.orbsafe_java_gs.repository.SolucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SolucaoService {

    @Autowired
    private SolucaoRepository repository;

    // 1. Cadastrar uma solução espacial
    public SolucaoResponseDto cadastrar(SolucaoRequestDto request) {
        SolucaoEspacial entidade = convertToEntity(request);
        entidade.setStatus("ATIVA"); // Inicia sempre ativa usando texto comum
        entidade.setPrioridade(calcularPrioridade(request.getImpacto(), request.getUrgencia()));

        SolucaoEspacial salva = repository.save(entidade);
        return convertToResponseDto(salva);
    }

    // 2. Listar todas as soluções
    public List<SolucaoResponseDto> listarTodas() {
        return repository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // 3. Buscar solução por ID
    public SolucaoResponseDto buscarPorId(Long id) {
        SolucaoEspacial entidade = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solução não encontrada com o ID: " + id));
        return convertToResponseDto(entidade);
    }

    // 4. Buscar soluções por área de impacto
    public List<SolucaoResponseDto> buscarPorAreaImpacto(String areaImpacto) {
        return repository.findByAreaImpactoContainingIgnoreCase(areaImpacto).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // 5. Listar por ODS
    public List<SolucaoResponseDto> buscarPorOds(String ods) {
        return repository.findByOds(ods).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // 6. Atualizar uma solução existente
    public SolucaoResponseDto atualizar(Long id, SolucaoRequestDto request) {
        SolucaoEspacial entidade = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solução não encontrada com o ID: " + id));

        // Regra de negócio: Não permitir alteração se o status atual for CANCELADA (usando String)
        if ("CANCELADA".equalsIgnoreCase(entidade.getStatus())) {
            throw new BusinessException("Não é permitido alterar uma solução com status CANCELADA.");
        }

        entidade.setNome(request.getNome());
        entidade.setDescricao(request.getDescricao());
        entidade.setAreaImpacto(request.getAreaImpacto());
        entidade.setOds(request.getOds());
        entidade.setImpacto(request.getImpacto());
        entidade.setUrgencia(request.getUrgencia());
        entidade.setPrioridade(calcularPrioridade(request.getImpacto(), request.getUrgencia()));

        SolucaoEspacial atualizada = repository.save(entidade);
        return convertToResponseDto(atualizada);
    }

    // 7. Alterar apenas o status da solução
    public SolucaoResponseDto atualizarStatus(Long id, StatusUpdateRequestDto request) {
        SolucaoEspacial entidade = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solução não encontrada com o ID: " + id));

        // Regra de negócio para validar se o novo status enviado faz sentido
        String novoStatus = request.getStatus().toUpperCase();
        if (!List.of("ATIVA", "INATIVA", "CANCELADA", "VALIDADA").contains(novoStatus)) {
            throw new BusinessException("Status inválido. Escolha entre ATIVA, INATIVA, CANCELADA ou VALIDADA.");
        }

        entidade.setStatus(novoStatus);
        SolucaoEspacial atualizada = repository.save(entidade);
        return convertToResponseDto(atualizada);
    }

    // 8. Excluir uma solução
    public void excluir(Long id) {
        SolucaoEspacial entidade = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solução não encontrada com o ID: " + id));

        // Regra de negócio: Impedir exclusão se o status for VALIDADA (usando String)
        if ("VALIDADA".equalsIgnoreCase(entidade.getStatus())) {
            throw new BusinessException("Não é permitido excluir uma solução com status VALIDADA.");
        }

        repository.delete(entidade);
    }

    // 9. Retornar resumo geral
    public ResumoGeralDto obterResumoGeral() {
        List<SolucaoEspacial> todas = repository.findAll();

        Long total = (long) todas.size();

        // Agrupamento por status usando strings
        Map<String, Long> porStatus = todas.stream()
                .collect(Collectors.groupingBy(SolucaoEspacial::getStatus, Collectors.counting()));

        // Agrupamento por área de impacto
        Map<String, Long> porArea = todas.stream()
                .collect(Collectors.groupingBy(SolucaoEspacial::getAreaImpacto, Collectors.counting()));

        // Contagem de prioridades altas usando string
        Long prioridadeAlta = todas.stream()
                .filter(s -> "ALTA".equalsIgnoreCase(s.getPrioridade()))
                .count();

        return new ResumoGeralDto(total, porStatus, porArea, prioridadeAlta);
    }

    // Métodos Auxiliares de Lógica Interna (Todos usando String)
    private String calcularPrioridade(Integer impacto, Integer urgencia) {
        int score = impacto * urgencia; // Varia de 1 a 25
        if (score >= 15) {
            return "ALTA";
        } else if (score >= 8) {
            return "MEDIA";
        } else {
            return "BAIXA";
        }
    }

    private SolucaoEspacial convertToEntity(SolucaoRequestDto dto) {
        SolucaoEspacial entity = new SolucaoEspacial();
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setAreaImpacto(dto.getAreaImpacto());
        entity.setOds(dto.getOds());
        entity.setImpacto(dto.getImpacto());
        entity.setUrgencia(dto.getUrgencia());
        return entity;
    }

    private SolucaoResponseDto convertToResponseDto(SolucaoEspacial entity) {
        SolucaoResponseDto dto = new SolucaoResponseDto();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setDescricao(entity.getDescricao());
        dto.setAreaImpacto(entity.getAreaImpacto());
        dto.setStatus(entity.getStatus());
        dto.setOds(entity.getOds());
        dto.setImpacto(entity.getImpacto());
        dto.setUrgencia(entity.getUrgencia());
        dto.setPrioridade(entity.getPrioridade());
        return dto;
    }
}