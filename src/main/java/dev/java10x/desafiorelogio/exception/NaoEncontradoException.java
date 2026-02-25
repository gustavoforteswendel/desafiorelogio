package dev.java10x.desafiorelogio.exception;
/**
 * Exceção de domínio utilizada quando um recurso
 * não é encontrado no banco de dados.
 *
 * Papel arquitetural:
 * - Representar erro 404 (Not Found)
 * - Evitar retornar null
 * - Centralizar semântica de erro de ausência
 *
 * É uma RuntimeException para evitar
 * obrigatoriedade de try/catch na camada Service.
 */

public class NaoEncontradoException extends RuntimeException{
    /**
     * Construtor que recebe mensagem personalizada.
     *
     * A mensagem normalmente contém:
     * - Tipo do recurso
     * - Identificador buscado
     *
     * Exemplo:
     * "Relógio não encontrado: 123e4567..."
     */
    public NaoEncontradoException(String msg){
        super(msg);
    }
}
