package br.sena.gestao_vagas.modules.candidate.controllers;

import java.lang.reflect.Array;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sena.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.sena.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.sena.gestao_vagas.modules.candidate.useCases.AllJobsByFilterUseCase;
import br.sena.gestao_vagas.modules.candidate.useCases.ApplyJobCandidateUseCase;
import br.sena.gestao_vagas.modules.candidate.useCases.CreateCandidateUseCase;
import br.sena.gestao_vagas.modules.candidate.useCases.ProfileCandidateUseCase;
import br.sena.gestao_vagas.modules.company.dto.JobResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/candidate")
@Tag(name = "Candidate Management System")
public class CandidateController {

  private final CreateCandidateUseCase createCandidateUseCase;
  private final ProfileCandidateUseCase profileCandidateUseCase;
  private final AllJobsByFilterUseCase allJobsByFilterUseCase;
  private final ApplyJobCandidateUseCase applyJobCandidateUseCase;

  public CandidateController(CreateCandidateUseCase createCandidateUseCase,
                             ProfileCandidateUseCase profileCandidateUseCase,
                             AllJobsByFilterUseCase allJobsByFilterUseCase,
                             ApplyJobCandidateUseCase applyJobCandidateUseCase) {
    this.createCandidateUseCase = createCandidateUseCase;
    this.profileCandidateUseCase = profileCandidateUseCase;
    this.allJobsByFilterUseCase = allJobsByFilterUseCase;
    this.applyJobCandidateUseCase = applyJobCandidateUseCase;
  }

  @PostMapping("/")
  @Operation(summary = "Create a new candidate")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Candidate created with success!", content = {
          @Content(schema = @Schema(implementation = CandidateEntity.class))
      }),
      @ApiResponse(responseCode = "400", description = "Bad request")
  })
  public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidate) {
    try {
      var result = this.createCandidateUseCase.execute(candidate);
      return ResponseEntity.ok().body(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/")
  @PreAuthorize("hasRole('CANDIDATE')")
  @Operation(summary = "Get candidate profile")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved profile", content = {
          @Content(schema = @Schema(implementation = ProfileCandidateResponseDTO.class))
      }),
      @ApiResponse(responseCode = "400", description = "Bad request")
  })
  public ResponseEntity<Object> getProfile(HttpServletRequest request) {
    try {
      var idCandidate = (String) request.getAttribute("candidate_id");
      var candidateProfile = this.profileCandidateUseCase.execute(UUID.fromString(idCandidate));
      return ResponseEntity.ok().body(candidateProfile);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/jobs")
  @Operation(summary = "Get all jobs by filter")
  @PreAuthorize("hasRole('CANDIDATE')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved list", content = {
          @Content(array = @ArraySchema(schema = @Schema(implementation = JobResponseDTO.class)))
      }),
      @ApiResponse(responseCode = "400", description = "Bad request",content= {
          @Content(array = @ArraySchema(schema = @Schema(implementation = Array.class)))
      })
  })
  public List<JobResponseDTO> getAllJobsByFilter(String filter) {
    try {
      return this.allJobsByFilterUseCase.execute(filter);
    } catch (Exception e) {
      return null;
    }

  }

  @PostMapping("/job/apply")
  @PreAuthorize("hasRole('CANDIDATE')")
  @Operation(summary = "Apply for a job")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully applied for the job", content = {
          @Content(schema = @Schema(implementation = Object.class))
      }),
      @ApiResponse(responseCode = "400", description = "Bad request")
  })
  public ResponseEntity<Object> applyJob(HttpServletRequest request, @RequestBody UUID jobId) {
    
    try {
      var idCandidate = (String) request.getAttribute("candidate_id");
      var result = this.applyJobCandidateUseCase.execute(
          UUID.fromString(idCandidate),
          jobId);
      return ResponseEntity.ok().body(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
