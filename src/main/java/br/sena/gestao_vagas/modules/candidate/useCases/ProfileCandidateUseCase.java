package br.sena.gestao_vagas.modules.candidate.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.sena.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.sena.gestao_vagas.modules.candidate.repository.CandidateReposity;

@Service
public class ProfileCandidateUseCase {
  
  @Autowired
  private CandidateReposity repository;
  
  public ProfileCandidateResponseDTO execute(UUID id) {

    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }
    
    var candidate = this.repository.findById(id)
    .orElseThrow(() -> {
      throw new UsernameNotFoundException("Candidate not found");
    });

    var candidateDTO = ProfileCandidateResponseDTO.builder()
      .id(candidate.getId().toString())
      .username(candidate.getUsername())
      .name(candidate.getName())
      .email(candidate.getEmail())
      .description(candidate.getDescription())
      .build();

    return candidateDTO;
  }
}
