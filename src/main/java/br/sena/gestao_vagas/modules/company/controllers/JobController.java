package br.sena.gestao_vagas.modules.company.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sena.gestao_vagas.exceptions.CompanyNotFoundException;
import br.sena.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.sena.gestao_vagas.modules.company.entities.JobEntity;
import br.sena.gestao_vagas.modules.company.useCases.CreateJobUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company")
@Tag(name = "Job Management System")
public class JobController {

  @Autowired
  private CreateJobUseCase createJobUseCase;

  @PostMapping("/job")
  @PreAuthorize("hasRole('COMPANY')")
  @Operation(summary = "Create a new Job")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Job created with success!", content = {
          @Content(schema = @Schema(implementation = JobEntity.class))
      }),
      @ApiResponse(responseCode = "400", description = "Bad request")
  })
  public ResponseEntity<Object> create(@Valid @RequestBody CreateJobDTO createJobDTO, HttpServletRequest request) {
    try {
      JobEntity job = JobEntity.builder()
          .benefits(createJobDTO.getBenefits())
          .description(createJobDTO.getDescription())
          .level(createJobDTO.getLevel())
          .companyId(UUID.fromString(request.getAttribute("company_id").toString()))
          .build();

      var result = this.createJobUseCase.execute(job);
      return ResponseEntity.ok().body(result);
    } catch (CompanyNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
