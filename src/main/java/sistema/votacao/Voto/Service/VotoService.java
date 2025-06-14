package sistema.votacao.Voto.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.votacao.Usuario.Model.UsuarioModel;
import sistema.votacao.Usuario.Service.UsuarioService;
import sistema.votacao.Util.HMAC;
import sistema.votacao.Votacao.Model.Votacao;
import sistema.votacao.Votacao.Service.VotacaoService;
import sistema.votacao.Voto.DTO.VotoRequestDTO;
import sistema.votacao.Voto.DTO.VotoResponseDTO;
import sistema.votacao.Voto.Model.OpcaoVoto;
import sistema.votacao.Voto.Model.VotoModel;
import sistema.votacao.Voto.Repository.VotoRepository;
import sistema.votacao.Util.ReproduzirSom;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço responsável pela lógica de negócio relacionada ao registro e consulta de votos.
 */
@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final UsuarioService usuarioService;
    private final VotacaoService votacaoService;
    private final OpcaoVotoService opcaoVotoService;

    // Injeta a chave secreta para o HMAC a partir do application.properties
    @Value("${voting.app.hmac-secret}")
    private String hmacSecret;

    public VotoService(VotoRepository votoRepository, UsuarioService usuarioService, VotacaoService votacaoService,
                       OpcaoVotoService opcaoVotoService) {
        this.votoRepository = votoRepository;
        this.usuarioService = usuarioService;
        this.votacaoService = votacaoService;
        this.opcaoVotoService = opcaoVotoService;
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

    /**
     * Registra um novo voto, garantindo sua integridade através de um HMAC.
     *
     * @param dto O objeto de transferência de dados com as informações do voto.
     * @return Um DTO com a confirmação do voto registrado.
     */
    @Transactional
    public VotoResponseDTO registrarVoto(VotoRequestDTO dto) {
        if (usuarioJaVotou(dto.getUsuarioId(), dto.getVotacaoId())) {
            throw new IllegalStateException("Usuário já votou nesta votação");
        }

        if (!votacaoService.isVotacaoAtiva(dto.getVotacaoId())) {
            throw new IllegalStateException("Votação não está ativa");
        }

        try {
            OpcaoVoto opcao = opcaoVotoService.buscarPorId(dto.getOpcaoVotoId());
            Votacao votacao = votacaoService.buscarPorId(dto.getVotacaoId())
                    .orElseThrow(() -> new RuntimeException("Votação não encontrada com o ID: " + dto.getVotacaoId()));
            UsuarioModel usuario = usuarioService.buscarPorId(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + dto.getUsuarioId()));

            VotoModel voto = new VotoModel();
            voto.setOpcaoVoto(opcao);
            voto.setVotacao(votacao);
            voto.setUsuario(usuario);
            voto.setDataHoraVoto(LocalDateTime.now());

            // 1. Criar a string com os dados do voto para gerar o selo de integridade
            String dadosParaAutenticar = String.format("usuarioId:%d;votacaoId:%d;opcaoId:%d;timestamp:%s",
                    dto.getUsuarioId(),
                    dto.getVotacaoId(),
                    dto.getOpcaoVotoId(),
                    voto.getDataHoraVoto().toString());

            // 2. Gerar o HMAC (o "selo") usando a classe utilitária
            String hmac = HMAC.calcularHmac(dadosParaAutenticar, hmacSecret);
            voto.setVotoHmac(hmac);

            // 3. Salvar o voto no banco de dados
            VotoModel salvo = votoRepository.save(voto);

            // 4. Atualizar status e contagem
            usuarioService.atualizarStatusVoto(dto.getUsuarioId(), true);
            opcaoVotoService.incrementarVotos(dto.getOpcaoVotoId());

            // Tocando som após operação de voto realizado
            ReproduzirSom.tocarBeepUrna(ReproduzirSom.AUDIO_URNA_PATH);

            // 5. Retornar a resposta (sem o campo 'votoCriptografado')
            return new VotoResponseDTO(
                    salvo.getId(),
                    dto.getUsuarioId(),
                    dto.getVotacaoId(),
                    salvo.getDataHoraVoto());

        } catch (Exception e) {
            // Captura exceções (ex: do HMAC) e lança uma exceção de runtime
            throw new RuntimeException("Falha ao registrar o voto de forma segura.", e);
        }
    }

    @Transactional(readOnly = true)
    public List<VotoModel> buscarVotosPorVotacao(Long votacaoId) {
        return votoRepository.findByVotacaoId(votacaoId);
    }

    @Transactional(readOnly = true)
    public boolean usuarioJaVotou(Long usuarioId, Long votacaoId) {
        return votoRepository.existsByUsuarioIdAndVotacaoId(usuarioId, votacaoId);
    }


    /**
     * Verifica a integridade de um registro de voto comparando seu HMAC armazenado
     * com um HMAC recém-calculado a partir dos dados do voto.
     *
     * @param votoId O ID do voto a ser verificado.
     * @return true se o voto for íntegro (o selo corresponde), false caso contrário.
     * @throws Exception se ocorrer um erro durante o processo (ex: voto não encontrado).
     */
    @Transactional(readOnly = true)
    public boolean verificarIntegridadeVoto(Long votoId) throws Exception {
        // 1. Buscar o voto no banco de dados
        VotoModel voto = buscarPorId(votoId);

        // 2. Reconstruir a string de dados original EXATAMENTE como foi criada durante o registro
        String dadosParaAutenticar = String.format("usuarioId:%d;votacaoId:%d;opcaoId:%d;timestamp:%s",
                voto.getUsuario().getId(),
                voto.getVotacao().getId(),
                voto.getOpcaoVoto().getId(),
                voto.getDataHoraVoto().toString());

        // 3. Recalcular o HMAC com os dados atuais do voto
        String hmacRecalculado = HMAC.calcularHmac(dadosParaAutenticar, hmacSecret);

        // 4. Comparar o HMAC armazenado com o HMAC recalculado de forma segura
        return hmacRecalculado.equals(voto.getVotoHmac());
    }
}