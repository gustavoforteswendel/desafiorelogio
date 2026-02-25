package dev.java10x.desafiorelogio.controller;


import dev.java10x.desafiorelogio.dto.AtualizarRelogioRequest;
import dev.java10x.desafiorelogio.dto.CirarRelogioResquest;
import dev.java10x.desafiorelogio.dto.PaginaRelogioDto;
import dev.java10x.desafiorelogio.dto.RelogioDto;
import dev.java10x.desafiorelogio.service.RelogioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/relogios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
/**
 * Camada Controller da API.
 *
 * Responsável por:
 * - Expor os endpoints HTTP
 * - Receber parâmetros da requisição
 * - Validar dados de entrada (@Valid)
 * - Delegar regras de negócio para o Service
 *
 * Esta camada NÃO contém regra de negócio.
 * Apenas orquestra entrada e saída.
 */
public class RelogioController {
    /**
     * Service injetado via construtor (Lombok).
     * Camada responsável pela lógica da aplicação.
     */
    private RelogioService servico;
    /**
     * GET /api/relogios
     *
     * Endpoint responsável por listar relógios com:
     * - Paginação
     * - Filtros dinâmicos
     * - Ordenação
     *
     * Todos os filtros são opcionais.
     */

    @GetMapping
    public PaginaRelogioDto listar(
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "12") int porPagina,
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false)  String tipoMovimento,
            @RequestParam(required = false) String materialCaixa,
            @RequestParam(required = false) String tipoVidro,
            @RequestParam(required = false) Integer resistenciaMin,
            @RequestParam(required = false) Integer resistenciaMax,
            @RequestParam(required = false) Long precoMin,
            @RequestParam(required = false) Long precoMax,
            @RequestParam(required = false) Integer diametroMin,
            @RequestParam(required = false) Integer diametroMax,
            @RequestParam(required = false) String ordenar
    ){
       return servico.listar(
               pagina,
               porPagina,
               busca,
               marca,
               tipoMovimento,
               materialCaixa,
               tipoVidro,
               resistenciaMin,
               resistenciaMax,
               precoMin,
               precoMax,
               diametroMin,
               diametroMax,
               ordenar);
    }
    /**
     * GET /api/relogios/{id}
     *
     * Busca um relógio pelo UUID.
     * Se não existir → exceção 404.
     */
    @GetMapping("/{id}")
    public RelogioDto buscarPorId(@PathVariable UUID id){
        return servico.buscarPorId(id);
    }
    /**
     * POST /api/relogios
     *
     * Cria um novo relógio.
     *
     * @Valid ativa Bean Validation no DTO.
     */
    @PostMapping
    public RelogioDto criar (@Valid @RequestBody CirarRelogioResquest req){
        return servico.criar(req);
    }
    /**
     * PUT /api/relogios/{id}
     *
     * Atualiza um relógio existente.
     */
    @PutMapping("/{id}")
    public RelogioDto atualizar(@PathVariable UUID id, @Valid @RequestBody AtualizarRelogioRequest req){
        return servico.atualizar(id, req);
    }
    /**
     * DELETE /api/relogios/{id}
     *
     * Remove um relógio pelo UUID.
     */
    @DeleteMapping("/{id}")
    public void remover(@PathVariable UUID id){
        servico.remover(id);
    }

}
