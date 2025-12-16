package br.sena.gestao_vagas.modules.company.useCases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import javax.naming.AuthenticationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import br.sena.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.sena.gestao_vagas.modules.company.entities.CompanyEntity;
import br.sena.gestao_vagas.modules.company.repositories.CompanyRepository;

@ExtendWith(MockitoExtension.class)
class AuthCompanyUseCaseTest {

  @InjectMocks
  private AuthCompanyUseCase authCompanyUseCase;

  @Mock
  private CompanyRepository repository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(authCompanyUseCase, "secretKey", "test-secret-key");
  }

  @Test
  @DisplayName("Should authenticate company successfully")
  void should_authenticate_company_successfully() throws AuthenticationException {
    var companyId = UUID.randomUUID();
    var company = CompanyEntity.builder()
        .id(companyId)
        .username("testcompany")
        .password("encodedPassword")
        .build();

    var authRequest = new AuthCompanyDTO("testcompany", "password123");

    when(repository.findByUsername("testcompany")).thenReturn(Optional.of(company));
    when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

    var result = authCompanyUseCase.execute(authRequest);

    assertThat(result).isNotNull();
    assertThat(result.getAccessToken()).isNotNull();
    assertThat(result.getExpiresIn()).isGreaterThan(0);
  }

  @Test
  @DisplayName("Should throw exception when company not found")
  void should_throw_exception_when_company_not_found() {
    var authRequest = new AuthCompanyDTO("nonexistent", "password123");

    when(repository.findByUsername("nonexistent")).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> {
      authCompanyUseCase.execute(authRequest);
    });
  }

  @Test
  @DisplayName("Should throw exception when password is invalid")
  void should_throw_exception_when_password_is_invalid() {
    var company = CompanyEntity.builder()
        .username("testcompany")
        .password("encodedPassword")
        .build();

    var authRequest = new AuthCompanyDTO("testcompany", "wrongpassword");

    when(repository.findByUsername("testcompany")).thenReturn(Optional.of(company));
    when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

    assertThrows(AuthenticationException.class, () -> {
      authCompanyUseCase.execute(authRequest);
    });
  }
}
