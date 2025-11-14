/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package br.sena.gestao_vagas.modules.company.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 *
 * @author eders
 */
@Data
@Entity(name = "job")
public class JobEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "ID gerado automaticamente")
  private UUID id;

  @Schema(example = "Vaga Java Fullstack Junior", description = "Descrição Vaga")
  private String description;

  @Schema(example = "Beneficios da vaga", description = "Benefícios da vaga")
  private String benefits;

  @Schema(example = "Junior, Pleno, Senior", description = "Nível da vaga")
  private String level;

  @ManyToOne
  @JoinColumn(name = "company_id", insertable = false, updatable = false)
  private CompanyEntity company;

  @Column(name = "company_id")
  private UUID companyId;

  @CreationTimestamp
  @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Data de criação")
  private LocalDateTime createdAt;
}
