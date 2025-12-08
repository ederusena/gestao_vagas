package br.sena.gestao_vagas.modules.candidate.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.sena.gestao_vagas.exceptions.JobNotFoundException;
import br.sena.gestao_vagas.exceptions.UserNotFoundException;
import br.sena.gestao_vagas.modules.candidate.repository.CandidateReposity;
import br.sena.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class ApplyJobCandidateUseCase {

  @Autowired
  private CandidateReposity candidateRepository;

  @Autowired
  private JobRepository jobRepository;

  public void execute(UUID candidateId, UUID jobId) {
    var candidate = this.candidateRepository.findById(candidateId)
        .orElseThrow(() -> {
          throw new UserNotFoundException();
        });

    var job = this.jobRepository.findById(jobId)
        .orElseThrow(() -> {
          throw new JobNotFoundException();
        });
    
    

  }
}
