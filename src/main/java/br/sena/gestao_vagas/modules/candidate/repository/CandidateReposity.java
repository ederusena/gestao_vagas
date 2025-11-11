package br.sena.gestao_vagas.modules.candidate.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.sena.gestao_vagas.modules.candidate.entities.CandidateEntity;

/**
 *
 * @author eders
 */
public interface CandidateReposity extends JpaRepository<CandidateEntity, UUID>{
  Optional<CandidateEntity> findByUsername(String username);
  Optional<CandidateEntity> findByEmail(String email);

}
