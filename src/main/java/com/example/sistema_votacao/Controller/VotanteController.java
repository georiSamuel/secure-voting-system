package com.example.sistema_votacao.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sistema_votacao.Model.Votante;
import com.example.sistema_votacao.Service.VotanteService;

import java.util.List;

@RestController
@RequestMapping("/votantes")
public class VotanteController {

    private final VotanteService votanteService;

    public VotanteController(VotanteService votanteService) {
        this.votanteService = votanteService;
    }

    // Criar novo votante
    @PostMapping
    public ResponseEntity<Votante> criarVotante(@RequestBody Votante votante) {
        Votante novoVotante = votanteService.criarVotante(votante);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoVotante);
    }

    // busca pelo cpf
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Votante> buscarPorCpf(@PathVariable String cpf) {
        Votante votante = votanteService.buscarVotantePorCpf(cpf);
        if (votante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(votante);
    }

    @GetMapping
    public ResponseEntity<List<Votante>> listarTodos() {
        List<Votante> votantes = votanteService.buscarTodos();
        return ResponseEntity.ok(votantes);
    }

    // deleta votante aravés do id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVotante(@PathVariable Long id) {
        votanteService.deletarVotante(id);
        return ResponseEntity.noContent().build();
    }
}
