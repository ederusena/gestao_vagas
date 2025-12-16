package br.sena.gestao_vagas.modules.company.useCases;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.sena.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.sena.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.sena.gestao_vagas.modules.company.repositories.CompanyRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthCompanyUseCase {
  
  @Value("${security.token.secret}")
  private String secretKey;

  private final CompanyRepository repository;
  private final PasswordEncoder passwordEncoder;

  public AuthCompanyUseCase(CompanyRepository repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }
  
  public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException {
    log.info("authCompanyDTO found: {}", authCompanyDTO.toString());
    var company = this.repository
        .findByUsername(authCompanyDTO.getUsername())
        .orElseThrow(() -> {
          throw new UsernameNotFoundException("Username/password inválidos!");
        });
    log.info("Company found: {}", company.getUsername());
    // Verificar a senha aqui (a implementação depende de como as senhas são armazenadas e verificadas)
    var passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());
    log.info("Password matches: {}", passwordMatches);
    // senao for igual -> .erro
    if (!passwordMatches) {
      throw new AuthenticationException("Senha inválida!");
    }

    // Se for igual -> autenticar gerar token JWT
    var expires_in = Instant.now().plus(Duration.ofHours(4));
    Algorithm algorithm = Algorithm.HMAC256(secretKey);
    var token = JWT.create().withIssuer("javagas")
      .withExpiresAt(expires_in)
      .withSubject(company.getId().toString())
      .withClaim("roles", Arrays.asList("COMPANY"))
      .sign(algorithm);

    var response = AuthCompanyResponseDTO.builder()
        .access_token(token)
        .expires_in(expires_in.toEpochMilli())
        .build();

    return response;
  }
}
