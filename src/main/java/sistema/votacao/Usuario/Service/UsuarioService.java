package sistema.votacao.Usuario.Service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sistema.votacao.Usuario.Model.UsuarioModel;
import sistema.votacao.Usuario.Repository.UsuarioRepository;
import sistema.votacao.Voto.Repository.VotoRepository;
import sistema.votacao.Util.Password; // Certifique-se de que esta classe está correta e funcional

import java.util.Optional;

@Data
@Service
public class UsuarioService {

    // O PasswordEncoder é usado para codificar senhas no cadastro
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Repositório para operações de persistência de usuários
    private final UsuarioRepository usuarioRepository;

    // Repositório para operações de persistência de votos (se necessário para lógica futura)
    private final VotoRepository votoRepository;

    /**
     * Construtor da classe UsuarioService.
     * Injeta as dependências necessárias para o serviço.
     * @param usuarioRepository Repositório de usuários.
     * @param votoRepository Repositório de votos.
     */
    @Autowired // Anotação para injeção de dependência pelo Spring
    public UsuarioService(UsuarioRepository usuarioRepository, VotoRepository votoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.votoRepository = votoRepository;
    }

    /**
     * Cadastra um novo usuário no sistema.
     * Antes de salvar, verifica se o email já está cadastrado e se o tipo de usuário é válido.
     * A senha do usuário é codificada antes de ser armazenada no banco de dados.
     * @param usuario O objeto UsuarioModel a ser cadastrado.
     * @return O UsuarioModel salvo no banco de dados.
     * @throws RuntimeException se o email já estiver cadastrado ou o tipo de usuário for nulo.
     */
    public UsuarioModel cadastrarUsuario(UsuarioModel usuario) {
        // Verifica se já existe um usuário com o email fornecido
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        // Verifica se o tipo de usuário não é nulo
        if (usuario.getTipoUsuario() == null) {
            throw new TipoUsuarioInvalido("Tipo de usuário não pode ser nulo.");
        }

        // Codifica a senha do usuário antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    /**
     * Busca um usuário pelo seu ID.
     * @param id O ID do usuário a ser buscado.
     * @return Um Optional contendo o UsuarioModel se encontrado, ou um Optional vazio.
     * @throws RuntimeException se o usuário não for encontrado.
     */
    public Optional<UsuarioModel> buscarPorId(Long id) {
        return Optional.ofNullable(
                usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario não encontrado")));
    }

    /**
     * Autentica um usuário com base no email e senha fornecidos.
     * @param email O email do usuário.
     * @param senha A senha em texto puro do usuário.
     * @return O objeto UsuarioModel se a autenticação for bem-sucedida, caso contrário, retorna null.
     */
    public UsuarioModel autenticar(String email, String senha) {
        // Tenta encontrar o usuário pelo email
        Optional<UsuarioModel> usuarioOptional = usuarioRepository.findByEmail(email);

        // Se o usuário for encontrado e a senha corresponder, retorna o usuário
        if (usuarioOptional.isPresent()) {
            UsuarioModel usuario = usuarioOptional.get();
            // Utiliza o método verifyPassword da classe Password para comparar a senha fornecida
            // com a senha armazenada (hashed)
            if (Password.verifyPassword(senha, usuario.getSenha())) {
                return usuario;
            }
        }
        // Retorna null se o usuário não for encontrado ou a senha estiver incorreta
        return null;
    }

    /**
     * Verifica se um usuário já votou em uma votação específica.
     * @param usuarioId O ID do usuário.
     * @param votacaoId O ID da votação.
     * @return true se o usuário já votou, false caso contrário.
     */
    public boolean verificarSeUsuarioJaVotou(Long usuarioId, Long votacaoId) {
        return votoRepository.existsByUsuarioIdAndVotacaoId(usuarioId, votacaoId);
    }

    /**
     * Atualiza o status de votação de um usuário.
     * @param usuarioId O ID do usuário.
     * @param status O novo status de votação (true para já votou, false para ainda não votou).
     * @throws RuntimeException se o usuário não for encontrado.
     */
    public void atualizarStatusVoto(Long usuarioId, boolean status) {
        UsuarioModel usuario = this.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setJaVotou(status);
        usuarioRepository.save(usuario);
    }
}
