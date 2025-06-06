package sistema.votacao.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistema.votacao.Usuario.Model.UsuarioModel ;
import sistema.votacao.Usuario.Model.TipoUsuario;
import sistema.votacao.Usuario.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CadastroController {

    @FXML
    private TextField email;

    @FXML
    private PasswordField senha;

    @FXML
    private TextField cpf;

    @FXML
    private TextField nome;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @FXML
    public void cadastrar(ActionEvent event) {
        String nomeUsuario = nome.getText();
        String emailUsuario = email.getText();
        String senhaUsuario = senha.getText();
        String cpfUsuario = cpf.getText();

        if (nomeUsuario.isEmpty() || emailUsuario.isEmpty() || senhaUsuario.isEmpty() || cpfUsuario.isEmpty()) {
            showAlert(AlertType.ERROR, "Erro no Cadastro", "Por favor, preencha todos os campos.");
            return;
        }

        if (!emailUsuario.contains("@")) {
            showAlert(AlertType.ERROR, "Erro no Cadastro", "Por favor, insira um e-mail válido.");
            return;
        }

        try {
            if (usuarioRepository.findByEmail(emailUsuario).isPresent()) {
                showAlert(AlertType.ERROR, "Erro no Cadastro", "Já existe um usuário cadastrado com este e-mail.");
                return;
            }

            UsuarioModel novoUsuario = new UsuarioModel();
            novoUsuario.setNome(nomeUsuario);
            novoUsuario.setEmail(emailUsuario);
            novoUsuario.setSenha(senhaUsuario); // Lembre-se: em produção, use criptografia (ex: BCrypt)!
            novoUsuario.setCpf(cpfUsuario);
            novoUsuario.setJaVotou(false);
            novoUsuario.setTipo(TipoUsuario.Tipo.COMUM);
            novoUsuario.setDataCadastro(LocalDateTime.now());

            usuarioRepository.save(novoUsuario);

            showAlert(AlertType.INFORMATION, "Cadastro Realizado", "Usuário cadastrado com sucesso!");

            abrirLogin(event);

        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro no Cadastro", "Ocorreu um erro ao cadastrar o usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml")); // Ajuste o caminho
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de Login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}