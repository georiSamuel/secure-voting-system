package sistema.votacao.Usuario.Service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sistema.votacao.Usuario.Model.UsuarioModel;
import sistema.votacao.Usuario.Repository.UsuarioRepository;
import sistema.votacao.Voto.Repository.VotoRepository;

import java.util.Optional;

/**
 * Classe responsável por implementar a lógica de negócios relacionada aos usuários do sistema de votação.
 * @author Horlan
 * @version 1.0
 * @since 20/05/2025
 */

@Data
@Service
public class UsuarioService {

    @Autowired private PasswordEncoder passwordEncoder; // Estou inicializando o Bean referenciadno a interface que o BCrypt implementa (Polimorfismo!)

    @Autowired private final UsuarioRepository usuarioRepository;

    @Autowired private final VotoRepository votoRepository;

    /**
     * Método para cadastrar um novo usuário no sistema.
     * @param usuario
     * @return O usuário cadastrado.
     * @throws RuntimeException se o email já estiver cadastrado ou se o tipo de usuário for nulo.
     * @throws TipoUsuarioInvalido se o tipo de usuário for inválido.
     */
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

    /**
     * Método para buscar um usuário pelo ID.
     * @param id
     * @return Um Optional contendo o usuário se encontrado, ou vazio caso contrário.
     * @throws RuntimeException se o usuário não for encontrado.
     */
    public Optional<UsuarioModel> buscarPorId(Long id) {
        return Optional.ofNullable(
                usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario não encontrado")));
    }

    /**
     * Método para autenticar um usuário com base no email e senha.
     * @param email
     * @param senha
     * @return O usuário autenticado, ou null se a autenticação falhar.
     */
    public UsuarioModel autenticar(String email, String senha) {
        Optional<UsuarioModel> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            UsuarioModel usuario = usuarioOptional.get();
            // Use o passwordEncoder injetado para verificar
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                return usuario;
            }
        }
        return null;
    }
    /**
     * Método para verificar se um usuário já votou em uma votação específica.
     * @param usuarioId ID do usuário a ser verificado.
     * @param votacaoId ID da votação a ser verificada.
     * @return true se o usuário já votou, false caso contrário.
     */
    public boolean verificarSeUsuarioJaVotou(Long usuarioId, Long votacaoId) {
        return votoRepository.existsByUsuarioIdAndVotacaoId(usuarioId, votacaoId); // Verifica se o voto existe
    }

}