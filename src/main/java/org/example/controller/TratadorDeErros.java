package org.example.controller;

import org.example.exception.RegraNegocioException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class TratadorDeErros {

    // 1. Intercepta erros de DTO (ex: CPF inválido, campo em branco)
    @ExceptionHandler(MethodArgumentNotValidException.class) // <-- Agora só tem um!
    public ResponseEntity<Map<String, String>> tratarErroDeValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erroLimpo = new HashMap<>();

        // Pega o primeiro erro que aconteceu e coloca no mapa
        String mensagemDeErro = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        erroLimpo.put("erro", mensagemDeErro);

        return ResponseEntity.badRequest().body(erroLimpo);
    }

    // 2. 🚀 NOVO: Intercepta os erros de lógica que nós mesmos lançarmos pelo Service
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, String>> tratarRegraDeNegocio(RegraNegocioException ex) {
        Map<String, String> erroLimpo = new HashMap<>();

        // Pega exatamente a mensagem que você enviou no "new RegraNegocioException("...")"
        erroLimpo.put("erro", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroLimpo);
    }

    // 3. 🚀 NOVO: Intercepta erros de integridade de banco de dados (CPF/Email duplicado)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> tratarDuplicata(DataIntegrityViolationException ex) {
        Map<String, String> erroLimpo = new HashMap<>();
        String mensagem = ex.getMostSpecificCause().getMessage();

        if (mensagem.contains("CPF")) {
            erroLimpo.put("erro", "Este CPF já está cadastrado no sistema.");
        } else if (mensagem.contains("EMAIL")) {
            erroLimpo.put("erro", "Este e-mail já está cadastrado no sistema.");
        } else if (mensagem.contains("REGISTRO_PROFISSIONAL")) {
            erroLimpo.put("erro", "Este número de registro profissional já está cadastrado.");
        } else {
            erroLimpo.put("erro", "Dados já existem no sistema. Verifique CPF, e-mail ou registro profissional.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroLimpo);
    }
}