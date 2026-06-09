package br.com.orbsafe.orbsafe_java_gs.repository;

import br.com.orbsafe.orbsafe_java_gs.model.SolucaoEspacial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolucaoRepository extends JpaRepository<SolucaoEspacial, Long> {

    // Buscar soluções por área de impacto
    List<SolucaoEspacial> findByAreaImpactoContainingIgnoreCase(String areaImpacto);

    // Buscar soluções por ODS relacionado (Agora usando String)
    List<SolucaoEspacial> findByOds(String ods);
}
