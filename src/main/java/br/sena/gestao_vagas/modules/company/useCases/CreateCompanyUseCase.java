package br.sena.gestao_vagas.modules.company.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.sena.gestao_vagas.exceptions.EmailFoundException;
import br.sena.gestao_vagas.exceptions.UserFoundException;
import br.sena.gestao_vagas.modules.company.entities.CompanyEntity;
import br.sena.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class CreateCompanyUseCase {

  @Autowired
  private CompanyRepository repository;

  @Autowired
  private PasswordEncoder passwordEncoder;

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
