package com.io.github.wendellvalentim.msproduto.controller.common;

import com.io.github.wendellvalentim.msproduto.controller.dto.ErroCampo;
import com.io.github.wendellvalentim.msproduto.controller.dto.ErroResponse;
import com.io.github.wendellvalentim.msproduto.exceptions.CodProdExists;
import com.io.github.wendellvalentim.msproduto.exceptions.EstoqueInsuficienteException;
import com.io.github.wendellvalentim.msproduto.exceptions.ProdutoNaoEncontradoException;
import org.springframework.http.HttpStatus;
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
    public ErroResponse handleMethodArgumentException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ErroCampo> listErros = fieldErrors.stream()
                .map(fe -> new ErroCampo(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return new ErroResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação!", listErros);
    }

    @ExceptionHandler(CodProdExists.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse handleCodProdExists (CodProdExists e) {
        return  new ErroResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse handleCEstoqueInsuficienteException(EstoqueInsuficienteException e) {
        return  new ErroResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of());
    }



    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResponse handleUsuarioNaoEncontradoException(ProdutoNaoEncontradoException e) {
        return new ErroResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), List.of());
    }
}
