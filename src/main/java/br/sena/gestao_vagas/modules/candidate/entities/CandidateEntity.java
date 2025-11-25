package br.sena.gestao_vagas.modules.candidate.entities;

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
@Entity(name = "candidate")
public class CandidateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "ID gerado automaticamente")
  private UUID id;
  
  @Schema(example = "João Silva", description = "Nome do candidato")
  private String name;

  @Email(message = "Invalid email format")
  @Column(unique = true)
  @Schema(example = "joao@email.com", description = "E-mail do candidato")
  private String email;

  @Pattern(regexp = "\\S+", message = "Username cannot be blank")
  @Column(unique = true)
  @Schema(example = "joaosilva", description = "Username (sem espaços)")
  private String username;

  @Length(min = 6, max = 30, message = "Password must be at least 6 characters long")
  @Column(length = 100)
  @Schema(example = "senha123", minLength = 6, maxLength = 30, description = "Senha (mínimo 6 caracteres)")
  private String password;

  @Schema(example = "Desenvolvedor Java com 5 anos de experiência", description = "Descrição profissional")
  private String description;
  
  @Schema(example = "Experiência em Spring Boot, Hibernate, PostgreSQL", description = "Resumo do currículo")
  private String resume;
  
  @CreationTimestamp
  @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Data de criação")
  private LocalDateTime createdAt;

}
