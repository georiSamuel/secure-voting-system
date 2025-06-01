package com.example.sistema_votacao.Votacao.Service;

import org.springframework.stereotype.Service;

import com.example.sistema_votacao.Votacao.Model.Votacao;
import com.example.sistema_votacao.Votacao.Repository.VotacaoRepository;

import java.sql.Timestamp;
import java.util.List;

@Service
public class VotacaoService {

    private final VotacaoRepository votacaoRepository;

    public VotacaoService(VotacaoRepository votacaoRepository) {
        this.votacaoRepository = votacaoRepository;
    }

    public Votacao criarVotacao(Votacao votacao) {
        if (votacao.getInicio().after(votacao.getFim())) {
            throw new IllegalArgumentException("Data de início deve ser antes da data de fim");
        }
        return votacaoRepository.save(votacao);
    }

    public List<Votacao> buscarTodasVotacoes() {
        return votacaoRepository.findAll();
    }

    public Votacao buscarPorId(Long id) {
        return votacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada com ID: " + id));
    }

    public void deletarVotacao(Long id) {
        if (!votacaoRepository.existsById(id)) {
            throw new RuntimeException("Votação não encontrada. Não é possível deletar.");
        }
        votacaoRepository.deleteById(id);
    }

    public List<Votacao> buscarVotacoesAtivas() {
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        return votacaoRepository.findByInicioBeforeAndFimAfter(agora, agora);
    }

    public Votacao encerrarVotacao(Long id) {
        Votacao votacao = buscarPorId(id);
        votacao.setFim(new Timestamp(System.currentTimeMillis()));
        return votacaoRepository.save(votacao);
    }

    public boolean isVotacaoAtiva(Long votacaoId) {
        Votacao votacao = buscarPorId(votacaoId);
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        return votacao.getInicio().before(agora) && votacao.getFim().after(agora);
    }
}