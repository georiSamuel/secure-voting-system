package sistema.votacao.Voto.Controller;

import sistema.votacao.Voto.DTO.OpcaoVotoRequestDTO;
import sistema.votacao.Voto.DTO.OpcaoVotoResponseDTO;
import sistema.votacao.Voto.Model.OpcaoVoto;
import sistema.votacao.Voto.Service.OpcaoVotoService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/votos") // Base mapping
public class OpcaoVotoController {

    private final OpcaoVotoService opcaoVotoService;

    public OpcaoVotoController(OpcaoVotoService opcaoVotoService) {
        this.opcaoVotoService = opcaoVotoService;
    }

    @PostMapping("/opcoes")
    public ResponseEntity<OpcaoVotoResponseDTO> criarOpcaoVoto(
            @Valid @RequestBody OpcaoVotoRequestDTO opcaoVotoRequest) {

        OpcaoVoto novaOpcao = opcaoVotoService.criarOpcaoVoto(
                opcaoVotoRequest.getDescricao(),
                opcaoVotoRequest.getVotacaoId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new OpcaoVotoResponseDTO(novaOpcao));
    }

    @GetMapping("/votacao/{votacaoId}")
    public ResponseEntity<List<OpcaoVotoResponseDTO>> listarOpcoesPorVotacao(
            @PathVariable Long votacaoId) {

        List<OpcaoVoto> opcoes = opcaoVotoService.buscarOpcoesPorVotacao(votacaoId);

        List<OpcaoVotoResponseDTO> response = opcoes.stream()
                .map(OpcaoVotoResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpcaoVotoResponseDTO> buscarOpcaoPorId(
            @PathVariable Long id) {

        OpcaoVoto opcao = opcaoVotoService.buscarPorId(id);
        return ResponseEntity.ok(new OpcaoVotoResponseDTO(opcao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarOpcaoVoto(
            @PathVariable Long id) {

        opcaoVotoService.deletarOpcaoVoto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/votar")
    public ResponseEntity<Void> registrarVoto(
            @PathVariable Long id) {

        opcaoVotoService.incrementarVotos(id);
        return ResponseEntity.ok().build();
    }
}
