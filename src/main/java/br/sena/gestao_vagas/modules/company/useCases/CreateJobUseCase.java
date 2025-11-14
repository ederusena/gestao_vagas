package br.sena.gestao_vagas.modules.company.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.sena.gestao_vagas.modules.company.entities.JobEntity;
import br.sena.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.sena.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class CreateJobUseCase {

  @Autowired
  private JobRepository repository;

  @Autowired
  private CompanyRepository companyRepository;

  public JobEntity execute(JobEntity job) {
    var companyExists = this.companyRepository.findById(job.getCompanyId());
    if (companyExists.isPresent()) {
      return this.repository.save(job);
    } else {
      throw new IllegalArgumentException("Empresa não encontrada para o ID fornecido.");
    }
  }
}
