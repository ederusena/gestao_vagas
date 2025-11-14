package br.sena.gestao_vagas.exceptions;

public class EmailFoundException extends RuntimeException {
  public EmailFoundException() {
    super("Email já existe: ");
  }
  
}
