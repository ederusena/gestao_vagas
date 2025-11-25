package br.sena.gestao_vagas.modules.company.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity(name = "company")
public class CompanyEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "ID gerado automaticamente")
  private UUID id;

  @Pattern(regexp = "\\S+", message = "Username cannot be blank")
  @Column(unique = true)
  @Schema(example = "Empresa LDTA", description = "Username (sem espaços)")
  private String username;

  @Email(message = "Invalid email format")
  @Column(unique = true)
  @Schema(example = "empresa@email.com", description = "E-mail da empresa")
  private String email;

  @Column(length = 100)
  @Schema(example = "senha123", description = "Senha")
  private String password;

  @Schema(example = "Empresa LTDA", description = "Nome da Empresa")
  private String name;

  @Schema(example = "Empresa do Ramo de tecnologia.", description = "Descrição da empresa")
  private String description;

  @CreationTimestamp
  @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Data de criação")
  private LocalDateTime createdAt;
}
