package br.sena.gestao_vagas.modules.company.controllers;

import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sena.gestao_vagas.modules.company.entities.CompanyEntity;
import br.sena.gestao_vagas.modules.company.useCases.CreateCompanyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company")
@Tag(name = "Company Management System")
@Slf4j
public class CompanyController {

  private final CreateCompanyUseCase createCompanyUseCase;

  public CompanyController(CreateCompanyUseCase createCompanyUseCase) {
    this.createCompanyUseCase = createCompanyUseCase;
  }

  @PostMapping("/")
  @Operation(summary = "Create a new company")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Company created successfully", content = {
          @Content(schema = @Schema(implementation = CompanyEntity.class))
      }),
      @ApiResponse(responseCode = "400", description = "Bad request")
  })
  public ResponseEntity<Object> create(@Valid @RequestBody CompanyEntity company) {
    try {
      var result = this.createCompanyUseCase.execute(company);
      return ResponseEntity.ok().body(result);
    } catch (Exception e) {
      log.error("Error creating company: {}", e.getMessage(), e);
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
