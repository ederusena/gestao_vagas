package br.sena.gestao_vagas.modules.candidate.useCases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.sena.gestao_vagas.exceptions.EmailFoundException;
import br.sena.gestao_vagas.exceptions.UserFoundException;
import br.sena.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.sena.gestao_vagas.modules.candidate.repository.CandidateReposity;

@Service
public class CreateCandidateUseCase {
  private final CandidateReposity repository;
  private final PasswordEncoder passwordEncoder;

  public CreateCandidateUseCase(CandidateReposity repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }

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

    if (candidate.getPassword() == null || candidate.getPassword().length() < 6) {
      throw new IllegalArgumentException("Password must be at least 6 characters long");
    }

    var password = passwordEncoder.encode(candidate.getPassword());
    candidate.setPassword(password);

    return this.repository.save(candidate);
  }

}
