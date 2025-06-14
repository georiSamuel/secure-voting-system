

// POSSIVELMENTE NÃO SERÁ USADO, MAS MANTIDO PARA REFERÊNCIA FUTURA

package sistema.votacao.Usuario.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sistema.votacao.Usuario.DTO.LoginRequest;
import sistema.votacao.Usuario.DTO.CadastroRequest;
import sistema.votacao.Usuario.Model.UsuarioModel;
import sistema.votacao.Usuario.Model.TipoUsuario;
import sistema.votacao.Usuario.Service.UsuarioService;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.Optional; // retorna uma coisa ou outra;eu

/**
 * Classe responsável por definir os endpoints (API) para o gerenciamento de usuários no sistema de votação.
 * @author Horlan
 * @version 1.0
 * @since 20/05/2025
 */

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint para cadastrar um novo usuário.
     * @param cadastroRequest Objeto contendo os dados do usuário a ser cadastrado.
     * @return ResponseEntity com o usuário cadastrado e status HTTP 201 (Created).
     */
    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioModel> cadastrar(@RequestBody CadastroRequest cadastroRequest) { // Agora aceita CadastroRequest
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome(cadastroRequest.getNome());
        usuario.setCpf(cadastroRequest.getCpf());
        usuario.setEmail(cadastroRequest.getEmail());
        usuario.setSenha(cadastroRequest.getSenha());
        usuario.setJaVotou(false); // Valor padrão
        usuario.setDataCadastro(LocalDate.now());

        if (cadastroRequest.getTipo() != null) {
            usuario.setTipoUsuario(cadastroRequest.getTipo());
        } else {
            usuario.setTipoUsuario(TipoUsuario.Tipo.COMUM);
        }

        UsuarioModel novoUsuario = usuarioService.cadastrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    /**
     * Endpoint para buscar um usuário pelo ID.
     * @param id ID do usuário a ser buscado.
     * @return ResponseEntity com o usuário encontrado ou status HTTP 404 (Not Found) se não encontrado.
     */
    @GetMapping("id/{id}")
    public ResponseEntity<UsuarioModel> buscarPorId(@PathVariable long id) {
        Optional<UsuarioModel> usuario = usuarioService.buscarPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    /**
     * Endpoint para autenticar um usuário (login). Este método recebe um objeto LoginRequest contendo o email e a senha do usuário.
     * @param loginRequest Objeto contendo o email e a senha do usuário.
     * @param session HttpSession para gerenciar a sessão do usuário autenticado.
     * @return ResponseEntity com uma mensagem de sucesso ou erro de autenticação.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session) {

        UsuarioModel usuario = usuarioService.autenticar(loginRequest.getEmail(), loginRequest.getSenha());

        if (usuario != null) {
            session.setAttribute("usuario", usuario);
            return ResponseEntity.ok("Login realizado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos.");
        }
    }

    /**
     * Endpoint para realizar o logout do usuário, invalidando a sessão atual.
     * @param session HttpSession para gerenciar a sessão do usuário.
     * @return ResponseEntity com uma mensagem de sucesso.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout realizado com sucesso!");

    }

    /**
     * Endpoint para verificar se um usuário está logado.
     * @param session HttpSession para verificar a sessão do usuário.
     * @return ResponseEntity com o nome do usuário logado ou uma mensagem de erro se não houver usuário logado.
     */

    @GetMapping("/logado")
    public ResponseEntity<String> verificarSessao(HttpSession session) {
        UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");

        if (usuario != null) {
            return ResponseEntity.ok("Usuário logado: " + usuario.getNome());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nenhum usuário logado.");
        }
    }
}
