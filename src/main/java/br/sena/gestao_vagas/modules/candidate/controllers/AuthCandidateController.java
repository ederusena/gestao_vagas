package br.sena.gestao_vagas.modules.candidate.controllers;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sena.gestao_vagas.modules.candidate.dto.AuthRequestCandidateDTO;
import br.sena.gestao_vagas.modules.candidate.useCases.AuthCandidateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/candidate")
@Tag(name = "Authentication Candidate")
public class AuthCandidateController {
  
  @Autowired
  private AuthCandidateUseCase authCandidateUseCase;

  @PostMapping("/auth")
  @Operation(summary = "Authenticate a Candidate and generate JWT token")
  public ResponseEntity<Object> create(@RequestBody AuthRequestCandidateDTO candidateDTO) {
      try {
      var token = this.authCandidateUseCase.execute(candidateDTO);
      return ResponseEntity.ok().body(token);
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
