package sistema.votacao.Voto.DTO;

import sistema.votacao.Voto.Model.OpcaoVoto;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpcaoVotoResponseDTO {
    private Long id;
    private String descricao;
    private Long quantidadeVotos;
    private Long votacaoId;

    public OpcaoVotoResponseDTO() {
    }

    public OpcaoVotoResponseDTO(Long id, String descricao, Long quantidadeVotos, Long votacaoId) {
        this.id = id;
        this.descricao = descricao;
        this.quantidadeVotos = quantidadeVotos;
        this.votacaoId = votacaoId;
    }

    public OpcaoVotoResponseDTO(OpcaoVoto opcao) {
        this.id = opcao.getId();
        this.descricao = opcao.getDescricao();
        this.quantidadeVotos = opcao.getQuantidadeVotos();
        this.votacaoId = opcao.getVotacao() != null ? opcao.getVotacao().getId() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getQuantidadeVotos() {
        return quantidadeVotos;
    }

    public void setQuantidadeVotos(Long quantidadeVotos) {
        this.quantidadeVotos = quantidadeVotos;
    }

    public Long getVotacaoId() {
        return votacaoId;
    }

    public void setVotacaoId(Long votacaoId) {
        this.votacaoId = votacaoId;
    }

    @Override
    public String toString() {
        return "OpcaoVotoResponseDTO{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", quantidadeVotos=" + quantidadeVotos +
                ", votacaoId=" + votacaoId +
                '}';
    }
}