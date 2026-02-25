package dev.java10x.desafiorelogio.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
/**
 * DTO de entrada para criação de um novo Relógio.
 *
 * - Representa exatamente os dados que o cliente pode enviar
 * - Contém validações de entrada (Bean Validation)
 * - Não contém id nem campos automáticos (ex: criadoEm)
 */

public record CirarRelogioResquest(
        @NotBlank @Size( max = 80) String marca,
        @NotBlank @Size( max = 120) String modelo,
        @NotBlank @Size( max = 80) String referencia,
        /**
         * Tipo de movimento recebido como String (ex: "quartz").
         * Será convertido para enum na camada de serviço/mapper.
         */
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
