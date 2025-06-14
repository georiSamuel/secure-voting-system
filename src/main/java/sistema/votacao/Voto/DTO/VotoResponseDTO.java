package sistema.votacao.Voto.DTO;

import java.time.LocalDateTime;

public record VotoResponseDTO(
    Long id,
    Long idUsuario,
    Long idVotacao,
    LocalDateTime dataVoto
) {}
