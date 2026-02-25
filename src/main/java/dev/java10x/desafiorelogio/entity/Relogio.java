package dev.java10x.desafiorelogio.entity;

import dev.java10x.desafiorelogio.entity.enums.MaterialCaixa;
import dev.java10x.desafiorelogio.entity.enums.TipoMovimento;
import dev.java10x.desafiorelogio.entity.enums.TipoVidro;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;
/**
 * Entidade JPA que representa um Relógio no domínio.
 * - Mapeia para a tabela "relogio" no banco.
 * - Usa UUID como chave primária.
 * - Mantém dados normalizados (trim) antes de persistir/atualizar.
 */


@Entity
@Table(name = "relogio", indexes = {
        // Índices melhoram performance quando você filtra/ordena por essas colunas com frequência
            @Index(name = "IDX_RELOGIO_MARCA", columnList = "marca"),
            @Index(name = "IDX_RELOGIO_CRIADO_EM", columnList = "criado_em"),
            @Index(name = "IDX_RELOGIO_PRECO", columnList = "preco_em_centavos")
    })
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder



public class Relogio {
    // =========================
    // Identidade / Chave primária
    // =========================

    /**
     * ID imutável do relógio.
     * - UUID evita colisão e não depende de sequência do banco.
     * - "updatable = false" garante que depois de criado, não muda.
     *
     * OBS: como você está gerando no @PrePersist, não precisa de @GeneratedValue.
     */
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    // =========================
    // Dados textuais (campos base)
    // =========================

    /**
     * Marca do relógio.
     * - length define tamanho máximo da coluna (varchar).
     */

    @Column(nullable = false, length = 80)
    private String marca;
    /**
     * Modelo do relógio.
     */

    @Column(nullable = false, length = 125)
    private String modelo;
    /**
     * Referência/código do fabricante (quando existe).
     */

    @Column(nullable = false, length = 80)
    private String referencia;
    // =========================
    // Enums (domínio controlado)
    // =========================

    /**
     * Tipo de movimento:
     * - EnumType.STRING salva "QUARTZ/AUTOMATICO/MANUAL" como texto no banco.
     * - Mais seguro do que ordinal (0/1/2), que pode quebrar se mudar a ordem.
     */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimento tipoMovimento;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MaterialCaixa materialCaixa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoVidro tipoVidro;
    // =========================
    // Medidas / especificações numéricas
    // =========================

    /**
     * Resistência à água em metros (ex: 50m, 100m).
     * Dica: no futuro você pode validar para não aceitar valores negativos.
     */

    @Column(nullable = false)
    private int resistenciaAguaM;

    @Column(nullable = false)
    private int diametroMm;

    @Column(nullable = false)
    private int lugToLugMm;

    @Column(nullable = false)
    private int espessuraMm;

    @Column(nullable = false)
    private int larguraLugMm;
    /**
     * Preço armazenado em centavos para evitar problemas de ponto flutuante (double).
     * Ex: R$ 199,90 -> 19990
     */

    @Column(name = "preco_em_centavos",nullable = false)
    private long precoEmCentavos;
    // =========================
    // Metadados / auditoria
    // =========================

    /**
     * URL da imagem do produto.
     */

    @Column(nullable = false, length = 600)
    private String urlImagem;
    /**
     * Momento de criação do registro (UTC).
     * É preenchido automaticamente no @PrePersist.
     */

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEM;

    // =========================
    // Callbacks do JPA (ciclo de vida)
    // =========================

    /**
     * Executa automaticamente ANTES do INSERT.
     * - Se id não foi preenchido, gera UUID.
     * - Se criadoEm não foi preenchido, define agora.
     * - Normaliza strings (trim).
     */

    @PrePersist
    void prePersist(){
        if(id == null) id = UUID.randomUUID();
        if (criadoEM == null) criadoEM = Instant.now();
        normalizar();
    }
    /**
     * Executa automaticamente ANTES do UPDATE.
     * - Normaliza strings para evitar salvar "com espaços".
     */

    @PreUpdate
    void preUpdate(){
        normalizar();
    }
    /**
     * Remove espaços extras nas pontas para manter dados padronizados no banco.
     * Isso evita problemas de busca/índices, por exemplo "Seiko" vs "Seiko ".
     */

    private void normalizar() {
        if (marca != null) marca = marca.trim();
        if (marca != null) modelo = modelo.trim();
        if (referencia != null) referencia = referencia.trim();
        if (urlImagem != null) urlImagem = urlImagem.trim();
    }


}
