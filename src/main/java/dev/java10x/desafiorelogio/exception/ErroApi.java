package dev.java10x.desafiorelogio.exception;

import java.time.Instant;
import java.util.List;
/**
 * Estrutura padrão de erro da API.
 *
 * Objetivo:
 * Padronizar todas as respostas de erro retornadas ao cliente.
 *
 * Esse record será utilizado pelo @ControllerAdvice
 * para transformar exceções em JSON estruturado.
 *
 * Exemplo de retorno:
 *
 * {
 *   "timestamp": "...",
 *   "status": 404,
 *   "erro": "Not Found",
 *   "mensagem": "Relógio não encontrado: ...",
 *   "caminho": "/api/relogios/...",
 *   "errosDeCampo": []
 * }
 */

public record ErroApi(

        /**
         * Momento exato em que o erro ocorreu.
         * Boa prática para rastreamento e logs.
         */
        Instant timestamp,

        /**
         * Código HTTP (ex: 404, 400, 500).
         */
        int status,

        /**
         * Nome padrão do erro HTTP.
         * Ex: "Not Found", "Bad Request".
         */
        String erro,
        /**
         * Mensagem detalhada da exceção.
         */
        String mensagem,

        /**
         * Endpoint que gerou o erro.
         */
        String caminho,

        /**
         * Lista de erros específicos de validação.
         * Usado principalmente para Bean Validation.
         */
        List<ErroCampo> errosDeCampo
) {
    /**
     * Representa erro específico de um campo.
     *
     * Exemplo:
     * {
     *   "campo": "marca",
     *   "mensagem": "não pode ser vazio"
     * }
     */
    public record ErroCampo(String campo, String mensagem){}
}
