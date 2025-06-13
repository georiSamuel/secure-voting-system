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
 * @author Lethycia
 */
@Getter
@Setter
@Service
public class VotacaoService {

    private final VotacaoRepository votacaoRepository;
    private final VotoRepository votoRepository; // Injete VotoRepository aqui

    public VotacaoService(VotacaoRepository votacaoRepository, VotoRepository votoRepository) { // Atualize o construtor
        this.votacaoRepository = votacaoRepository;
        this.votoRepository = votoRepository; // Inicialize votoRepository
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

    public Optional<Votacao> buscarPorId(Long id) {
        return votacaoRepository.findById(id);
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
        Votacao votacao = buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada com o ID: " + id));
        votacao.setFim(new Timestamp(System.currentTimeMillis()));
        return votacaoRepository.save(votacao);
    }

    public boolean isVotacaoAtiva(Long votacaoId) {
        Votacao votacao = buscarPorId(votacaoId)
                .orElseThrow(() -> new RuntimeException("Votação não encontrada com o ID: " + votacaoId));
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        return votacao.getInicio().before(agora) && votacao.getFim().after(agora);
    }

    public VotacaoPersonalizada criarVotacaoPersonalizada(VotacaoPersonalizada votacao) {
        // como a votação é personalizada ela pode ter regras específicas, como permitir
        // voto múltiplo
        if (votacao.getInicio().after(votacao.getFim())) {
            throw new IllegalArgumentException("Data de início deve ser antes da data de fim");
        } // exemplo de regra específica
        return votacaoRepository.save(votacao);
    }

    /**
     * Gera o resultado para uma votação personalizada.
     *
     * @param votacaoId O ID da votação personalizada.
     * @return Uma string contendo o resultado da votação.
     */
    public String gerarResultadoPersonalizado(Long votacaoId) {
        // Recupera a votação pelo ID
        Votacao votacao = votacaoRepository.findById(votacaoId)
                .orElseThrow(() -> new RuntimeException("Votação personalizada não encontrada com o ID: " + votacaoId));

        if (!(votacao instanceof VotacaoPersonalizada)) {
            throw new IllegalArgumentException("A votação com ID " + votacaoId + " não é do tipo Personalizada.");
        }

        long totalVotos = votoRepository.countByVotacaoId(votacaoId);
        return "Resultado da votação personalizada - Total de votos: " + totalVotos;
    }
}
