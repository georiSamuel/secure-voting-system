package sistema.votacao.voto.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.votacao.opcao_voto.model.OpcaoVoto;
import sistema.votacao.opcao_voto.service.OpcaoVotoService;
import sistema.votacao.usuario.model.UsuarioModel;
import sistema.votacao.usuario.service.UsuarioService;
import sistema.votacao.util.HMAC;
import sistema.votacao.votacao.model.Votacao;
import sistema.votacao.votacao.service.VotacaoService;
import sistema.votacao.voto.dto.VotoRequestDTO;
import sistema.votacao.voto.dto.VotoResponseDTO;
import sistema.votacao.voto.model.VotoModel;
import sistema.votacao.voto.repository.VotoRepository;
import sistema.votacao.util.ReproduzirSom;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Serviço responsável pela lógica de negócio relacionada ao registro e consulta
 * de votos.
 * Fornece métodos para criar, buscar, deletar votos e registrar votos com
 * segurança usando HMAC.
 * 
 * @author Lethycia
 * @author Horlan
 * @version 2.0
 * @since 29/05/25
 */
@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final UsuarioService usuarioService;
    private final VotacaoService votacaoService;
    private final OpcaoVotoService opcaoVotoService;

    // Injeta a chave secreta para o HMAC a partir do application.properties
    @Value("${VOTING_APP_HMAC_SECRET}")
    private String hmacSecret;

    /**
     * Construtor do serviço de votos.
     * @param votoRepository
     * @param usuarioService
     * @param votacaoService
     * @param opcaoVotoService
     */
    public VotoService(VotoRepository votoRepository, UsuarioService usuarioService, VotacaoService votacaoService,
            OpcaoVotoService opcaoVotoService) {
        this.votoRepository = votoRepository;
        this.usuarioService = usuarioService;
        this.votacaoService = votacaoService;
        this.opcaoVotoService = opcaoVotoService;
    }

    /**
     *  Cria um novo voto no sistema.
     *  Este método é responsável por persistir um voto no banco de dados.
     *  Ele não verifica a integridade do voto, apenas o salva.
     * @param voto
     * @return  O voto criado e salvo no banco de dados.
     */
    @Transactional
    public VotoModel criarVoto(VotoModel voto) {
        return votoRepository.save(voto);
    }

    /**
     *  Busca todos os votos registrados no sistema.
     * @return uma lista de todos os votos.
     */
    @Transactional(readOnly = true)
    public List<VotoModel> buscarTodosVotos() {
        return votoRepository.findAll();
    }

    /**
     *  Busca um voto específico pelo seu ID.
     *  Se o voto não for encontrado, lança uma exceção.
     * @param id
     * @return O voto encontrado com o ID especificado.
     * @throws RuntimeException se o voto não for encontrado.
     */
    @Transactional(readOnly = true)
    public VotoModel buscarPorId(Long id) {
        return votoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voto não encontrado com ID: " + id));
    }

    /**
     * Este método verifica se o voto existe antes de tentar deletá-lo.
     * @param id
     * @throws RuntimeException se o voto não for encontrado.
     */
    @Transactional
    public void deletarVoto(Long id) {
        if (!votoRepository.existsById(id)) {
            throw new RuntimeException("Voto não encontrado com ID: " + id);
        }
        votoRepository.deleteById(id);
    }

    /**
     * Registra um novo voto, garantindo sua integridade através de um HMAC.
     * @param dto O objeto de transferência de dados com as informações do voto.
     * @return Um DTO com a confirmação do voto registrado.
     * @throws IllegalStateException se o usuário já tiver votado ou se a votação não estiver ativa.
     * @throws RuntimeException se ocorrer um erro ao buscar a votação ou o usuário, ou ao gerar o HMAC.
     * @throws RuntimeException se ocorrer um erro ao registrar o voto.
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

            /*
             * Trunca o tempo para microssegundos ANTES de definir no objeto e usar no HMAC.
             * Isso é necessário porque A maioria dos bancos de dados,
             * incluindo o MySQL, não armazena a precisão total de nanossegundos sendo que o
             * HAMC PRECISA do valor exato para realizar a verivicação por hash
             */
            LocalDateTime timestampTruncado = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
            voto.setDataHoraVoto(timestampTruncado);

            // Passos de geração de hash com chave secreta HMAC:

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

            // 4. Atualizar contagem
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

    /**
     * Busca todos os votos associados a uma votação específica.
     * @param votacaoId ID da votação para a qual os votos serão buscados.
     * @return Lista de votos associados à votação especificada.
     */
    @Transactional(readOnly = true)
    public List<VotoModel> buscarVotosPorVotacao(Long votacaoId) {
        return votoRepository.findByVotacaoId(votacaoId);
    }

    /**
     *  Verifica se um usuário já votou em uma votação específica.
     * @param usuarioId
     * @param votacaoId
     * @return  true se o usuário já tiver votado, false caso contrário.
     */
    @Transactional(readOnly = true)
    public boolean usuarioJaVotou(Long usuarioId, Long votacaoId) {
        return votoRepository.existsByUsuarioIdAndVotacaoId(usuarioId, votacaoId);
    }

    /**
     * Verifica a integridade de um registro de voto comparando seu HMAC armazenado
     * com um HMAC recém-calculado a partir dos dados do voto.
     * @param votoId O ID do voto a ser verificado.
     * @return true se o voto for íntegro (o selo corresponde), false casocontrário.
     * @throws Exception se ocorrer um erro durante o processo (ex: voto nãoencontrado).
     */
    @Transactional(readOnly = true)
    public boolean verificarIntegridadeVoto(Long votoId) throws Exception {
        // 1. Buscar o voto no banco de dados
        VotoModel voto = buscarPorId(votoId);

        // Apenas para garantir, mesmo que a leitura do banco já deva vir truncada, é
        // uma boa prática re-truncar para garantir consistência total.
        LocalDateTime timestampVerificacao = voto.getDataHoraVoto().truncatedTo(ChronoUnit.MICROS);

        // 2. Reconstruir a string de dados original EXATAMENTE como foi criada
        String dadosParaAutenticar = String.format("usuarioId:%d;votacaoId:%d;opcaoId:%d;timestamp:%s",
                voto.getUsuario().getId(),
                voto.getVotacao().getId(),
                voto.getOpcaoVoto().getId(),
                timestampVerificacao.toString()); // Usa a string do timestamp truncado

        // 3. Recalcular o HMAC
        String hmacRecalculado = HMAC.calcularHmac(dadosParaAutenticar, hmacSecret);

        // 4. Comparar
        return hmacRecalculado.equals(voto.getVotoHmac());
    }
}
