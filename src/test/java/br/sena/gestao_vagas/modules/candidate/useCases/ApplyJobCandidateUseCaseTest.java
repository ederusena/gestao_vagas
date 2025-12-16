package br.sena.gestao_vagas.modules.candidate.useCases;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.sena.gestao_vagas.exceptions.JobNotFoundException;
import br.sena.gestao_vagas.exceptions.UserNotFoundException;
import br.sena.gestao_vagas.modules.candidate.entities.ApplyJobEntity;
import br.sena.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.sena.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import br.sena.gestao_vagas.modules.candidate.repository.CandidateReposity;
import br.sena.gestao_vagas.modules.company.entities.JobEntity;
import br.sena.gestao_vagas.modules.company.repositories.JobRepository;

@ExtendWith(MockitoExtension.class)
class ApplyJobCandidateUseCaseTest {

  @InjectMocks
  private ApplyJobCandidateUseCase applyJobCandidateUseCase;

  @Mock
  private CandidateReposity candidateRepository;

  @Mock
  private JobRepository jobRepository;

  @Mock
  private ApplyJobRepository applyJobRepository;

  @Test
  @DisplayName("Should not be able to apply job with candidate not found")
  void should_not_be_able_to_apply_job_with_candidate_not_found() {
    try {
      applyJobCandidateUseCase.execute(
          UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
          UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));
    } catch (Exception e) {
      assertThat(e).isInstanceOf(UserNotFoundException.class);
    }
  }

  @Test
  @DisplayName("Should not be able to apply job with job not found")
  void should_not_be_able_to_apply_job_with_job_not_found() {
    var candidateId = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
    var candidate = new CandidateEntity();
    candidate.setId(candidateId);

    when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));

    try {
      applyJobCandidateUseCase.execute(
          candidateId,
          UUID.fromString("123e4567-e89b-12d3-a456-426614174003"));
    } catch (Exception e) {
      assertThat(e).isInstanceOf(JobNotFoundException.class);
    }
  }

  @Test
  @DisplayName("Should be able to apply job")
  void should_be_able_to_apply_job() {
    var candidateId = UUID.randomUUID();
    var jobId = UUID.randomUUID();

    var applyJob = ApplyJobEntity.builder()
        .candidateId(candidateId)
        .jobId(jobId)
        .build();

    var applyJobCreated = ApplyJobEntity.builder()
        .id(UUID.randomUUID())
        .build();

    when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(new CandidateEntity()));

    when(jobRepository.findById(jobId)).thenReturn(Optional.of(new JobEntity()));

    when(applyJobRepository.save(applyJob)).thenReturn(applyJobCreated);

    var result = applyJobCandidateUseCase.execute(
        candidateId,
        jobId);

    assertThat(result).hasFieldOrProperty("id");
    assertNotNull(result.getId());
  }
}
