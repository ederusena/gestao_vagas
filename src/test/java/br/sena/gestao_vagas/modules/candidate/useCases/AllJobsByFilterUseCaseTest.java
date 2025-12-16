package br.sena.gestao_vagas.modules.candidate.useCases;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.sena.gestao_vagas.modules.company.entities.CompanyEntity;
import br.sena.gestao_vagas.modules.company.entities.JobEntity;
import br.sena.gestao_vagas.modules.company.repositories.JobRepository;

@ExtendWith(MockitoExtension.class)
class AllJobsByFilterUseCaseTest {

  @InjectMocks
  private AllJobsByFilterUseCase allJobsByFilterUseCase;

  @Mock
  private JobRepository jobRepository;

  @Test
  @DisplayName("Should return jobs filtered by description")
  void should_return_jobs_filtered_by_description() {
    var company = CompanyEntity.builder()
        .id(UUID.randomUUID())
        .name("Test Company")
        .email("company@email.com")
        .build();

    var job1 = JobEntity.builder()
        .id(UUID.randomUUID())
        .description("Java Developer")
        .benefits("Health insurance")
        .level("Senior")
        .company(company)
        .build();

    var job2 = JobEntity.builder()
        .id(UUID.randomUUID())
        .description("Java Architect")
        .benefits("Flexible hours")
        .level("Expert")
        .company(company)
        .build();

    when(jobRepository.findByDescriptionContainingIgnoreCase("java"))
        .thenReturn(Arrays.asList(job1, job2));

    var result = allJobsByFilterUseCase.execute("java");

    assertThat(result).isNotNull().hasSize(2);
    assertThat(result.get(0).getDescription()).isEqualTo("Java Developer");
    assertThat(result.get(0).getCompanyName()).isEqualTo("Test Company");
    assertThat(result.get(1).getDescription()).isEqualTo("Java Architect");
  }

  @Test
  @DisplayName("Should return empty list when no jobs match filter")
  void should_return_empty_list_when_no_jobs_match() {
    when(jobRepository.findByDescriptionContainingIgnoreCase("python"))
        .thenReturn(List.of());

    var result = allJobsByFilterUseCase.execute("python");

    assertThat(result).isNotNull().isEmpty();
  }
}
