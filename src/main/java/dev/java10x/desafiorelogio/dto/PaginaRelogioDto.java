package dev.java10x.desafiorelogio.dto;

import java.util.List;

/**
 * DTO de paginação.
 *
 * Usado para retornar listas paginadas de relógios.
 * Evita expor diretamente Page<T> do Spring para a API.
 */

public record PaginaRelogioDto(
        /**
         * Lista de relógios da página atual.
         */
        List<RelogioDto> itens,
        /**
         * Total de registros disponíveis (sem paginação).
         * Permite ao frontend calcular páginas, botões, etc.
         */
        long total
) {
}
