package br.sena.gestao_vagas.modules.company.useCases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.sena.gestao_vagas.exceptions.EmailFoundException;
import br.sena.gestao_vagas.exceptions.UserFoundException;
import br.sena.gestao_vagas.modules.company.entities.CompanyEntity;
import br.sena.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class CreateCompanyUseCase {

  private final CompanyRepository repository;
  private final PasswordEncoder passwordEncoder;

  public CreateCompanyUseCase(CompanyRepository repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }

  public CompanyEntity execute(CompanyEntity company) {
    this.repository
        .findByUsername(company.getUsername())
        .ifPresent(existingCandidate -> {
          throw new UserFoundException();
        });

    this.repository
        .findByEmail(company.getEmail())
        .ifPresent(existingCandidate -> {
          throw new EmailFoundException();
        });

    if (company.getPassword() == null || company.getPassword().length() < 6) {
      throw new IllegalArgumentException("Password must be at least 6 characters long");
    }

    var password = passwordEncoder.encode(company.getPassword());
    company.setPassword(password);

    return this.repository.save(company);
  }
}
