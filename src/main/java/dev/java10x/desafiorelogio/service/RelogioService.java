package dev.java10x.desafiorelogio.service;

import dev.java10x.desafiorelogio.dto.AtualizarRelogioRequest;
import dev.java10x.desafiorelogio.dto.CirarRelogioResquest;
import dev.java10x.desafiorelogio.dto.PaginaRelogioDto;
import dev.java10x.desafiorelogio.dto.RelogioDto;
import dev.java10x.desafiorelogio.entity.Relogio;
import dev.java10x.desafiorelogio.entity.enums.MaterialCaixa;
import dev.java10x.desafiorelogio.entity.enums.TipoMovimento;
import dev.java10x.desafiorelogio.entity.enums.TipoVidro;
import dev.java10x.desafiorelogio.exception.NaoEncontradoException;
import dev.java10x.desafiorelogio.mapper.RelogioMapper;
import dev.java10x.desafiorelogio.repository.RelogioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static dev.java10x.desafiorelogio.service.RelogioSpecs.*;

@Service
@RequiredArgsConstructor
/**
 * Camada de Serviço responsável por:
 *
 * - Orquestrar acesso ao repositório
 * - Aplicar regras de negócio
 * - Traduzir dados da API (Strings) para enums de domínio
 * - Construir filtros dinâmicos (Specification)
 * - Aplicar paginação e ordenação
 *
 * Essa camada nunca expõe Entity diretamente.
 * Sempre retorna DTO.
 */

public class RelogioService {

    /**
     * Repositório JPA responsável pela persistência.
     * Injetado via construtor (Lombok - RequiredArgsConstructor).
     */

    private final RelogioRepository relogioRepository;

    /**
     * Mapper responsável por converter:
     * Entity ↔ DTO
     */
    private final RelogioMapper mapeador;
    /**
     * Método responsável por listar relógios com:
     *
     * - Filtros dinâmicos
     * - Paginação segura
     * - Ordenação configurável
     *
     * Arquitetura aplicada:
     * - Specification Pattern
     * - Pageable
     * - Sort
     */

    public PaginaRelogioDto listar(
            int pagina,
            int porPagina,
            String busca,
            String marca,
            String tipoMovimento,
            String materialCaixa,
            String tipoVidro,
            Integer resistenciaMin,
            Integer resistenciaMax,
            Long precoMin,
            Long precoMax,
            Integer diametroMin,
            Integer diametroMax,
            String ordenar
    ){
        int paginaSegura = Math.max(1, pagina);
        int porPaginaSeguro = Math.min(60, Math.max(1, pagina));

        // Converte Strings da API para enums do domínio
        TipoMovimento movimento = TipoMovimento.fromApi(tipoMovimento);
        MaterialCaixa material = MaterialCaixa.fromApi(materialCaixa);
        TipoVidro vidro = TipoVidro.fromApi(tipoVidro);
        // Converte parâmetro de ordenação da API para enum controlado
        OrdenacaoRelogios ordenacao = OrdenacaoRelogios.fromApi(ordenar);

        // Define dinamicamente o critério de ordenação
        Sort sort = switch (ordenacao){
            case MAIS_RECENTES -> Sort.by(Sort.Direction.DESC, "criadoEm");
            case PRECO_CRESC -> Sort.by(Sort.Direction.ASC, "precoEmCentavos");
            case PRECO_DESC -> Sort.by(Sort.Direction.DESC, "precoEmCentavos");
            case DIAMETRO_CRESC -> Sort.by(Sort.Direction.ASC, "diametroMm");
            case RESISTENCIA_DESC -> Sort.by(Sort.Direction.DESC, "reistenciaAguaM");
        };
        // Cria objeto de paginação (Spring Data)
        Pageable pageable = PageRequest.of(paginaSegura - 1, porPaginaSeguro, sort);

        Specification<Relogio> spec = Specification.where(busca(busca))
                .and(marcaIgual(marca))
                .and(tipoMovimentoIgual(movimento))
                .and(materialCaixaIgual(material))
                .and(tipoVidroIgual(vidro))
                .and(resisteciaAguaEntre(resistenciaMin, resistenciaMax))
                .and(precoEntre(precoMin, precoMax))
                .and(diametroEntre(diametroMin, diametroMax));


        Page<Relogio> resultado = relogioRepository.findAll(spec, pageable);

        return new PaginaRelogioDto(
                resultado.getContent().stream().map(mapeador::toDto).toList(),
                resultado.getTotalElements()
        );

    }
    /**
     * Busca relógio por ID.
     *
     * Se não existir:
     * lança exceção customizada (404).
     */

    public RelogioDto buscarPorId(UUID id){
        Relogio r = relogioRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Relógio não encontrado: " + id ));
        return mapeador.toDto(r);
    }
    /**
     * Cria um novo relógio.
     *
     * Responsabilidades:
     * - Gerar UUID
     * - Converter enums
     * - Definir data de criação
     * - Persistir no banco
     */
    public RelogioDto criar(CirarRelogioResquest req){
        Relogio r = Relogio.builder()
                .id(UUID.randomUUID())
                .marca(req.marca())
                .modelo(req.modelo())
                .referencia(req.referencia())
                .tipoMovimento(TipoMovimento.fromApi(req.tipoMovimento()))
                .materialCaixa(MaterialCaixa.fromApi(req.materialCaixa()))
                .tipoVidro(TipoVidro.fromApi(req.tipoVidro()))
                .resistenciaAguaM(req.resistenciaAguaM())
                .diametroMm(req.diametroMm())
                .lugToLugMm(req.lugToLugMm())
                .espessuraMm(req.espessuraMm())
                .larguraLugMm(req.larguraLugMm())
                .precoEmCentavos(req.precoEmCentavos())
                .urlImagem(req.urlImagem())
                .criadoEM(Instant.now())
                .build();
        return mapeador.toDto(relogioRepository.save(r));
    }
    /**
     * Atualiza um relógio existente.
     *
     * Fluxo:
     * - Busca entidade
     * - Atualiza campos
     * - Salva novamente
     */
    public RelogioDto atualizar(UUID id, AtualizarRelogioRequest req){
        Relogio r = relogioRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Relógio não encontrado: " + id ));
        r.setMarca(req.marca());
        r.setModelo(req.modelo());
        r.setReferencia(req.referencia());
        r.setTipoMovimento(TipoMovimento.fromApi(req.tipoMovimento()));
        r.setMaterialCaixa(MaterialCaixa.fromApi(req.materialCaixa()));
        r.setTipoVidro(TipoVidro.fromApi(req.tipoVidro()));
        r.setResistenciaAguaM(req.resistenciaAguaM());
        r.setDiametroMm(req.diametroMm());
        r.setLugToLugMm(req.lugToLugMm());
        r.setEspessuraMm(req.espessuraMm());
        r.setLarguraLugMm(req.larguraLugMm());
        r.setPrecoEmCentavos(req.precoEmCentavos());
        r.setUrlImagem(req.urlImagem());
        return mapeador.toDto(relogioRepository.save(r));
    }
    /**
     * Remove relógio por ID.
     *
     * Primeiro valida existência.
     */
    public void remover(UUID id){
        if (!relogioRepository.existsById(id)){
            throw new NaoEncontradoException("Relógio não encontrado: " + id );
        }
        relogioRepository.deleteById(id);
    }

}
