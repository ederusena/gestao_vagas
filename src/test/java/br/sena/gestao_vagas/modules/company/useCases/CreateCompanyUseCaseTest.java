package br.sena.gestao_vagas.modules.company.useCases;

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
import br.sena.gestao_vagas.modules.company.entities.CompanyEntity;
import br.sena.gestao_vagas.modules.company.repositories.CompanyRepository;

@ExtendWith(MockitoExtension.class)
class CreateCompanyUseCaseTest {

  @InjectMocks
  private CreateCompanyUseCase createCompanyUseCase;

  @Mock
  private CompanyRepository repository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("Should create a company successfully")
  void should_create_company_successfully() {
    var company = CompanyEntity.builder()
        .username("testcompany")
        .email("company@email.com")
        .password("password123")
        .name("Test Company")
        .description("Test Description")
        .build();

    when(repository.findByUsername("testcompany")).thenReturn(Optional.empty());
    when(repository.findByEmail("company@email.com")).thenReturn(Optional.empty());
    when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
    when(repository.save(any(CompanyEntity.class))).thenReturn(company);

    var result = createCompanyUseCase.execute(company);

    assertThat(result).isNotNull();
    assertThat(result.getPassword()).isEqualTo("encodedPassword");
    verify(repository).save(company);
  }

  @Test
  @DisplayName("Should throw exception when username already exists")
  void should_throw_exception_when_username_exists() {
    var company = CompanyEntity.builder()
        .username("existingcompany")
        .email("company@email.com")
        .password("password123")
        .build();

    var existingCompany = new CompanyEntity();
    when(repository.findByUsername("existingcompany")).thenReturn(Optional.of(existingCompany));

    assertThrows(UserFoundException.class, () -> {
      createCompanyUseCase.execute(company);
    });

    verify(repository, never()).save(any(CompanyEntity.class));
  }

  @Test
  @DisplayName("Should throw exception when email already exists")
  void should_throw_exception_when_email_exists() {
    var company = CompanyEntity.builder()
        .username("newcompany")
        .email("existing@email.com")
        .password("password123")
        .build();

    var existingCompany = new CompanyEntity();
    when(repository.findByUsername("newcompany")).thenReturn(Optional.empty());
    when(repository.findByEmail("existing@email.com")).thenReturn(Optional.of(existingCompany));

    assertThrows(EmailFoundException.class, () -> {
      createCompanyUseCase.execute(company);
    });

    verify(repository, never()).save(any(CompanyEntity.class));
  }

  @Test
  @DisplayName("Should throw exception when password is null")
  void should_throw_exception_when_password_is_null() {
    var company = CompanyEntity.builder()
        .username("testcompany")
        .email("company@email.com")
        .password(null)
        .build();

    when(repository.findByUsername("testcompany")).thenReturn(Optional.empty());
    when(repository.findByEmail("company@email.com")).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> {
      createCompanyUseCase.execute(company);
    });

    verify(repository, never()).save(any(CompanyEntity.class));
  }

  @Test
  @DisplayName("Should throw exception when password is too short")
  void should_throw_exception_when_password_is_too_short() {
    var company = CompanyEntity.builder()
        .username("testcompany")
        .email("company@email.com")
        .password("12345")
        .build();

    when(repository.findByUsername("testcompany")).thenReturn(Optional.empty());
    when(repository.findByEmail("company@email.com")).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> {
      createCompanyUseCase.execute(company);
    });

    verify(repository, never()).save(any(CompanyEntity.class));
  }
}
