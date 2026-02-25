package dev.java10x.desafiorelogio.config;


import dev.java10x.desafiorelogio.entity.Relogio;
import dev.java10x.desafiorelogio.entity.enums.MaterialCaixa;
import dev.java10x.desafiorelogio.entity.enums.TipoMovimento;
import dev.java10x.desafiorelogio.entity.enums.TipoVidro;
import dev.java10x.desafiorelogio.repository.RelogioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class CrregadorDadosInicial {

    private final RelogioRepository relogioRepository;




    @Bean
    CommandLineRunner seedRelogios(){
        return args -> {
          if (relogioRepository.count() > 0) return;
          Instant agora = Instant.now();
            List<Relogio> relogios = List.of(
                    Relogio.builder()
                            .id(UUID.randomUUID())
                            .marca("Cassio")
                            .modelo("F-91WD")
                            .referencia("1234")
                            .tipoMovimento(TipoMovimento.QUARTZ)
                            .materialCaixa(MaterialCaixa.RESINA)
                            .tipoVidro(TipoVidro.ACRILICO)
                            .resistenciaAguaM(30)
                            .diametroMm(35)
                            .lugToLugMm(38)
                            .espessuraMm(9)
                            .larguraLugMm(18)
                            .precoEmCentavos(12990)
                            .urlImagem("123")
                            .criadoEM(agora.minusSeconds(50000))
                            .build(),
                    Relogio.builder()
                            .id(UUID.randomUUID())
                            .marca("Seiko")
                            .modelo("Diver 200m")
                            .referencia("890")
                            .tipoMovimento(TipoMovimento.AUTOMATICO)
                            .materialCaixa(MaterialCaixa.ACO)
                            .tipoVidro(TipoVidro.MINERAL)
                            .resistenciaAguaM(42)
                            .diametroMm(46)
                            .lugToLugMm(50)
                            .espessuraMm(13)
                            .larguraLugMm(22)
                            .precoEmCentavos(159990)
                            .urlImagem("456")
                            .criadoEM(agora.minusSeconds(30000))
                            .build(),
                    Relogio.builder()
                            .id(UUID.randomUUID())
                            .marca("Citizen")
                            .modelo("Eco-Diver Field")
                            .referencia("88888")
                            .tipoMovimento(TipoMovimento.QUARTZ)
                            .materialCaixa(MaterialCaixa.RESINA)
                            .tipoVidro(TipoVidro.ACRILICO)
                            .resistenciaAguaM(30)
                            .diametroMm(35)
                            .lugToLugMm(38)
                            .espessuraMm(9)
                            .larguraLugMm(18)
                            .precoEmCentavos(12990)
                            .urlImagem("456")
                            .criadoEM(agora.minusSeconds(50000))
                            .build()
            );

            relogioRepository.saveAll(relogios);
        };
    }

}
