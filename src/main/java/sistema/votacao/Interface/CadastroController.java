package sistema.votacao.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
 * Controller responsável pela tela de cadastro dos usuários.
 * Gerencia a interação entre a interface de usuário (cadastro.fxml) e a lógica de negócio
 * para registro de novos usuários, inferindo o tipo de usuário pelo domínio do e-mail.
 *
 * @author Suelle
 * @author Horlan
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
     * Construtor padrão da classe CadastroController.
     * <p>
     * Este construtor é utilizado pelos frameworks Spring e JavaFX para instanciar o controller.
     * A inicialização dos componentes da interface e a configuração de dependências
     * ocorrem após a construção do objeto, através das anotações {@literal @FXML} e {@literal @Autowired}.
     * A lógica de inicialização da tela deve ser colocada em um método anotado com {@literal @FXML},
     * como o método {@code initialize()}.
     * </p>
     *
     * @since 10/06/25
     * @version 1.0
     */
    public CadastroController() {
        // Construtor vazio, a inicialização é feita pelo framework.
    }

    /**
     * Método de inicialização do controlador, chamado após o carregamento do FXML.
     *
     * @since 10/06/25
     * @version 1.0
     */
    @FXML public void initialize() {
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
    @FXML private void handleCadastroButton(ActionEvent event) {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String cpf = txtCpf.getText();
        String senha = pwdSenha.getText();
        String confirmarSenha = pwdConfirmarSenha.getText();

        if (nome.isEmpty() || email.isEmpty() || cpf.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro de Cadastro", "Por favor, preencha todos os campos.");
            return;
        }

        // Garante que o cpf deve ter 11 dígitos e todos dever ser numéricos
        if (!cpf.matches("\\d{11}")) {
            showAlert(Alert.AlertType.ERROR, "Erro de Cadastro", "CPF inválido. Deve conter exatamente 11 dígitos numéricos.");
            return;
        }

        if (senha.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Erro de Cadastro", "A senha deve ter no mínimo 6 dígitos.");
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
    @FXML private void handleVoltarLoginButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/login.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) btnVoltarLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Votação");
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
    @FXML private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
