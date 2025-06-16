package sistema.votacao.usuario.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sistema.votacao.usuario.model.TipoUsuario;
import sistema.votacao.usuario.model.UsuarioModel;
import sistema.votacao.usuario.repository.UsuarioRepository;
import sistema.votacao.voto.repository.VotoRepository;

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

        if (usuarioRepository.findByCpf(usuario.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado.");
        }

        String email = usuario.getEmail();
        if (email.endsWith("@admin.com")) {
            usuario.setTipoUsuario(TipoUsuario.Tipo.ADMIN);
        } else if (email.endsWith("@usuario.com")) {
            usuario.setTipoUsuario(TipoUsuario.Tipo.COMUM);
        } else {
            // Se o domínio não for válido, o próprio serviço lança a exceção.
            throw new TipoUsuarioInvalido("Domínio de email inválido. Utilize @admin.com ou @usuario.com.");
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