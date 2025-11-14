package br.sena.gestao_vagas.modules.company.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.sena.gestao_vagas.exceptions.EmailFoundException;
import br.sena.gestao_vagas.exceptions.UserFoundException;
import br.sena.gestao_vagas.modules.company.entities.CompanyEntity;
import br.sena.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class CreateCompanyUseCase {

  @Autowired
  private CompanyRepository repository;

  public CompanyEntity execute(CompanyEntity company) {
    this.repository
        .findByUsername(company.getName())
        .ifPresent(existingCandidate -> {
          throw new UserFoundException();
        });

    this.repository
        .findByEmail(company.getEmail())
        .ifPresent(existingCandidate -> {
          throw new EmailFoundException();
        });

    return this.repository.save(company);
  }
}
