package br.sena.gestao_vagas.modules.candidate.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.sena.gestao_vagas.exceptions.EmailFoundException;
import br.sena.gestao_vagas.exceptions.UserFoundException;
import br.sena.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.sena.gestao_vagas.modules.candidate.repository.CandidateReposity;

@Service
public class CreateCandidateUseCase {
  @Autowired
  private CandidateReposity repository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public CandidateEntity execute(CandidateEntity candidate) {
    this.repository
        .findByUsername(candidate.getUsername())
        .ifPresent(existingCandidate -> {
          throw new UserFoundException();
        });

    this.repository
        .findByEmail(candidate.getEmail())
        .ifPresent(existingCandidate -> {
          throw new EmailFoundException();
        });

    var password = passwordEncoder.encode(candidate.getPassword());
    candidate.setPassword(password);

    return this.repository.save(candidate);
  }

}
