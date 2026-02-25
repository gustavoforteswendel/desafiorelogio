package dev.java10x.desafiorelogio.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
/**
 * DTO de entrada para atualização de um Relógio existente.
 *
 * Estrutura semelhante ao CriarRelogioRequest, mas usado em operações de UPDATE.
 * O id normalmente vem pela URL (ex: PUT /relogios/{id}).
 */

public record AtualizarRelogioRequest(
        @NotBlank @Size( max = 80) String marca,
        @NotBlank @Size( max = 120) String modelo,
        @NotBlank @Size( max = 80) String referencia,

        @NotBlank String tipoMovimento,
        @NotBlank String materialCaixa,
        @NotBlank String tipoVidro,

        @Min(0) int resistenciaAguaM,
        @Min(20) int diametroMm,
        @Min(20) int lugToLugMm,
        @Min(5) int espessuraMm,
        @Min(10) int larguraLugMm,
        @Min(1) long precoEmCentavos,
        @NotNull @Size(max = 600) String urlImagem

) {
}
