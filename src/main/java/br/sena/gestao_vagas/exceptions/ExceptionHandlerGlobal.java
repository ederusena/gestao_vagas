package br.sena.gestao_vagas.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerGlobal {
  
  private final MessageSource messageSource;

  public ExceptionHandlerGlobal(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @SuppressWarnings("null")
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ErrorMessageDTO>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    List<ErrorMessageDTO> dto = new ArrayList<>();

    ex.getBindingResult().getFieldErrors().forEach(error -> {
      String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
      ErrorMessageDTO errorMessageDto = new ErrorMessageDTO(message, error.getField());
      dto.add(errorMessageDto);
    });

    return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
  }
}
