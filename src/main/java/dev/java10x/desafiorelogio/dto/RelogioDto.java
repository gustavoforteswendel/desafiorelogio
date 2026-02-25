package dev.java10x.desafiorelogio.dto;

import lombok.Builder;

import java.util.UUID;
/**
 * DTO de saída da API.
 *
 * Representa um Relógio no formato exposto para o cliente (frontend / consumidor da API).
 * - Não é entidade JPA
 * - Não possui regras de negócio
 * - Pode conter campos derivados ou formatados (strings, labels, etc.)
 *
 * Usar DTO evita expor diretamente a Entity e desacopla a API do banco.
 */

@Builder

public record RelogioDto(
        UUID id,
        String marca,
        String modelo,
        String referencia,
        String tipoMovimento,
        String materialCaixa,
        String tipoVidro,
        int resistenciaAguaM,
        int diametroMm,
        int lugToLugMm,
        int espessuraMm,
        int larguraLugMm,
        long precoEmCentavos,
        String urlImagem,
        String etiquetaResistenciaAgua,
        int pontuacaoColecionador
) {
}
