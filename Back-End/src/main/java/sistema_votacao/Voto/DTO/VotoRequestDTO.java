package sistema_votacao.Voto.DTO;

import lombok.Data;

@Data
public class VotoRequestDTO {
    private Long usuarioId;
    private Long votacaoId;
    private Long opcaoVotoId;
}
