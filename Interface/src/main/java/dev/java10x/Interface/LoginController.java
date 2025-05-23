package dev.java10x.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import java.net.URI;

@Component
@Scope("prototype") //para controllers do JavaFX
public class LoginController {

    @FXML private ImageView imagem_icon;
    @FXML private Hyperlink cadastroBotao;
    @FXML private Hyperlink linkGithub;
    @FXML private AnchorPane backgroundCor;
    @FXML private Button botaoLogin;
    @FXML private Label descricaoText;
    @FXML private Hyperlink esqueceuSenha;
    @FXML private PasswordField senhaCampo;
    @FXML private AnchorPane telaBranca;
    @FXML private Label tituloText;
    @FXML private TextField usuarioCampo;
    @FXML private Text usuariotext;

    @FXML
    public void initialize() {
    }

    @FXML
    void fazerLogin(ActionEvent event) {
        String usuario = usuarioCampo.getText();
        String senha = senhaCampo.getText();

        //Demonstração básica de como ficaria:
        if(usuario.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Erro", "Preencha todos os campos!");
            return;}

        if(usuario.equals("susu") && senha.equals("1234")) {
            mostrarAlerta("Sucesso", "Login realizado com sucesso!");
        } else {
            mostrarAlerta("Erro", "Usuário ou senha inválidos!");
        }
    }

    @FXML
    void recuperarSenha(ActionEvent event) {
        mostrarAlerta("Recuperação", "Em desenvolvimento!");
    }

    @FXML
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    @FXML
    private void abrirRepositorio() {
        try {
            java.awt.Desktop.getDesktop().browse(new URI("https://github.com/georiSamuel/secure-voting-system"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void abrirCadastro(ActionEvent actionEvent) {
        //to-do
    }
}