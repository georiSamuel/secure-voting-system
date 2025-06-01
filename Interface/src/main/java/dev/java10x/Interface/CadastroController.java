package dev.java10x.Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CadastroController {
    @FXML private AnchorPane backgroundCor;
    @FXML private AnchorPane telaBranca;
    @FXML private Label tituloText;
    @FXML private Label descricaoText;
    @FXML private TextField email;
    @FXML private PasswordField senha;
    @FXML private Button botaoCadastrar;
    @FXML private Text infoText;
    @FXML private Hyperlink botaoVoltar;
    @FXML private TextField cpf;
    @FXML private TextField nome;

    @FXML
    private void cadastrar() {
        System.out.println("Cadastrando...");
        System.out.println("Nome: " + nome.getText());
        System.out.println("CPF: " + cpf.getText());
        System.out.println("Email: " + email.getText());
        System.out.println("Senha: " + senha.getText());
    }

    //método utilitário para mudar de tela
    @FXML private void abrirLogin() {
        try {
            Parent telaDeCadastro = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
            Scene cenaAtual = botaoVoltar.getScene();
            Stage palco = (Stage) cenaAtual.getWindow();
            palco.setScene(new Scene(telaDeCadastro));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void initialize() {
    }
}