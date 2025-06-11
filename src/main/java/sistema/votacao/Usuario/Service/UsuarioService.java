package sistema.votacao.Usuario.Service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sistema.votacao.Usuario.Model.UsuarioModel;
import sistema.votacao.Usuario.Repository.UsuarioRepository;
import sistema.votacao.Voto.Repository.VotoRepository;
import sistema.votacao.Util.PasswordUtil;

import java.util.Optional;

@Data
@Service
public class UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private final VotoRepository votoRepository;

    // @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, VotoRepository votoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.votoRepository = votoRepository;
    }

    public UsuarioModel cadastrarUsuario(UsuarioModel usuario) { // Parâmetro alterado para UsuarioModel
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        if (usuario.getTipoUsuario() == null) { // Acessando diretamente de UsuarioModel
            throw new TipoUsuarioInvalido("Tipo de usuário não pode ser nulo.");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
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

            if (PasswordUtil.verifyPassword(senha, usuario.getSenha())) {
                return usuario;
            }
        }

        return null;
    }

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
