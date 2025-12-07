package br.sena.gestao_vagas.modules.candidate.useCases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.sena.gestao_vagas.modules.company.dto.JobResponseDTO;
import br.sena.gestao_vagas.modules.company.entities.JobEntity;
import br.sena.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class AllJobsByFilterUseCase {

  @Autowired
  private JobRepository jobRepository;

  public List<JobResponseDTO> execute(String filter) {
    List<JobEntity> jobs = this.jobRepository.findByDescriptionContainingIgnoreCase(filter);
    var jobsDTO = jobs.stream().map(job -> JobResponseDTO.builder()
        .description(job.getDescription())
        .benefits(job.getBenefits())
        .level(job.getLevel())
        .companyName(job.getCompany().getName())
        .companyEmail(job.getCompany().getEmail())
        .build()).toList();

    return jobsDTO;
  }
}
