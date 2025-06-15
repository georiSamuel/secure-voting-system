package sistema.votacao.OpcaoVoto.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.votacao.OpcaoVoto.Model.OpcaoVoto;
import sistema.votacao.OpcaoVoto.Repository.OpcaoVotoRepository;
import sistema.votacao.Votacao.Model.Votacao;
import sistema.votacao.Votacao.Repository.VotacaoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Serviço para gerenciar as operações relacionadas às opções de voto.
 * Fornece métodos para criar, buscar, incrementar votos e deletar opções de
 * voto.
 * 
 * @author Lethycia e Georis
 * @version 1.0
 * @since 26/05/25
 */
@Service
public class OpcaoVotoService {

    private final OpcaoVotoRepository opcaoVotoRepository;
    private final VotacaoRepository votacaoRepository;

    /**
     * Construtor que injeta os repositórios de opção de voto e votação.
     * 
     * @param opcaoVotoRepository repositório de opções de voto
     * @param votacaoRepository   repositório de votações
     */
    public OpcaoVotoService(OpcaoVotoRepository opcaoVotoRepository,
            VotacaoRepository votacaoRepository) {
        this.opcaoVotoRepository = opcaoVotoRepository;
        this.votacaoRepository = votacaoRepository;
    }

    /**
     * Cria uma nova opção de voto com base em uma descrição e ID da votação.
     * 
     * @param descricao descrição da opção de voto
     * @param votacaoId ID da votação
     * @return a opção de voto criada
     */
    @Transactional
    public OpcaoVoto criarOpcaoVoto(String descricao, Long votacaoId) {
        Votacao votacao = votacaoRepository.findById(votacaoId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada com ID: " + votacaoId));
        return criarOpcaoVoto(descricao, votacao);
    }

    /**
     * Cria uma nova opção de voto com base em uma descrição e objeto Votacao.
     * 
     * @param descricao descrição da opção de voto
     * @param votacao   objeto Votacao ao qual a opção será vinculada
     * @return a opção de voto criada
     */
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

    /**
     * Busca todas as opções de voto associadas a uma votação.
     * 
     * @param votacaoId ID da votação
     * @return lista de opções de voto
     */
    @Transactional(readOnly = true)
    public List<OpcaoVoto> buscarOpcoesPorVotacao(Long votacaoId) {
        return opcaoVotoRepository.findByVotacaoId(votacaoId);
    }

    /**
     * Incrementa a quantidade de votos de uma opção de voto específica.
     * 
     * @param opcaoId ID da opção de voto
     * @return a opção de voto atualizada com o novo total de votos
     */
    @Transactional
    public OpcaoVoto incrementarVotos(Long opcaoId) {
        OpcaoVoto opcao = buscarPorId(opcaoId);
        opcao.incrementarVotos();
        return opcaoVotoRepository.save(opcao);
    }

    /**
     * Busca uma opção de voto pelo seu ID.
     * 
     * @param id ID da opção de voto
     * @return a opção de voto encontrada
     * @throws RuntimeException se a opção não for encontrada
     */
    @Transactional(readOnly = true)
    public OpcaoVoto buscarPorId(Long id) {
        return opcaoVotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opção de voto não encontrada com ID: " + id));
    }

    /**
     * Deleta uma opção de voto com base no seu ID.
     * 
     * @param id ID da opção de voto
     * @throws RuntimeException se a opção não for encontrada
     */
    @Transactional
    public void deletarOpcaoVoto(Long id) {
        if (!opcaoVotoRepository.existsById(id)) {
            throw new RuntimeException("Opção de voto não encontrada com ID: " + id);
        }
        opcaoVotoRepository.deleteById(id);
    }

    /**
     * Verifica se já existe uma opção de voto com a mesma descrição em uma
     * determinada votação.
     * 
     * @param descricao descrição da opção
     * @param votacaoId ID da votação
     * @return true se existir, false caso contrário
     */
    @Transactional(readOnly = true)
    public boolean existeOpcaoNaVotacao(String descricao, Long votacaoId) {
        return opcaoVotoRepository.findByDescricaoAndVotacaoId(descricao, votacaoId).isPresent();
    }

    /**
     * Retorna o ranking das opções de voto de uma votação, ordenadas pela
     * quantidade de votos.
     * 
     * @param votacaoId ID da votação
     * @return lista de opções de voto em ordem decrescente de votos
     */
    @Transactional(readOnly = true)
    public List<OpcaoVoto> buscarRankingPorVotacao(Long votacaoId) {
        return opcaoVotoRepository.findRankingByVotacaoId(votacaoId);
    }

    /**
     * Busca a opção de voto vencedora de uma votação.
     * 
     * @param votacaoId ID da votação
     * @return a opção de voto com maior quantidade de votos, se existir
     */
    @Transactional(readOnly = true)
    public Optional<OpcaoVoto> buscarVencedoraPorVotacao(Long votacaoId) {
        return opcaoVotoRepository.findVencedoraByVotacaoId(votacaoId);
    }

    /**
     * Conta quantas opções de voto existem em uma determinada votação.
     * 
     * @param votacaoId ID da votação
     * @return número de opções de voto
     */
    @Transactional(readOnly = true)
    public long contarOpcoesPorVotacao(Long votacaoId) {
        return opcaoVotoRepository.countByVotacaoId(votacaoId);
    }
}
