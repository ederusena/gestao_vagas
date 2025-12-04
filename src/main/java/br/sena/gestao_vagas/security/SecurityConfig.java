package br.sena.gestao_vagas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {

  @Autowired
  private SecurityFilter filter;

  @Autowired
  private SecurityCandidateFilter candidateFilter;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> {
          auth.requestMatchers("/candidate/").permitAll()
              .requestMatchers("/candidate/auth").permitAll()
              .requestMatchers("/company/").permitAll()
              .requestMatchers("/company/auth").permitAll()
              .requestMatchers("/swagger-ui.html").permitAll()
              .requestMatchers("/swagger-ui/**").permitAll()
              .requestMatchers("/v3/api-docs/**").permitAll()
              .requestMatchers("/swagger-resources/**").permitAll()
              .requestMatchers("/webjars/**").permitAll();
          auth.anyRequest().authenticated();
        })
        .addFilterBefore(candidateFilter, BasicAuthenticationFilter.class)
        .addFilterBefore(filter, BasicAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
