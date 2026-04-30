package com.io.github.wendellvalentim.mspedido.controller.common;

import com.io.github.wendellvalentim.mspedido.controller.dto.ErroResposta;
import com.io.github.wendellvalentim.mspedido.exception.EstoqueInsuficienteException;
import feign.FeignException;
import feign.RetryableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
        String url = e.request().url();;
        String nomeService = "Externo";

        if(url.contains("/produtos")) {
            nomeService = "Produtos";
        }
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErroResposta("Falha na comunicação: o microserviço de" + nomeService + "Esta fora do ar"));
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErroResposta> handleEstoqueInsuficienteException(EstoqueInsuficienteException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErroResposta(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> handleGeneralException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResposta("Ocorreu um erro interno inesperado no sistema."));
    }
}
