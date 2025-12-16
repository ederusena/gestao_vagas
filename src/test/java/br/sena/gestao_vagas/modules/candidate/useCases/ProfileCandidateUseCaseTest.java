package br.sena.gestao_vagas.modules.candidate.useCases;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.sena.gestao_vagas.exceptions.UserNotFoundException;
import br.sena.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.sena.gestao_vagas.modules.candidate.repository.CandidateReposity;

@ExtendWith(MockitoExtension.class)
class ProfileCandidateUseCaseTest {

  @InjectMocks
  private ProfileCandidateUseCase profileCandidateUseCase;

  @Mock
  private CandidateReposity repository;

  @Test
  @DisplayName("Should get candidate profile successfully")
  void should_get_candidate_profile_successfully() {
    var candidateId = UUID.randomUUID();
    var candidate = new CandidateEntity();
    candidate.setId(candidateId);
    candidate.setUsername("testuser");
    candidate.setName("Test User");
    candidate.setEmail("test@email.com");
    candidate.setDescription("Test description");

    when(repository.findById(candidateId)).thenReturn(Optional.of(candidate));

    var result = profileCandidateUseCase.execute(candidateId);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(candidateId.toString());
    assertThat(result.getUsername()).isEqualTo("testuser");
    assertThat(result.getName()).isEqualTo("Test User");
    assertThat(result.getEmail()).isEqualTo("test@email.com");
    assertThat(result.getDescription()).isEqualTo("Test description");
  }

  @Test
  @DisplayName("Should throw exception when candidate not found")
  void should_throw_exception_when_candidate_not_found() {
    var candidateId = UUID.randomUUID();

    when(repository.findById(candidateId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      profileCandidateUseCase.execute(candidateId);
    });
  }

  @Test
  @DisplayName("Should throw exception when ID is null")
  void should_throw_exception_when_id_is_null() {
    assertThrows(IllegalArgumentException.class, () -> {
      profileCandidateUseCase.execute(null);
    });
  }
}
