package sistema.votacao.voto.dto;

import java.time.LocalDateTime;

/**
 * DTO para resposta de voto.
 * @author Lethycia
 * @author Horlan
 * @version 2.0
 * @since 29/05/2025
 */
public record VotoResponseDTO(
    Long id,
    Long idUsuario,
    Long idVotacao,
    LocalDateTime dataVoto
) {}
