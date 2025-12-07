package br.sena.gestao_vagas.modules.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthCompanyDTO {
  @Schema(example = "zenkaipower", description = "Nome de usuário da empresa")
  private String username;
  @Schema(example = "senha123", description = "Senha da empresa")
  private String password;
}
