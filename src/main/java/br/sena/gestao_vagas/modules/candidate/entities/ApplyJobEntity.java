package br.sena.gestao_vagas.modules.candidate.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import br.sena.gestao_vagas.modules.company.entities.JobEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "apply_jobs")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyJobEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "candidate_id", insertable = false, updatable = false)
  private CandidateEntity candidateEntity;

  @ManyToOne
  @JoinColumn(name = "job_id", insertable = false, updatable = false)
  private JobEntity jobEntity;

  @Column(name = "candidate_id")
  private UUID candidateId;

  @Column(name = "job_id")
  private UUID jobId;

  @CreationTimestamp
  @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Data de criação")
  private LocalDateTime createdAt;
}
