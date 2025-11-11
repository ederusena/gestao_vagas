package br.sena.gestao_vagas.modules.candidate.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.sena.gestao_vagas.exceptions.UserFoundException;
import br.sena.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.sena.gestao_vagas.modules.candidate.repository.CandidateReposity;

@Service
public class CreateCandidateUseCase {
  @Autowired
  private CandidateReposity repository;

  public CandidateEntity execute(CandidateEntity candidate) {
    this.repository
        .findByUsername(candidate.getName())
        .ifPresent(existingCandidate -> {
          throw new UserFoundException();
        });

    this.repository
        .findByEmail(candidate.getEmail())
        .ifPresent(existingCandidate -> {
          throw new UserFoundException();
        });
    return this.repository.save(candidate);
  }

}
