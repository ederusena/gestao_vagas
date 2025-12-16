package br.sena.gestao_vagas.modules.candidate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCandidateResponseDTO {
  @Schema(example = "1", description = "ID do candidato")
  private String id;
  @Schema(example = "johndoe", description = "Nome de usuário do candidato")
  private String username;
  @Schema(example = "John Doe", description = "Nome completo do candidato")
  private String name;
  @Schema(example = "email@email.com", description = "Email do candidato")
  private String email;
  @Schema(example = "Desenvolvedor Full Stack com 5 anos de experiência...", description = "Descrição do candidato")
  private String description;
}
