package com.example.sistema_votacao.Voto.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sistema_votacao.Usuario.Service.UsuarioService;
import com.example.sistema_votacao.Voto.Repository.VotoRepository;
import com.example.sistema_votacao.Voto.DTO.VotoRequestDTO;
import com.example.sistema_votacao.Voto.DTO.VotoResponseDTO;
import com.example.sistema_votacao.Voto.Model.OpcaoVoto;
import com.example.sistema_votacao.Voto.Model.VotoModel;
import com.example.sistema_votacao.Votacao.Model.Votacao;
import com.example.sistema_votacao.Votacao.Service.VotacaoService;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final UsuarioService usuarioService;
    private final VotacaoService votacaoService;
    private final OpcaoVotoService opcaoVotoService;

    public VotoService(VotoRepository votoRepository, UsuarioService usuarioService, VotacaoService votacaoService) {
        this.votoRepository = votoRepository;
        this.usuarioService = usuarioService;
        this.votacaoService = votacaoService;
        this.opcaoVotoService = null;
    }

    @Transactional
    public VotoModel criarVoto(VotoModel voto) {
        return votoRepository.save(voto);
    }

    @Transactional(readOnly = true)
    public List<VotoModel> buscarTodosVotos() {
        return votoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public VotoModel buscarPorId(Long id) {
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
    public VotoResponseDTO registrarVoto(VotoRequestDTO dto) {

        if (usuarioJaVotou(dto.getUsuarioId(), dto.getVotacaoId())) {
            throw new IllegalStateException("Usuário já votou nesta votação");
        }

        if (!votacaoService.isVotacaoAtiva(dto.getVotacaoId())) {
            throw new IllegalStateException("Votação não está ativa");
        }

        OpcaoVoto opcao = opcaoVotoService.buscarPorId(dto.getOpcaoVotoId());
        Votacao votacao = votacaoService.buscarPorId(dto.getVotacaoId());

        VotoModel voto = new VotoModel();
        voto.setOpcaoVoto(opcao);
        voto.setVotacao(votacao);
        voto.setDataHoraVoto(LocalDateTime.now());

        voto.setVotoCriptografado(opcao.getDescricao());

        VotoModel salvo = votoRepository.save(voto);

        usuarioService.atualizarStatusVoto(dto.getUsuarioId(), true);
        opcaoVotoService.incrementarVotos(dto.getOpcaoVotoId());

        return new VotoResponseDTO(
                salvo.getId(),
                dto.getUsuarioId(),
                dto.getVotacaoId(),
                salvo.getVotoCriptografado(),
                salvo.getDataHoraVoto());
    }

    @Transactional(readOnly = true)
    public List<VotoModel> buscarVotosPorVotacao(Long votacaoId) {

        return votoRepository.findByVotacaoId(votacaoId);
    }

    @Transactional(readOnly = true)
    public boolean usuarioJaVotou(Long usuarioId, Long votacaoId) {
        return votoRepository.existsByUsuarioIdAndVotacaoId(usuarioId, votacaoId);
    }
}
