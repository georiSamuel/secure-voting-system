package sistema.votacao.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink; // Importe Hyperlink
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sistema.votacao.Usuario.Model.TipoUsuario;
import sistema.votacao.Usuario.Model.UsuarioModel;
import sistema.votacao.Usuario.Service.TipoUsuarioInvalido;
import sistema.votacao.Usuario.Service.UsuarioService;
import sistema.votacao.SistemaVotacaoApplication;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe responsável pela tela de cadastro dos usuários.
 * Gerencia a interação entre a interface de usuário (cadastro.fxml) e a lógica de negócio
 * para registro de novos usuários, inferindo o tipo de usuário pelo domínio do e-mail.
 *
 * @author Suelle &
 * @version 1.0
 * @since 26/05/25
 */
@Component
public class CadastroController {
    @Autowired private UsuarioService usuarioService;

    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCpf;
    @FXML private PasswordField pwdSenha;
    @FXML private PasswordField pwdConfirmarSenha;
    @FXML private Hyperlink btnVoltarLogin;

    /**
     * Método de inicialização do controlador, chamado após o carregamento do FXML.
     *
     * @since 10/06/25
     * @version 1.0
     */
    @FXML
    public void initialize() {
    }

    /**
     * Manipula o evento de clique do botão "Cadastrar".
     * Coleta as informações dos campos, valida-as e tenta cadastrar o usuário.
     * O tipo de usuário é determinado com base no domínio do email.
     *
     * @param event O evento de ação que disparou este método.
     * @version 1.0
     * @since 10/06/25
     */
    @FXML
    private void handleCadastroButton(ActionEvent event) {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String cpf = txtCpf.getText();
        String senha = pwdSenha.getText();
        String confirmarSenha = pwdConfirmarSenha.getText();

        // Validação básica
        if (nome.isEmpty() || email.isEmpty() || cpf.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro de Cadastro", "Por favor, preencha todos os campos.");
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            showAlert(Alert.AlertType.ERROR, "Erro de Cadastro", "As senhas não coincidem.");
            return;
        }

        TipoUsuario.Tipo tipoUsuario;
        if (email.endsWith("@admin.com")) {
            tipoUsuario = TipoUsuario.Tipo.ADMIN;
        } else if (email.endsWith("@usuario.com")) {
            tipoUsuario = TipoUsuario.Tipo.COMUM;
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro de Cadastro", "Domínio de email inválido. Utilize @admin.com ou @usuario.com.");
            return;
        }

        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setCpf(cpf);
        usuario.setSenha(senha);
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setJaVotou(false);
        usuario.setDataCadastro(LocalDate.now());

        try {
            usuarioService.cadastrarUsuario(usuario);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Usuário cadastrado com sucesso!");

            txtNome.clear();
            txtEmail.clear();
            txtCpf.clear();
            pwdSenha.clear();
            pwdConfirmarSenha.clear();

            handleVoltarLoginButton(event);

        } catch (TipoUsuarioInvalido e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", e.getMessage());

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Cadastro", "Ocorreu um erro ao cadastrar o usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Manipula o evento de clique do botão "Voltar para Login".
     * Carrega a tela de login (Login.fxml).
     *
     * @version 1.0
     * @since 10/06/25
     * @param event O evento de ação que disparou este método.
     */
    @FXML
    private void handleVoltarLoginButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/login.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) btnVoltarLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de login.");
            e.printStackTrace();
        }
    }

    /**
     * Exibe um alerta na tela.
     *
     *
     * @version 1.0
     * @since 10/06/25
     * @param alertType O tipo de alerta (INFORMATION, ERROR, WARNING, etc.).
     * @param title O título do alerta.
     * @param message A mensagem a ser exibida no alerta.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
