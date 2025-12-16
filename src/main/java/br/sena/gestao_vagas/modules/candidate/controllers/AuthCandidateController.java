package br.sena.gestao_vagas.modules.candidate.controllers;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sena.gestao_vagas.modules.candidate.dto.AuthRequestCandidateDTO;
import br.sena.gestao_vagas.modules.candidate.dto.AuthResponseCandidateDTO;
import br.sena.gestao_vagas.modules.candidate.useCases.AuthCandidateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/candidate")
@Tag(name = "Authentication Candidate")
public class AuthCandidateController {
  
  private final AuthCandidateUseCase authCandidateUseCase;

  public AuthCandidateController(AuthCandidateUseCase authCandidateUseCase) {
    this.authCandidateUseCase = authCandidateUseCase;
  }

  @PostMapping("/auth")
  @Operation(summary = "Authenticate a Candidate and generate JWT token")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Candidate authenticated successfully", content = {
          @Content(schema = @Schema(implementation = AuthResponseCandidateDTO.class))
      }),
      @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials"),
      @ApiResponse(responseCode = "400", description = "Bad request")
  })
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
