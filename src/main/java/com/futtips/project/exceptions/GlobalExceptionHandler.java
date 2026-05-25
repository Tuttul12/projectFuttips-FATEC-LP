package com.futtips.project.exceptions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.futtips.project.responses.ApiResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> tratarErroValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(erro ->
            erros.put(erro.getField(), erro.getDefaultMessage())
        );

        return ResponseEntity
            .badRequest()
            .body(ApiResponse.erro("Erro de validação nos campos enviados.", erros));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> tratarConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> erros = new LinkedHashMap<>();

        ex.getConstraintViolations().forEach(erro ->
            erros.put(erro.getPropertyPath().toString(), erro.getMessage())
        );

        return ResponseEntity
            .badRequest()
            .body(ApiResponse.erro("Erro de validação nos dados informados.", erros));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> tratarJsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity
            .badRequest()
            .body(ApiResponse.erro("JSON inválido ou campo com formato incorreto.", Map.of("erro", ex.getMostSpecificCause().getMessage())));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> tratarErroBanco(DataIntegrityViolationException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ApiResponse.erro("Erro de integridade no banco de dados.", Map.of("erro", "Verifique se já existe registro com dados únicos, como CPF ou e-mail, ou se as chaves relacionadas existem.")));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> tratarRuntime(RuntimeException ex) {
        return ResponseEntity
            .badRequest()
            .body(ApiResponse.erro(ex.getMessage(), Map.of("erro", ex.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> tratarErroInesperado(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.erro("Erro interno inesperado.", Map.of("erro", ex.getMessage())));
    }
}
