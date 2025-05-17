package com.example.sistema_votacao.Voto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votos")
public class VotoController {

    private final VotoService votoService;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @PostMapping
    public ResponseEntity<Voto> criarVoto(@RequestBody Voto voto) {
        Voto novoVoto = votoService.criarVoto(voto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoVoto);
    }

    @GetMapping
    public ResponseEntity<List<Voto>> buscarTodosVotos() {
        List<Voto> votos = votoService.buscarTodosVotos();
        return ResponseEntity.ok(votos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voto> buscarPorId(@PathVariable Long id) {
        Voto voto = votoService.buscarPorId(id);
        if (voto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(voto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVoto(@PathVariable Long id) {
        votoService.deletarVoto(id);
        return ResponseEntity.noContent().build();
    }
}
