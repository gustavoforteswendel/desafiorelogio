package dev.java10x.desafiorelogio.mapper;

import dev.java10x.desafiorelogio.dto.RelogioDto;
import dev.java10x.desafiorelogio.entity.Relogio;
import dev.java10x.desafiorelogio.entity.enums.MaterialCaixa;
import dev.java10x.desafiorelogio.entity.enums.TipoMovimento;
import dev.java10x.desafiorelogio.entity.enums.TipoVidro;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável por converter a entidade Relogio
 * para o DTO exposto pela API (RelogioDto).
 *
 * - Centraliza toda lógica de conversão Entity -> DTO
 * - Evita que Controller ou Service tenham lógica de formatação
 * - Pode conter regras de apresentação (labels, pontuações, campos derivados)
 */

@Component

public class RelogioMapper {
    /**
     * Converte uma entidade Relogio em RelogioDto.
     *
     * Aqui ocorre:
     * - Conversão de enums para valores de API (String)
     * - Cálculo de campos derivados (etiquetaResistenciaAgua, pontuacaoColecionador)
     * - Montagem do DTO usando Builder
     */

    public RelogioDto toDto (Relogio r) {
        return RelogioDto.builder()
                .id(r.getId())
                .marca(r.getMarca())
                .referencia(r.getReferencia())
                // Enum do domínio convertido para String da API
                .tipoMovimento(r.getTipoMovimento().toApi())
                .materialCaixa(r.getMaterialCaixa().toApi())
                .tipoVidro(r.getTipoVidro().toApi())
                // Campos numéricos simples
                .resistenciaAguaM(r.getResistenciaAguaM())
                .diametroMm(r.getDiametroMm())
                .lugToLugMm(r.getLugToLugMm())
                .espessuraMm(r.getEspessuraMm())
                .larguraLugMm(r.getLarguraLugMm())
                .precoEmCentavos(r.getPrecoEmCentavos())
                // Campos derivados (não existem na entidade)
                .urlImagem(r.getUrlImagem())
                .etiquetaResistenciaAgua(etiquetaResistencia(r.getResistenciaAguaM()))
                .pontuacaoColecionador(pontuacaoColecionador(r))
                .build();


    }

    /**
     * Gera uma etiqueta textual baseada na resistência à água.
     *
     * Essa lógica é de "apresentação" e não de persistência,
     * por isso fica no Mapper e não na Entity.
     */
    private String etiquetaResistencia(int resistenciaM){
        if (resistenciaM < 50) return "respingos";
        if (resistenciaM < 100) return "uso_diario";
        if (resistenciaM < 200) return "natacao";
        return "mergulho";
    }
    /**
     * Calcula uma pontuação de colecionador com base
     * em características técnicas do relógio.
     *
     * Exemplo de regra derivada:
     * - Vidro safira vale mais pontos
     * - Maior resistência à água aumenta pontuação
     * - Movimento automático é mais valorizado
     * - Materiais nobres aumentam score
     * - Tamanho de caixa dentro de um range ideal soma pontos
     *
     * Essa regra não pertence à Entity porque:
     * - Não é persistida
     * - Pode mudar conforme critérios da aplicação
     */

    private int pontuacaoColecionador(Relogio r) {
        int pontos = 0;
        if (r.getTipoVidro() == TipoVidro.SAFIRA) pontos += 25;
        if (r.getResistenciaAguaM() >= 100) pontos += 15;
        if (r.getResistenciaAguaM() >= 200) pontos += 10;

        if (r.getTipoMovimento() == TipoMovimento.AUTOMATICO) pontos += 20;

        if (r.getMaterialCaixa() == MaterialCaixa.ACO) pontos += 12;
        if (r.getMaterialCaixa() == MaterialCaixa.TITANIO) pontos += 12;

        if (r.getDiametroMm() >= 38 && r.getDiametroMm() <= 42) pontos += 8;
        return pontos;


    }

}
