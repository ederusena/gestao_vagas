package br.sena.gestao_vagas.modules.company.controllers;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sena.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.sena.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.sena.gestao_vagas.modules.company.useCases.AuthCompanyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/company")
@Tag(name = "Authentication Company")
public class AuthCompanyController {

  @Autowired
  private AuthCompanyUseCase authCompanyUseCase;

  @PostMapping("/auth")
  @Operation(summary = "Authenticate a Company and generate JWT token")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Company authenticated successfully", content = {
          @Content(schema = @Schema(implementation = AuthCompanyResponseDTO.class))
      }),
      @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials"),
      @ApiResponse(responseCode = "400", description = "Bad request")
  })
  public ResponseEntity<Object> create(@RequestBody AuthCompanyDTO authCompanyDTO) {
    try {
      var token = this.authCompanyUseCase.execute(authCompanyDTO);
      return ResponseEntity.ok().body(token);
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
  
}
