package com.example.sistema_votacao.Votacao.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sistema_votacao.Votacao.Model.Votacao;
import com.example.sistema_votacao.Votacao.Service.VotacaoService;

import java.util.List;

@RestController
@RequestMapping("/votacoes")
public class VotacaoController {

    private final VotacaoService votacaoService;

    public VotacaoController(VotacaoService votacaoService) {
        this.votacaoService = votacaoService;
    }

    @PostMapping
    public ResponseEntity<Votacao> criarVotacao(@RequestBody Votacao votacao) {
        Votacao novaVotacao = votacaoService.criarVotacao(votacao);
        return ResponseEntity.ok(novaVotacao);
    }

    @GetMapping
    public ResponseEntity<List<Votacao>> buscarTodasVotacoes() {
        List<Votacao> votacoes = votacaoService.buscarTodasVotacoes();
        return ResponseEntity.ok(votacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Votacao> buscarPorId(@PathVariable Long id) {
        Votacao votacao = votacaoService.buscarPorId(id);
        if (votacao == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(votacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVotacao(@PathVariable Long id) {
        votacaoService.deletarVotacao(id);
        return ResponseEntity.noContent().build();
    }
}
