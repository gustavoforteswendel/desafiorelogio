package dev.java10x.desafiorelogio.entity.enums;

public enum TipoVidro {
    MINERAL, SAFIRA, ACRILICO;
    /**
     * Converte valores vindos da API (em ingles) para o enum interno do domínio.
     * Lança IllegalArgumentoException para valores inválidos
     */
    public static TipoVidro fromApi(String valor){
        if (valor == null || valor.isBlank()) return null;
        return switch (valor){
            case "mineral" -> MINERAL;
            case "sapphire" -> SAFIRA;
            case "acrylic" -> ACRILICO;
            default -> throw new IllegalArgumentException("Tipo de Vidro Inválido: " + valor);
        };
    }
    public String toApi(){
        return switch (this){
            case MINERAL -> "mineral";
            case SAFIRA -> "sapphire";
            case ACRILICO -> "acrylic";
        };
    }
}
