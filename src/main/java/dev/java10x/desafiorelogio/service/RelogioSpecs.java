package dev.java10x.desafiorelogio.service;

import dev.java10x.desafiorelogio.entity.Relogio;
import dev.java10x.desafiorelogio.entity.enums.MaterialCaixa;
import dev.java10x.desafiorelogio.entity.enums.TipoMovimento;
import dev.java10x.desafiorelogio.entity.enums.TipoVidro;
import org.springframework.data.jpa.domain.Specification;
/**
 * Classe utilitária responsável por concentrar todas as Specifications
 * utilizadas para montar filtros dinâmicos na entidade Relogio.
 *
 * Padrão aplicado:
 * - Specification Pattern (Spring Data JPA)
 * - Filtros combináveis dinamicamente
 *
 * Objetivo:
 * Permitir construção de consultas flexíveis sem precisar escrever JPQL manual.
 */

public class RelogioSpecs {
    /**
     * Construtor privado para impedir instanciação.
     * Esta classe funciona apenas como fábrica de Specifications (métodos estáticos).
     */
    private RelogioSpecs(){}
    /**
     * Specification neutra.
     *
     * Retorna uma condição sempre verdadeira (conjunction),
     * equivalente a um "WHERE 1=1".
     *
     * Utilizada como base para encadeamento de filtros.
     */

    public static Specification<Relogio> tudo(){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
    }
    /**
     * Método utilitário para verificar se uma String é nula ou vazia.
     *
     * Evita criar filtros desnecessários quando o parâmetro não foi informado.
     */

    public static boolean blank(String str){
        return str == null || str.isBlank();
    }
    /**
     * Busca textual genérica.
     *
     * Equivalente SQL:
     *
     * WHERE
     * LOWER(marca) LIKE '%termo%'
     * OR LOWER(modelo) LIKE '%termo%'
     * OR LOWER(referencia) LIKE '%termo%'
     *
     * Permite pesquisa ampla ignorando maiúsculas/minúsculas.
     */

    public static Specification<Relogio> busca(String termo){
        if (blank(termo)) return tudo();
        String like = "%" + termo.toLowerCase() + "%";
        return ((root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("marca")), like),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("modelo")), like),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("referencia")), like)
        ));

        /**
         * Filtro exato por marca.
         *
         * WHERE marca = ?
         */

    }
    public static Specification<Relogio> marcaIgual(String marca){
        if (blank(marca)) return tudo();
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("marca"), marca));
    }
    /**
     * Filtro exato por referência.
     *
     * WHERE referencia = ?
     */
    public static Specification<Relogio> referenciaIgual(String referencia){
        if (blank(referencia)) return tudo();
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("referencia"), referencia));
    }
    /**
     * Filtro por tipo de movimento.
     *
     * Exemplo: AUTOMATICO, QUARTZ etc.
     *
     * WHERE tipo_movimento = ?
     */
    public static Specification<Relogio> tipoMovimentoIgual(TipoMovimento tipoMovimento) {
        if (tipoMovimento == null) return tudo();
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tipoMovimento"), tipoMovimento));
    }
    /**
     * Filtro por tipo de vidro.
     *
     * WHERE tipo_vidro = ?
     */
    public static Specification<Relogio> tipoVidroIgual(TipoVidro tipoVidro ) {
        if (tipoVidro == null) return tudo();
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tipoVidro"), tipoVidro));
    }

    /**
     * Filtro por material da caixa.
     *
     * WHERE material_caixa = ?
     */
    public static Specification<Relogio> materialCaixaIgual(MaterialCaixa materialCaixa) {
        if (materialCaixa == null) return tudo();
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("materialCaixa"), materialCaixa));
    }
    /**
     * Filtro por faixa de resistência à água.
     *
     * Casos possíveis:
     * - min e max informados → BETWEEN
     * - apenas min → >=
     * - apenas max → <=
     *
     * WHERE resistencia_agua BETWEEN ? AND ?
     */
    public static Specification<Relogio> resisteciaAguaEntre(Integer min, Integer max){
        if (min == null && max == null) return tudo();
        return (root, query, criteriaBuilder) -> {
            if (min != null && max != null) return criteriaBuilder.between(root.get("resistenciaAguaM"), min, max);
            if (min != null) return criteriaBuilder.greaterThanOrEqualTo(root.get("resistenciaAguaM"), min);
            return criteriaBuilder.lessThanOrEqualTo(root.get("resistenciaAguaM"), max);
        };
    }
    /**
     * Filtro por faixa de preço.
     *
     * Observação arquitetural:
     * O preço está armazenado em centavos (Long),
     * evitando problemas de precisão com BigDecimal/double.
     *
     * WHERE preco_em_centavos BETWEEN ? AND ?
     */
    public static Specification<Relogio> precoEntre(Long min, Long max) {
        if (min == null && max == null) return tudo();
        return (root, query, criteriaBuilder) -> {
            if (min != null && max != null) return criteriaBuilder.between(root.get("precoEmCentavos"), min, max);
            if (min != null) return criteriaBuilder.greaterThanOrEqualTo(root.get("precoEmCentavos"), min);
            return criteriaBuilder.lessThanOrEqualTo(root.get("precoEmCentavos"), max);
        };
    }
    /**
     * Filtro por diâmetro da caixa em milímetros.
     *
     * WHERE diametro_mm BETWEEN ? AND ?
     */
    public static Specification<Relogio> diametroEntre(Integer min, Integer max){
        if (min == null && max == null) return tudo();
        return (root, query, criteriaBuilder) -> {
            if (min != null && max != null) return criteriaBuilder.between(root.get("diametroMm"), min, max);
            if (min != null) return criteriaBuilder.greaterThanOrEqualTo(root.get("diametroMm"), min);
            return criteriaBuilder.lessThanOrEqualTo(root.get("diametroMm"), max);
        };

    }
}