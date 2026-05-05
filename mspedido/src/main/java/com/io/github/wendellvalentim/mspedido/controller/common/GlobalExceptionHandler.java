package com.io.github.wendellvalentim.mspedido.controller.common;

import com.io.github.wendellvalentim.mspedido.controller.dto.ErroCampo;
import com.io.github.wendellvalentim.mspedido.controller.dto.ErroResposta;
import com.io.github.wendellvalentim.mspedido.exception.EstoqueInsuficienteException;
import feign.FeignException;
import feign.RetryableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleMethodArgumentException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrorList = e.getFieldErrors();
        List<ErroCampo> listErros = fieldErrorList.stream()
                .map(fe -> new ErroCampo(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return new ErroResposta(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro validação!", listErros);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeingException(FeignException e) {
        int status = e.status();

        String corpoErro = e.contentUTF8();

        if(corpoErro == null || corpoErro.isEmpty()) {
            corpoErro = "{\"mensagem\": \"Erro inesperado no serviço externo\"}";
        }

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(corpoErro);
    }

    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<ErroResposta> handleRetryableException(RetryableException e) {
        String url = e.request().url();
        String nomeService = "Externo";

        if(url.contains("/produtos")) {
            nomeService = "Produtos";
        }
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErroResposta(HttpStatus.SERVICE_UNAVAILABLE.value(),"Falha na comunicação: o microserviço de" + nomeService + "Esta fora do ar", List.of()));
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErroResposta> handleEstoqueInsuficienteException(EstoqueInsuficienteException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErroResposta(HttpStatus.BAD_REQUEST.value(),e.getMessage(), List.of()));
    }


}
