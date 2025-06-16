package sistema.votacao.Voto.DTO;

import lombok.Data;

/**
 * DTO para requisição de voto.
 * @author Lethycia
 * @author Horlan
 * @version 2.0
 * @since 29/05/2025
 */
@Data
public class VotoRequestDTO {
    private Long usuarioId;
    private Long votacaoId;
    private Long opcaoVotoId;
}
