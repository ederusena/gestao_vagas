package br.sena.gestao_vagas.modules.candidate.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sena.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.sena.gestao_vagas.modules.candidate.useCases.CreateCandidateUseCase;
import br.sena.gestao_vagas.modules.candidate.useCases.ProfileCandidateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/candidate")
@Tag(name = "Candidate Management System")
public class CandidateController {

  @Autowired
  private CreateCandidateUseCase createCandidateUseCase;

  @Autowired
  private ProfileCandidateUseCase profileCandidateUseCase;

  @PostMapping("/")
  @Operation(summary = "Create a new candidate")
  public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidate) {
    try {
      var result = this.createCandidateUseCase.execute(candidate);
      return ResponseEntity.ok().body(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/")
  public ResponseEntity<Object> getProfile(HttpServletRequest request) {
    try {
      var idCandidate = (String) request.getAttribute("candidate_id");
      var candidateProfile = this.profileCandidateUseCase.execute(UUID.fromString(idCandidate));
      return ResponseEntity.ok().body(candidateProfile);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

  }

}
