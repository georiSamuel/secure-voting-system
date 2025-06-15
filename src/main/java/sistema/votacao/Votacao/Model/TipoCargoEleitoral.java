package sistema.votacao.Votacao.Model;

/**
 * Enumeração que representa os diferentes tipos de cargos em disputas
 * eleitorais formais.
 * Cada cargo possui uma descrição legível, usada para exibição em interfaces ou
 * relatórios.
 * 
 * @author Lethycia
 * @version 1.0
 * @since 26/05/25
 */

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

    /**
     * Retorna a descrição do cargo, formatada para exibição.
     * 
     * @return descrição legível do cargo eleitoral
     */
    public String getDescricao() {
        return descricao;
    }
}
