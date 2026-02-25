package dev.java10x.desafiorelogio.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
/**
 * Classe responsável por tratar exceções globalmente.
 *
 * Papel arquitetural:
 * - Interceptar exceções lançadas na aplicação
 * - Traduzir exceções em respostas HTTP padronizadas
 * - Garantir consistência no formato de erro (ErroApi)
 *
 * Substitui completamente o erro padrão do Spring.
 */

@RestControllerAdvice
public class TratadorGlobalException {
    /**
     * Trata exceções de recurso não encontrado.
     *
     * Retorna HTTP 404.
     */


    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<ErroApi> tratarNaoEncontrado(NaoEncontradoException ex, HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroApi(
                Instant.now(), 404, "Não encontrado", ex.getMessage(), req.getRequestURI(), List.of()
        ));
    }
    /**
     * Trata erros de validação do Bean Validation (@Valid).
     *
     * Retorna HTTP 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroApi> tratarRequisicaoInvalida(Exception ex, HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroApi(
                Instant.now(), 400, "Requisição inválida", ex.getMessage(), req.getRequestURI(), List.of()
        ));

    }
    /**
     * Fallback para erros inesperados.
     *
     * Evita vazar stacktrace para o cliente.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroApi> tratarValidacao(MethodArgumentNotValidException ex, HttpServletRequest req){
        List<ErroApi.ErroCampo> campos = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErroApi.ErroCampo(fieldError.getField(), ex.getMessage()))
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroApi(
                Instant.now(),400,"Requisição inválida", "Erro de validação", req.getRequestURI(), campos
        ));
    }

}
