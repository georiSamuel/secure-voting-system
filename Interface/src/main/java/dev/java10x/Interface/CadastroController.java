package dev.java10x.Interface;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class CadastroController {
    @FXML
    private AnchorPane backgroundCor;
    @FXML
    private AnchorPane telaBranca;
    @FXML
    private Label tituloText;
    @FXML
    private Label descricaoText;
    @FXML
    private TextField email;
    @FXML
    private PasswordField senha;
    @FXML
    private Button botaoCadastrar;
    @FXML
    private Text infoText;
    @FXML
    private Hyperlink voltarText;
    @FXML
    private TextField cpf;
    @FXML
    private TextField nome;

    @FXML
    private void cadastrar() {
        System.out.println("Cadastrando...");
        System.out.println("Nome: " + nome.getText());
        System.out.println("CPF: " + cpf.getText());
        System.out.println("Email: " + email.getText());
        System.out.println("Senha: " + senha.getText());
    }

    @FXML
    private void abrirLogin() {
        System.out.println("Voltando para a tela de login...");
    }

    @FXML
    private void initialize() {
    }
}