package sistema.votacao.Votacao.Service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import sistema.votacao.Votacao.Model.Votacao;
import sistema.votacao.Votacao.Model.VotacaoPersonalizada;
import sistema.votacao.Votacao.Repository.VotacaoRepository;
import sistema.votacao.Voto.Repository.VotoRepository; // Import VotoRepository

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Serviço para gerenciar as operações relacionadas às votações.
 * Fornece métodos para criar, buscar, deletar e encerrar votações,
 * bem como verificar se uma votação está ativa.
 * 
 * @author Lethycia
 * @version 1.0
 * @since 26/05/25
 */
@Getter
@Setter
@Service
public class VotacaoService {

    private final VotacaoRepository votacaoRepository;
    private final VotoRepository votoRepository;

    /**
     * Construtor que injeta as dependências dos repositórios de votação e voto.
     * 
     * @param votacaoRepository repositório para operações com votações
     * @param votoRepository    repositório para operações com votos
     */
    public VotacaoService(VotacaoRepository votacaoRepository, VotoRepository votoRepository) {
        this.votacaoRepository = votacaoRepository;
        this.votoRepository = votoRepository;
    }

    /**
     * Cria uma nova votação após validar as datas.
     * 
     * @param votacao objeto Votacao a ser criado
     * @return a votação criada e salva no banco
     * @throws IllegalArgumentException se a data de início for posterior à data de
     *                                  fim
     */
    public Votacao criarVotacao(Votacao votacao) {
        if (votacao.getInicio().after(votacao.getFim())) {
            throw new IllegalArgumentException("Data de início deve ser antes da data de fim");
        }
        return votacaoRepository.save(votacao);
    }

    /**
     * Busca todas as votações cadastradas.
     * 
     * @return lista de todas as votações
     */
    public List<Votacao> buscarTodasVotacoes() {
        return votacaoRepository.findAll();
    }

    /**
     * Busca uma votação pelo seu ID.
     * 
     * @param id identificador da votação
     * @return Optional contendo a votação se encontrada, ou vazio caso contrário
     */
    public Optional<Votacao> buscarPorId(Long id) {
        return votacaoRepository.findById(id);
    }

    /**
     * Deleta uma votação pelo seu ID, se existir.
     * 
     * @param id identificador da votação a ser deletada
     * @throws RuntimeException se a votação não for encontrada
     */
    public void deletarVotacao(Long id) {
        if (!votacaoRepository.existsById(id)) {
            throw new RuntimeException("Votação não encontrada. Não é possível deletar.");
        }
        votacaoRepository.deleteById(id);
    }

    /**
     * Busca as votações que estão ativas no momento da consulta.
     * 
     * @return lista de votações ativas
     */
    public List<Votacao> buscarVotacoesAtivas() {
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        return votacaoRepository.findByInicioBeforeAndFimAfter(agora, agora);
    }

    /**
     * Encerra uma votação definindo seu fim como o momento atual.
     * 
     * @param id identificador da votação a ser encerrada
     * @return a votação atualizada com a data de fim modificada
     * @throws RuntimeException se a votação não for encontrada
     */
    public Votacao encerrarVotacao(Long id) {
        Votacao votacao = buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada com o ID: " + id));
        votacao.setFim(new Timestamp(System.currentTimeMillis()));
        return votacaoRepository.save(votacao);
    }

    /**
     * Verifica se uma votação está ativa, dado o seu ID.
     * 
     * @param votacaoId identificador da votação
     * @return true se a votação estiver ativa, false caso contrário
     * @throws RuntimeException se a votação não for encontrada
     */
    public boolean isVotacaoAtiva(Long votacaoId) {
        Votacao votacao = buscarPorId(votacaoId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada com o ID: " + votacaoId));
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        return votacao.getInicio().before(agora) && votacao.getFim().after(agora);
    }

    /**
     * Cria uma votação personalizada, aplicando regras específicas para este tipo.
     * 
     * @param votacao objeto VotacaoPersonalizada a ser criado
     * @return a votação personalizada criada e salva no banco
     * @throws IllegalArgumentException se a data de início for posterior à data de
     *                                  fim
     */
    public VotacaoPersonalizada criarVotacaoPersonalizada(VotacaoPersonalizada votacao) {
        if (votacao.getInicio().after(votacao.getFim())) {
            throw new IllegalArgumentException("Data de início deve ser antes da data de fim");
        }
        return votacaoRepository.save(votacao);
    }
}
