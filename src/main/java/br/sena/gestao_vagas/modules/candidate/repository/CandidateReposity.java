package br.sena.gestao_vagas.modules.candidate.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.sena.gestao_vagas.modules.candidate.entity.CandidateEntity;

/**
 *
 * @author eders
 */
public interface CandidateReposity extends JpaRepository<CandidateEntity, UUID>{

}
