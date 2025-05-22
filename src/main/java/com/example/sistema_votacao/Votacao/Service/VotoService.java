package com.example.sistema_votacao.Votacao.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.sistema_votacao.Usuario.Model.UsuarioModel;
import com.example.sistema_votacao.Usuario.Service.UsuarioService;
import com.example.sistema_votacao.Votacao.Model.Voto;
import com.example.sistema_votacao.Votacao.Repository.VotoRepository;
import java.sql.Timestamp;
import java.util.List;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final UsuarioService usuarioService;
    private final VotacaoService votacaoService;

    public VotoService(VotoRepository votoRepository,
            UsuarioService usuarioService,
            VotacaoService votacaoService) {
        this.votoRepository = votoRepository;
        this.usuarioService = usuarioService;
        this.votacaoService = votacaoService;
    }

    @Transactional
    public Voto criarVoto(Voto voto) {
        return votoRepository.save(voto);
    }

    @Transactional(readOnly = true)
    public List<Voto> buscarTodosVotos() {
        return votoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Voto buscarPorId(Long id) {
        return votoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voto não encontrado com ID: " + id));
    }

    @Transactional
    public void deletarVoto(Long id) {
        if (!votoRepository.existsById(id)) {
            throw new RuntimeException("Voto não encontrado com ID: " + id);
        }
        votoRepository.deleteById(id);
    }

    @Transactional
    public Voto registrarVoto(Voto voto, Long usuarioId, Long votacaoId) {

        if (usuarioJaVotou(usuarioId, votacaoId)) {
            throw new IllegalStateException("Usuário já votou nesta votação");
        }

        if (!votacaoService.isVotacaoAtiva(votacaoId)) {
            throw new IllegalStateException("Votação não está ativa no momento");
        }

        UsuarioModel usuario = usuarioService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        voto.setHorario(new Timestamp(System.currentTimeMillis()));
        voto.setUsuario(usuario);

        Voto votoSalvo = votoRepository.save(voto);

        usuarioService.atualizarStatusVoto(usuarioId, true);

        return votoSalvo;
    }

    @Transactional(readOnly = true)
    public List<Voto> buscarVotosPorVotacao(Long votacaoId) {
        return votoRepository.findByVotacaoId(votacaoId);
    }

    @Transactional(readOnly = true)
    public boolean usuarioJaVotou(Long usuarioId, Long votacaoId) {
        return votoRepository.existsByUsuarioIdAndVotacaoId(usuarioId, votacaoId);
    }
}