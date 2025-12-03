package br.sena.gestao_vagas.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.sena.gestao_vagas.providers.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

  @Autowired
  private JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    SecurityContextHolder.getContext().setAuthentication(null);
    String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      var subjectToken = jwtProvider.validateToken(header);

      if (!subjectToken.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }

      request.setAttribute("company_id", subjectToken);
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(subjectToken, null,
          Collections.emptyList());

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
