package dev.java10x.desafiorelogio.entity.enums;

public enum TipoMovimento {
    QUARTZ, AUTOMATICO, MANUAL;

    /**
     * Converte valores vindos da API (em ingles) para o enum interno do domínio.
     * Lança IllegalArgumentoException para valores inválidos
     */
    public static TipoMovimento fromApi(String valor){
        if (valor == null || valor.isBlank()) return null;
        return switch (valor){
            case "quartz" -> QUARTZ;
            case "automatic" -> AUTOMATICO;
            case "manal" -> MANUAL;
            default -> throw new IllegalArgumentException("Tipo de Movimento inválido: " + valor);
        };
    }

    public String toApi(){
        return switch (this){
            case QUARTZ -> "quartz";
            case AUTOMATICO -> "automatic";
            case MANUAL -> "manual";
        };
    }
}
