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

import java.time.LocalDateTime;
import java.util.Optional; // retorna uma coisa ou outra;eu

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioModel> cadastrar(@RequestBody CadastroRequest cadastroRequest) { // Agora aceita CadastroRequest
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome(cadastroRequest.getNome());
        usuario.setCpf(cadastroRequest.getCpf());
        usuario.setEmail(cadastroRequest.getEmail());
        usuario.setSenha(cadastroRequest.getSenha());
        usuario.setJaVotou(false); // Valor padrão
        usuario.setDataCadastro(LocalDateTime.now());

        if (cadastroRequest.getTipo() != null) {
            usuario.setTipoUsuario(cadastroRequest.getTipo());
        } else {
            usuario.setTipoUsuario(TipoUsuario.Tipo.COMUM);
        }

        UsuarioModel novoUsuario = usuarioService.cadastrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<UsuarioModel> buscarPorId(@PathVariable long id) {
        Optional<UsuarioModel> usuario = usuarioService.buscarPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout realizado com sucesso!");

    }

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
