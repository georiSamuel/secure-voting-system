package sistema.votacao.OpcaoVoto.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OpcaoVotoRequestDTO {

    @NotBlank(message = "A descrição da opção é obrigatória")
    @Size(min = 2, max = 100, message = "A descrição deve ter entre 2 e 100 caracteres")
    private String descricao;

    @NotNull(message = "O ID da votação é obrigatório")
    private Long votacaoId;

    public OpcaoVotoRequestDTO() {
    }

    public OpcaoVotoRequestDTO(String descricao, Long votacaoId) {
        this.descricao = descricao;
        this.votacaoId = votacaoId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getVotacaoId() {
        return votacaoId;
    }

    public void setVotacaoId(Long votacaoId) {
        this.votacaoId = votacaoId;
    }
}