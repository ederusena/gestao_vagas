package br.sena.gestao_vagas.modules.candidate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sena.gestao_vagas.modules.candidate.entity.CandidateEntity;
import br.sena.gestao_vagas.modules.candidate.repository.CandidateReposity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/candidates")
@Tag(name = "Candidate Management System")
public class CandidateController {

  @Autowired
  private CandidateReposity repository;

  @PostMapping("/")
  @Operation(summary = "Create a new candidate")
  public CandidateEntity create(@Valid @RequestBody CandidateEntity candidate) {
    if (candidate == null) {
      throw new IllegalArgumentException("Candidate data must be provided");
    }

    return this.repository.save(candidate);
  }
}
