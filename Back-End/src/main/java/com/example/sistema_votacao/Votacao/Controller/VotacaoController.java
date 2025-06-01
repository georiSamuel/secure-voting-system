package com.example.sistema_votacao.Votacao.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sistema_votacao.Votacao.Model.Votacao;
import com.example.sistema_votacao.Votacao.Model.VotacaoPersonalizada;
import com.example.sistema_votacao.Votacao.Service.VotacaoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/votacoes")
public class VotacaoController {

    private final VotacaoService votacaoService;

    public VotacaoController(VotacaoService votacaoService) {
        this.votacaoService = votacaoService;
    }

    // Criar uma votação simples
    @PostMapping
    public ResponseEntity<Votacao> criarVotacao(@RequestBody Votacao votacao) {
        Votacao novaVotacao = votacaoService.criarVotacao(votacao);
        return ResponseEntity.ok(novaVotacao);
    }

    // Buscar todas as votações
    @GetMapping
    public ResponseEntity<List<Votacao>> buscarTodasVotacoes() {
        List<Votacao> votacoes = votacaoService.buscarTodasVotacoes();
        return ResponseEntity.ok(votacoes);
    }

    // Buscar votação por ID
    @GetMapping("/{id}")
    public ResponseEntity<Votacao> buscarPorId(@PathVariable Long id) {
        Optional<Votacao> votacao = votacaoService.buscarPorId(id);
        if (votacao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(votacao.get());
    }

    // Deletar votação por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVotacao(@PathVariable Long id) {
        votacaoService.deletarVotacao(id);
        return ResponseEntity.noContent().build();
    }

    // Criar uma votação personalizada
    @PostMapping("/personalizada")
    public ResponseEntity<VotacaoPersonalizada> criarVotacaoPersonalizada(@RequestBody VotacaoPersonalizada votacao) {
        VotacaoPersonalizada novaVotacao = votacaoService.criarVotacaoPersonalizada(votacao);
        return ResponseEntity.ok(novaVotacao);
    }
}
