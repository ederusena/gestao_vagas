package br.sena.gestao_vagas.modules.candidate.useCases;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.sena.gestao_vagas.modules.candidate.dto.AuthRequestCandidateDTO;
import br.sena.gestao_vagas.modules.candidate.dto.AuthResponseCandidateDTO;
import br.sena.gestao_vagas.modules.candidate.repository.CandidateReposity;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthCandidateUseCase {

  @Value("${security.token.secret.candidate}")
  private String secretKey;

  @Autowired
  private CandidateReposity repository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public AuthResponseCandidateDTO execute(AuthRequestCandidateDTO authRequest) throws AuthenticationException {
    var candidate = repository
        .findByUsername(authRequest.username())
        .orElseThrow(() -> {
          log.error("Candidate not found with username: {}", authRequest.username());
          throw new UsernameNotFoundException("Username/password inválidos!");
        });
    var passwordMatches = this.passwordEncoder.matches(authRequest.password(), candidate.getPassword());

    if (!passwordMatches) {
      log.error("Invalid password for username: {}", authRequest.username());
      throw new AuthenticationException("Username/password inválidos!");
    }

    var expiredIn = Instant.now().plus(Duration.ofHours(2));

    Algorithm algorithm = Algorithm.HMAC256(secretKey);
    var token = JWT.create()
        .withIssuer("javagas")
        .withSubject(candidate.getId().toString())
        .withClaim("roles", Arrays.asList("candidate"))
        .withExpiresAt(expiredIn)
        .sign(algorithm);

    var response = AuthResponseCandidateDTO.builder()
        .access_token(token)
        .expires_in(expiredIn.toEpochMilli())
        .build();

    return response;
  }
}
