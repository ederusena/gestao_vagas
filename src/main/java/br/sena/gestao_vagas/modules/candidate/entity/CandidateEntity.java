package br.sena.gestao_vagas.modules.candidate.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity(name = "candidates")
public class CandidateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  
  private String name;

  @Email(message = "Invalid email format")
  private String email;

  @Pattern(regexp = "\\S+", message = "Username cannot be blank")
  private String username;

  @Length(min = 6, max = 30, message = "Password must be at least 6 characters long")
  private String password;

  private String description;
  private String resume;
  
  @CreationTimestamp
  private LocalDateTime createdAt;

}
