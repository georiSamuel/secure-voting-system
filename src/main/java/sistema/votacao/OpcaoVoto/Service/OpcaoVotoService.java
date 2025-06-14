package sistema.votacao.OpcaoVoto.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.votacao.OpcaoVoto.Model.OpcaoVoto;
import sistema.votacao.OpcaoVoto.Repository.OpcaoVotoRepository;
import sistema.votacao.Votacao.Model.Votacao;
import sistema.votacao.Votacao.Repository.VotacaoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OpcaoVotoService {

    private final OpcaoVotoRepository opcaoVotoRepository;
    private final VotacaoRepository votacaoRepository;

    public OpcaoVotoService(OpcaoVotoRepository opcaoVotoRepository,
                            VotacaoRepository votacaoRepository) {
        this.opcaoVotoRepository = opcaoVotoRepository;
        this.votacaoRepository = votacaoRepository;
    }

    @Transactional
    public OpcaoVoto criarOpcaoVoto(String descricao, Long votacaoId) {
        Votacao votacao = votacaoRepository.findById(votacaoId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada com ID: " + votacaoId));
        return criarOpcaoVoto(descricao, votacao);
    }

    @Transactional
    public OpcaoVoto criarOpcaoVoto(String descricao, Votacao votacao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição da opção não pode ser vazia");
        }

        if (votacao == null) {
            throw new IllegalArgumentException("Votação não pode ser nula");
        }

        if (existeOpcaoNaVotacao(descricao, votacao.getId())) {
            throw new IllegalStateException("Já existe uma opção com esta descrição na votação");
        }

        OpcaoVoto opcao = new OpcaoVoto(descricao, votacao);
        return opcaoVotoRepository.save(opcao);
    }

    @Transactional(readOnly = true)
    public List<OpcaoVoto> buscarOpcoesPorVotacao(Long votacaoId) {
        return opcaoVotoRepository.findByVotacaoId(votacaoId);
    }

    // MUDANÇA AQUI [GEORIS]
    @Transactional
    public OpcaoVoto incrementarVotos(Long opcaoId) {
        // 1. BUSCAR: Carrega a entidade. O @Convert descriptografa o valor aqui.
        OpcaoVoto opcao = buscarPorId(opcaoId);

        // 2. MODIFICAR: Incrementa o valor em memória.
        opcao.incrementarVotos();

        // 3. SALVAR: Persiste a entidade. O @Convert criptografa o novo valor aqui.
        return opcaoVotoRepository.save(opcao);
    }
    // --- FIM DA ALTERAÇÃO ---

    @Transactional(readOnly = true)
    public OpcaoVoto buscarPorId(Long id) {
        return opcaoVotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opção de voto não encontrada com ID: " + id));
    }

    @Transactional
    public void deletarOpcaoVoto(Long id) {
        if (!opcaoVotoRepository.existsById(id)) {
            throw new RuntimeException("Opção de voto não encontrada com ID: " + id);
        }
        opcaoVotoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existeOpcaoNaVotacao(String descricao, Long votacaoId) {
        return opcaoVotoRepository.findByDescricaoAndVotacaoId(descricao, votacaoId).isPresent();
    }

    @Transactional(readOnly = true)
    public List<OpcaoVoto> buscarRankingPorVotacao(Long votacaoId) {
        return opcaoVotoRepository.findRankingByVotacaoId(votacaoId);
    }

    @Transactional(readOnly = true)
    public Optional<OpcaoVoto> buscarVencedoraPorVotacao(Long votacaoId) {
        return opcaoVotoRepository.findVencedoraByVotacaoId(votacaoId);
    }

    @Transactional(readOnly = true)
    public long contarOpcoesPorVotacao(Long votacaoId) {
        return opcaoVotoRepository.countByVotacaoId(votacaoId);
    }
}
