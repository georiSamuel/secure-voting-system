package com.example.sistema_votacao.Service;

import org.springframework.stereotype.Service;

import com.example.sistema_votacao.Model.Votacao;
import com.example.sistema_votacao.Repository.VotacaoRepository;

import java.util.List;

@Service
public class VotacaoService {

    private final VotacaoRepository votacaoRepository;

    public VotacaoService(VotacaoRepository votacaoRepository) {
        this.votacaoRepository = votacaoRepository;
    }

    public Votacao criarVotacao(Votacao votacao) {
        return votacaoRepository.save(votacao);
    }

    public List<Votacao> buscarTodasVotacoes() {
        return votacaoRepository.findAll();
    }

    public Votacao buscarPorId(Long id) {
        return votacaoRepository.findById(id).orElse(null);
    }

    public void deletarVotacao(Long id) {
        votacaoRepository.deleteById(id);
    }
}
