package br.sena.gestao_vagas.modules.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponseDTO {
  @Schema(example = "Vaga Java Fullstack Junior", description = "Descrição Vaga")
  private String description;
  @Schema(example = "Beneficios da vaga", description = "Benefícios da vaga")
  private String benefits;
  @Schema(example = "Junior, Pleno, Senior", description = "Nível da vaga")
  private String level;
  @Schema(example = "Empresa LTDA", description = "Nome da Empresa")
  private String companyName;
  @Schema(example = "empresa@ltda.com", description= "Email da empresa")
  private String companyEmail;
}
