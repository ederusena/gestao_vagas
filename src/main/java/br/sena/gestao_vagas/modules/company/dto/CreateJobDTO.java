package br.sena.gestao_vagas.modules.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateJobDTO {

  @Schema(example = "Vaga Vue.js", description = "Descrição da vaga")
  private String description;

  @Schema(example = "Auxilio R$300,00, GYMPASS", description = "Benefícios da vaga")
  private String benefits;

  @Schema(example = "Junior, Pleno, Senior", description = "Nível da vaga")
  private String level;
}
