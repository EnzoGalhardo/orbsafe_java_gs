package br.com.orbsafe.orbsafe_java_gs.controller;

import br.com.orbsafe.orbsafe_java_gs.dtos.ResumoGeralDto;
import br.com.orbsafe.orbsafe_java_gs.dtos.SolucaoRequestDto;
import br.com.orbsafe.orbsafe_java_gs.dtos.SolucaoResponseDto;
import br.com.orbsafe.orbsafe_java_gs.dtos.StatusUpdateRequestDto;
import br.com.orbsafe.orbsafe_java_gs.service.SolucaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solucoes")
public class SolucaoController {

    @Autowired
    private SolucaoService service;

    // 1. Cadastrar uma solução espacial
    @PostMapping
    public ResponseEntity<SolucaoResponseDto> cadastrar(@Valid @RequestBody SolucaoRequestDto request) {
        SolucaoResponseDto response = service.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // Retorna HTTP 201 Created
    }

    // 2. Listar todas as soluções
    @GetMapping
    public ResponseEntity<List<SolucaoResponseDto>> listarTodas() {
        List<SolucaoResponseDto> response = service.listarTodas();
        return ResponseEntity.ok(response); // Retorna HTTP 200 OK
    }

    // 3. Buscar solução por ID
    @GetMapping("/{id}")
    public ResponseEntity<SolucaoResponseDto> buscarPorId(@PathVariable Long id) {
        SolucaoResponseDto response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    // 4. Buscar soluções por área de impacto
    @GetMapping("/area-impacto")
    public ResponseEntity<List<SolucaoResponseDto>> buscarPorAreaImpacto(@RequestParam String busca) {
        List<SolucaoResponseDto> response = service.buscarPorAreaImpacto(busca);
        return ResponseEntity.ok(response);
    }

    // 5. Listar soluções por ODS relacionado (Agora usando String como solicitado)
    @GetMapping("/ods/{ods}")
    public ResponseEntity<List<SolucaoResponseDto>> buscarPorOds(@PathVariable String ods) {
        List<SolucaoResponseDto> response = service.buscarPorOds(ods);
        return ResponseEntity.ok(response);
    }

    // 6. Atualizar uma solução existente
    @PutMapping("/{id}")
    public ResponseEntity<SolucaoResponseDto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody SolucaoRequestDto request) {
        SolucaoResponseDto response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // 7. Alterar apenas o status da solução
    @PatchMapping("/{id}/status")
    public ResponseEntity<SolucaoResponseDto> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequestDto request) {
        SolucaoResponseDto response = service.atualizarStatus(id, request);
        return ResponseEntity.ok(response);
    }

    // 8. Excluir uma solução
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build(); // Retorna HTTP 204 No Content
    }

    // 9. Retornar um resumo geral
    @GetMapping("/resumo")
    public ResponseEntity<ResumoGeralDto> obterResumoGeral() {
        ResumoGeralDto response = service.obterResumoGeral();
        return ResponseEntity.ok(response);
    }
}