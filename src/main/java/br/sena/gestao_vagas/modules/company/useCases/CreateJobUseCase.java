package br.sena.gestao_vagas.modules.company.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.sena.gestao_vagas.modules.company.entities.JobEntity;
import br.sena.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class CreateJobUseCase {

  @Autowired
  private JobRepository repository;

  public JobEntity execute(JobEntity job) {
      return this.repository.save(job);
  }
}
