package com.example.sistema_votacao.Usuario.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sistema_votacao.Usuario.DTO.LoginRequest;
import com.example.sistema_votacao.Usuario.Model.UsuarioModel;
import com.example.sistema_votacao.Usuario.Service.UsuarioService;

import jakarta.servlet.http.HttpSession;

import java.util.Optional; // retorna uma coisa ou outra;eu

@RestController
@RequestMapping("/api/cadastro")
public class UsuarioController {

    @Autowired // injeção de instância automática, ou seja, a classejá receb os seus objetos prontos, em vez de utilizar o new
    private UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioModel> cadastrar(@RequestBody UsuarioModel usuario){ // "ReponseEntity" - controla a resposta que a HTTP envia, incluindo corpo, status HTTP (200,201, 404, 500);
        UsuarioModel novoUsuario = usuarioService.cadastrarUsuario(usuario);
        return ResponseEntity.ok(novoUsuario);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<UsuarioModel> buscarPorId(@PathVariable long id){
        Optional<UsuarioModel> usuario = usuarioService.buscarPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session){

        UsuarioModel usuario = usuarioService.autenticar(loginRequest.getEmail(), loginRequest.getSenha());

        if(usuario != null){
            session.setAttribute("usuario", usuario);
            return ResponseEntity.ok("Login realizado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos.");    
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session){
        session.invalidate(); // invalida a sessão do usuário --> destrói a sessão atual e remove todos os dados associados a ela
        return ResponseEntity.ok("Logout realizado com sucesso!");
        
    }

    @GetMapping("/logado")
    public ResponseEntity<String> verificarSessao(HttpSession session){
        UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");

        if(usuario != null){
            return ResponseEntity.ok("Usuário logado: " + usuario.getNome());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nenhum usuário logado.");
        }
    }

    
}
