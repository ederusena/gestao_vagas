package br.sena.gestao_vagas.modules.candidate.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sena.gestao_vagas.modules.candidate.entity.CandidateEntity;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/candidates")
public class CandidateController {
    

  @PostMapping("/")
  public void create(@Valid @RequestBody CandidateEntity candidate) {
    System.out.println("Candidate created: " + candidate.getName());
  }
}
