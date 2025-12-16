package br.sena.gestao_vagas.modules.candidate.useCases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.sena.gestao_vagas.exceptions.JobNotFoundException;
import br.sena.gestao_vagas.exceptions.UserNotFoundException;
import br.sena.gestao_vagas.modules.candidate.entities.ApplyJobEntity;
import br.sena.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import br.sena.gestao_vagas.modules.candidate.repository.CandidateReposity;
import br.sena.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class ApplyJobCandidateUseCase {

  private final CandidateReposity candidateRepository;
  private final JobRepository jobRepository;
  private final ApplyJobRepository applyJobRepository;

  public ApplyJobCandidateUseCase(CandidateReposity candidateRepository,
                                   JobRepository jobRepository,
                                   ApplyJobRepository applyJobRepository) {
    this.candidateRepository = candidateRepository;
    this.jobRepository = jobRepository;
    this.applyJobRepository = applyJobRepository;
  }

  public ApplyJobEntity execute(UUID candidateId, UUID jobId) {
    var candidate = this.candidateRepository.findById(candidateId)
        .orElseThrow(() -> {
          throw new UserNotFoundException();
        });

    var job = this.jobRepository.findById(jobId)
        .orElseThrow(() -> {
          throw new JobNotFoundException();
        });

    var applyJob = ApplyJobEntity.builder()
        .candidateId(candidateId)
        .jobId(jobId)
        .build();

    var applyJobCreated = this.applyJobRepository.save(applyJob);
    return applyJobCreated;
  }
}
