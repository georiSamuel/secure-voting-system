package com.example.sistema_votacao.Votacao.Model;

public enum TipoCargoEleitoral {
    DEPUTADO_FEDERAL("Deputado(a) Federal"),
    DEPUTADO_ESTADUAL("Deputado(a) Estadual"),
    SENADOR("Senador(a)"),
    GOVERNADOR("Governador(a)"),
    PRESIDENTE_DA_REPUBLICA("Presidente da República");

    private final String descricao;

    TipoCargoEleitoral(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
