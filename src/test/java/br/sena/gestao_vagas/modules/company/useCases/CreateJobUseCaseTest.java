package br.sena.gestao_vagas.modules.company.useCases;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.sena.gestao_vagas.exceptions.CompanyNotFoundException;
import br.sena.gestao_vagas.modules.company.entities.CompanyEntity;
import br.sena.gestao_vagas.modules.company.entities.JobEntity;
import br.sena.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.sena.gestao_vagas.modules.company.repositories.JobRepository;

@ExtendWith(MockitoExtension.class)
class CreateJobUseCaseTest {

  @InjectMocks
  private CreateJobUseCase createJobUseCase;

  @Mock
  private JobRepository repository;

  @Mock
  private CompanyRepository companyRepository;

  @Test
  @DisplayName("Should create a job successfully")
  void should_create_job_successfully() {
    var companyId = UUID.randomUUID();
    var company = CompanyEntity.builder()
        .id(companyId)
        .name("Test Company")
        .build();

    var job = JobEntity.builder()
        .companyId(companyId)
        .description("Java Developer")
        .benefits("Health insurance")
        .level("Senior")
        .build();

    when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
    when(repository.save(any(JobEntity.class))).thenReturn(job);

    var result = createJobUseCase.execute(job);

    assertThat(result).isNotNull();
    assertThat(result.getDescription()).isEqualTo("Java Developer");
    verify(repository).save(job);
  }

  @Test
  @DisplayName("Should throw exception when company not found")
  void should_throw_exception_when_company_not_found() {
    var companyId = UUID.randomUUID();
    var job = JobEntity.builder()
        .companyId(companyId)
        .description("Java Developer")
        .build();

    when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

    assertThrows(CompanyNotFoundException.class, () -> {
      createJobUseCase.execute(job);
    });

    verify(repository, never()).save(any(JobEntity.class));
  }
}
