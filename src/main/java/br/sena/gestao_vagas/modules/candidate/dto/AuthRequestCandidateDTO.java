package br.sena.gestao_vagas.modules.candidate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestCandidateDTO {

  @Schema(example = "ederusena", description = "Nome de usuário do candidato", requiredMode = Schema.RequiredMode.REQUIRED)
  String username;
  @Schema(example = "senha123", description = "Senha do candidato", requiredMode = Schema.RequiredMode.REQUIRED)
  String password;

}
