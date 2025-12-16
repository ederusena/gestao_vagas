package br.sena.gestao_vagas.modules.candidate.useCases;

import java.util.Optional;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import br.sena.gestao_vagas.exceptions.EmailFoundException;
import br.sena.gestao_vagas.exceptions.UserFoundException;
import br.sena.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.sena.gestao_vagas.modules.candidate.repository.CandidateReposity;

@ExtendWith(MockitoExtension.class)
class CreateCandidateUseCaseTest {

  @InjectMocks
  private CreateCandidateUseCase createCandidateUseCase;

  @Mock
  private CandidateReposity repository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("Should create a candidate successfully")
  void should_create_candidate_successfully() {
    var candidate = new CandidateEntity();
    candidate.setUsername("testuser");
    candidate.setEmail("test@email.com");
    candidate.setPassword("password123");

    when(repository.findByUsername("testuser")).thenReturn(Optional.empty());
    when(repository.findByEmail("test@email.com")).thenReturn(Optional.empty());
    when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
    when(repository.save(any(CandidateEntity.class))).thenReturn(candidate);

    var result = createCandidateUseCase.execute(candidate);

    assertThat(result).isNotNull();
    assertThat(result.getPassword()).isEqualTo("encodedPassword");
    verify(repository).save(candidate);
  }

  @Test
  @DisplayName("Should throw exception when username already exists")
  void should_throw_exception_when_username_exists() {
    var candidate = new CandidateEntity();
    candidate.setUsername("existinguser");
    candidate.setEmail("test@email.com");
    candidate.setPassword("password123");

    var existingCandidate = new CandidateEntity();
    when(repository.findByUsername("existinguser")).thenReturn(Optional.of(existingCandidate));

    assertThrows(UserFoundException.class, () -> {
      createCandidateUseCase.execute(candidate);
    });

    verify(repository, never()).save(any(CandidateEntity.class));
  }

  @Test
  @DisplayName("Should throw exception when email already exists")
  void should_throw_exception_when_email_exists() {
    var candidate = new CandidateEntity();
    candidate.setUsername("newuser");
    candidate.setEmail("existing@email.com");
    candidate.setPassword("password123");

    var existingCandidate = new CandidateEntity();
    when(repository.findByUsername("newuser")).thenReturn(Optional.empty());
    when(repository.findByEmail("existing@email.com")).thenReturn(Optional.of(existingCandidate));

    assertThrows(EmailFoundException.class, () -> {
      createCandidateUseCase.execute(candidate);
    });

    verify(repository, never()).save(any(CandidateEntity.class));
  }

  @Test
  @DisplayName("Should throw exception when password is null")
  void should_throw_exception_when_password_is_null() {
    var candidate = new CandidateEntity();
    candidate.setUsername("testuser");
    candidate.setEmail("test@email.com");
    candidate.setPassword(null);

    when(repository.findByUsername("testuser")).thenReturn(Optional.empty());
    when(repository.findByEmail("test@email.com")).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> {
      createCandidateUseCase.execute(candidate);
    });

    verify(repository, never()).save(any(CandidateEntity.class));
  }

  @Test
  @DisplayName("Should throw exception when password is too short")
  void should_throw_exception_when_password_is_too_short() {
    var candidate = new CandidateEntity();
    candidate.setUsername("testuser");
    candidate.setEmail("test@email.com");
    candidate.setPassword("12345");

    when(repository.findByUsername("testuser")).thenReturn(Optional.empty());
    when(repository.findByEmail("test@email.com")).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> {
      createCandidateUseCase.execute(candidate);
    });

    verify(repository, never()).save(any(CandidateEntity.class));
  }
}
