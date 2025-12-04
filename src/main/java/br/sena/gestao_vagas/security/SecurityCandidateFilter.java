package br.sena.gestao_vagas.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.sena.gestao_vagas.providers.JWTCandidateProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityCandidateFilter extends OncePerRequestFilter {

  @Autowired
  private JWTCandidateProvider jwtCandidateProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // SecurityContextHolder.getContext().setAuthentication(null);

    if (request.getRequestURI().startsWith("/candidate")) {
      var header = request.getHeader("Authorization");

      if (header != null && header.startsWith("Bearer ")) {
        var candidateToken = this.jwtCandidateProvider.validateToken(header);

        if (candidateToken == null) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return;
        }

        request.setAttribute("candidate_id", candidateToken.getSubject());
        var roles = candidateToken.getClaim("roles").asList(String.class);
        var grants = roles.stream()
            .map(
                role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
            .toList();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(candidateToken.getSubject(),
            null,
            grants);

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    filterChain.doFilter(request, response);
  }

}
