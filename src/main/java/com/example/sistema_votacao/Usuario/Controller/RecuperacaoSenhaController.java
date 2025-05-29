package com.example.sistema_votacao.Usuario.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sistema_votacao.Usuario.Service.RecuperacaoSenhaService;

@RestController
@RequestMapping("recuperacao")
public class RecuperacaoSenhaController {

    @Autowired
    private RecuperacaoSenhaService service;

    @PostMapping("/solicitar")
    public ResponseEntity<String> redifinir(@RequestParam String email){

        String token = service.gerarToken(email);

        return ResponseEntity.ok("Token" + token);
    }

    public ResponseEntity<String> redefinir(@RequestParam String token, @RequestParam String novaSenha){

        service.redefinirSenha(token, novaSenha);

        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }




    
}
