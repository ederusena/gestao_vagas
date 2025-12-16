package br.sena.gestao_vagas.modules.candidate.useCases;

import java.util.Optional;
import java.util.UUID;

import javax.naming.AuthenticationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import br.sena.gestao_vagas.modules.candidate.dto.AuthRequestCandidateDTO;
import br.sena.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.sena.gestao_vagas.modules.candidate.repository.CandidateReposity;

@ExtendWith(MockitoExtension.class)
class AuthCandidateUseCaseTest {

  @InjectMocks
  private AuthCandidateUseCase authCandidateUseCase;

  @Mock
  private CandidateReposity repository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(authCandidateUseCase, "secretKey", "test-secret-key");
  }

  @Test
  @DisplayName("Should authenticate candidate successfully")
  void should_authenticate_candidate_successfully() throws AuthenticationException {
    var candidateId = UUID.randomUUID();
    var candidate = new CandidateEntity();
    candidate.setId(candidateId);
    candidate.setUsername("testuser");
    candidate.setPassword("encodedPassword");

    var authRequest = new AuthRequestCandidateDTO("testuser", "password123");

    when(repository.findByUsername("testuser")).thenReturn(Optional.of(candidate));
    when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

    var result = authCandidateUseCase.execute(authRequest);

    assertThat(result).isNotNull();
    assertThat(result.getAccessToken()).isNotNull();
    assertThat(result.getExpiresIn()).isGreaterThan(0);
  }

  @Test
  @DisplayName("Should throw exception when candidate not found")
  void should_throw_exception_when_candidate_not_found() {
    var authRequest = new AuthRequestCandidateDTO("nonexistent", "password123");

    when(repository.findByUsername("nonexistent")).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> {
      authCandidateUseCase.execute(authRequest);
    });
  }

  @Test
  @DisplayName("Should throw exception when password is invalid")
  void should_throw_exception_when_password_is_invalid() {
    var candidate = new CandidateEntity();
    candidate.setUsername("testuser");
    candidate.setPassword("encodedPassword");

    var authRequest = new AuthRequestCandidateDTO("testuser", "wrongpassword");

    when(repository.findByUsername("testuser")).thenReturn(Optional.of(candidate));
    when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

    assertThrows(AuthenticationException.class, () -> {
      authCandidateUseCase.execute(authRequest);
    });
  }
}
