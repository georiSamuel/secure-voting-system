package sistema.votacao.Usuario.Service;

import org.springframework.stereotype.Service;
import sistema.votacao.Usuario.Model.UsuarioModel;
import sistema.votacao.Usuario.Model.TipoUsuario.Tipo;
import sistema.votacao.Usuario.Repository.UsuarioRepository;
import sistema.votacao.Voto.Repository.VotoRepository;
import sistema.votacao.cryptography.src.cryptographyUtil.PasswordUtil;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UsuarioService {

    // @Autowired
    private UsuarioRepository usuarioRepository;

    // @Autowired
    private VotoRepository votoRepository;

    // @Autowired

    // NÃO ENTENDI A PRESENÇA DESSE CONSTRUTOR AQUI - tira a função do autowired
    public UsuarioService(UsuarioRepository usuarioRepository, VotoRepository votoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.votoRepository = votoRepository;
    }

    public UsuarioModel cadastrarUsuario(UsuarioModel usuario) {

        String email = usuario.getEmail();

        // Verifica se o email já está cadastrado
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        // Define o tipo com base no domínio do email
        if (email.endsWith("@admin.com")) {
            if (usuarioRepository.existsByTipo(Tipo.ADMIN)) {
                throw new RuntimeException("Já existe um administrador cadastrado no sistema.");
            }
            usuario.setTipo(Tipo.ADMIN);
        } else if (email.endsWith("@usuario.com")) {
            usuario.setTipo(Tipo.COMUM);
        } else {
            throw new TipoUsuarioInvalido(email);
        }

        // Usar sua função personalizada
        usuario.setSenha(PasswordUtil.hashPassword(usuario.getSenha()));
        usuario.setDataCadastro(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }


    public Optional<UsuarioModel> buscarPorId(Long id) {
        return Optional.ofNullable(
                usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario não encontrado")));
    }

    public UsuarioModel autenticar(String email, String senha) {
        Optional<UsuarioModel> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            UsuarioModel usuario = usuarioOptional.get();

            // Usar sua função personalizada para verificar a senha
            if (PasswordUtil.verifyPassword(senha, usuario.getSenha())) {
                return usuario;
            }
        }

        return null;
    }

    // horlan, tem problema eu colocar isso aqui?
    public boolean verificarSeUsuarioJaVotou(Long usuarioId, Long votacaoId) {
        return votoRepository.existsByUsuarioIdAndVotacaoId(usuarioId, votacaoId); // Verifica se o voto existe
    }

    public void atualizarStatusVoto(Long usuarioId, boolean status) {
        UsuarioModel usuario = this.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado")); // Verifica se o usuário existe
        usuario.setJaVotou(status);
        usuarioRepository.save(usuario);
    }
}
