package br.sena.gestao_vagas.modules.company.useCases;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.sena.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.sena.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class AuthCompanyUseCase {
  
  @Autowired
  private CompanyRepository repository;

  @Autowired
  private PasswordEncoder passwordEncoder;
  
  public void execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException {
    var company = this.repository
        .findByUsername(authCompanyDTO.getUsername())
        .orElseThrow(() -> {
          throw new UsernameNotFoundException("Empresa não encontrada!");
        });

    // Verificar a senha aqui (a implementação depende de como as senhas são armazenadas e verificadas)
    var passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());
    // senao for igual -> .erro
    if (!passwordMatches) {
      throw new AuthenticationException("Senha inválida!");
    }

    // Se for igual -> autenticar gerar token JWT
    
  }
}
