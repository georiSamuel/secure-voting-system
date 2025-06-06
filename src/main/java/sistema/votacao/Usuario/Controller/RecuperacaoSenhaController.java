package sistema.votacao.Usuario.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sistema.votacao.Usuario.Service.RecuperacaoSenhaService;

@RestController
@RequestMapping("recuperacao")
public class RecuperacaoSenhaController {

    @Autowired
    private RecuperacaoSenhaService service;

    @PostMapping("/solicitar")
    public ResponseEntity<String> solicitar(@RequestParam String email) {
        String token = service.gerarToken(email);
        // return ResponseEntity.ok("Token gerado: " + token); //isso pode ser melhorado
        // para enviar um email com o token, não está seguro porque o token é exposto na
        // resposta
        System.out.println("Token gerado (para testes): " + token);
        return ResponseEntity.ok("Se o e-mail estiver cadastrado, você receberá instruções.");
    }

    @PostMapping("/redefinir")
    public ResponseEntity<String> redefinir(@RequestParam String token, @RequestParam String novaSenha) {
        service.redefinirSenha(token, novaSenha);
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }
}
