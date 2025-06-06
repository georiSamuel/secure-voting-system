package sistema.votacao.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import java.awt.*;
import java.net.URI;

@Component
@Scope("prototype") //para controllers do JavaFX
public class LoginController {

    @FXML private Hyperlink cadastroBotao;
    @FXML private PasswordField senhaCampo;
    @FXML private TextField usuarioCampo;

    /**
     * O initialize é um método para que o JavaFx inicialize e configure todos os componentes da interface após a carga do arquivo fxml. Funciona como um "construtor", pode ser removido se todos os campos da interface ja forem definidos no fxml.
     * @author Suelle
     * @since 22/05/25
     */
    @FXML public void initialize() {
        cadastroBotao.setOnAction(this::abrirCadastro);
    }

    /**
     * Método que implementa a lógica de login, o usuário digita suas informações para fazer login no sistema
     * @author Suelle
     * @since 22/05/25
     * @param event
     */
    @FXML void fazerLogin(ActionEvent event) {
        String usuario = usuarioCampo.getText();
        String senha = senhaCampo.getText();

        //Demonstração básica de como ficaria:
        if(usuario.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Erro", "Preencha todos os campos!");
            return;}

        if(usuario.equals("susu") && senha.equals("1234")) {
            abrirTelaUsuario();
        } else {
            mostrarAlerta("Erro", "Usuário ou senha inválidos!");
        }
    }

    /**
     * Método que aciona o evento quando o usuário esqueceu a senha e implementa a lógica da pergunta de segurança para mudar a senha
     * @author Suelle
     * @version 1.0
     * @since 22/05/25
     * @param event
     */
    @FXML void recuperarSenha(ActionEvent event) {
        mostrarAlerta("Recuperação", "Em desenvolvimento!");
    }

    /**
     * Método que será usado para emitir alertas na tela caso o usuário tenha feito algo fora do planejado e não possa prosseguir.
     * @author Suelle
     * @version 1.0
     * @since 22/05/25
     * @param titulo
     * @param mensagem
     */
    @FXML private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Método para abrir o repositório desse projeto no navegador.
     * @author Suelle
     * @version 1.0
     * @since 22/05/25
     */
    @FXML private void abrirRepositorio() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/georiSamuel/secure-voting-system"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que aciona o botão "cadastre-se aqui" e abre a tela pro usuário se cadastrar.
     * @author Suelle
     * @version 1.0
     * @since 31/05/25
     * @param actionEvent
     */
    public void abrirCadastro(ActionEvent actionEvent) {
        try {
            Parent telaDeCadastro = FXMLLoader.load(getClass().getResource("/views/cadastro.fxml"));
            Scene cenaAtual = cadastroBotao.getScene();
            Stage palco = (Stage) cenaAtual.getWindow();
            palco.setScene(new Scene(telaDeCadastro));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void abrirTelaUsuario() {
        try {
            Parent telaUsuario = FXMLLoader.load(getClass().getResource("/views/telaUsuario.fxml"));
            Scene cenaAtual = usuarioCampo.getScene();
            Stage palco = (Stage) cenaAtual.getWindow();
            palco.setScene(new Scene(telaUsuario));
            palco.sizeToScene();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível carregar a tela do usuário.");
        }
    }
}