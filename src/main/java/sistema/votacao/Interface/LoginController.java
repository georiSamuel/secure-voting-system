package sistema.votacao.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene; // Importação correta para Scene
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import sistema.votacao.SistemaVotacaoApplication; // Importa a aplicação principal

import java.awt.Desktop; // <--- CORREÇÃO: Importa especificamente Desktop
import java.io.IOException;
import java.net.URI; // <--- CORREÇÃO: Importa especificamente URI
import java.util.Objects;

// Removida importação "import java.awt.*;" para evitar conflito com TextField

@Component // Permite que o Spring gerencie este controlador
public class LoginController {

    @FXML private Hyperlink cadastroBotao;
    @FXML private PasswordField senhaCampo;
    @FXML private TextField usuarioCampo; // Campo de texto para o nome de usuário
    // Adicionado para completar os FXMLs que aparecem no seu FXML do Login,
    // caso não estejam já declarados no seu código
    @FXML private Hyperlink esqueceuSenha;
    @FXML private Button botaoLogin;
    @FXML private Hyperlink linkGithub;


    /**
     * O initialize é um método para que o JavaFx inicialize e configure todos os componentes da interface
     * após a carga do arquivo fxml. Funciona como um "construtor".
     * Pode ser removido se todos os campos da interface já forem definidos no fxml.
     * @author Suelle
     * @since 22/05/25
     */
    @FXML public void initialize() {
        // Se 'cadastroBotao' não for null, configura o onAction
        if (cadastroBotao != null) {
            cadastroBotao.setOnAction(this::abrirCadastro);
        }
        // Garanta que os outros botões também tenham seus onActions configurados se não estiverem no FXML
        // Por exemplo, se eles não tiverem onAction direto no FXML (descomente e ajuste se necessário):
        // if (botaoLogin != null) botaoLogin.setOnAction(this::fazerLogin);
        // if (esqueceuSenha != null) esqueceuSenha.setOnAction(this::recuperarSenha);
        // if (linkGithub != null) linkGithub.setOnAction(this::abrirRepositorio);
    }

    /**
     * Método que implementa a lógica de login. O usuário digita suas informações para fazer login no sistema.
     * @author Suelle
     * @since 22/05/25
     * @param event O evento de ação que disparou este método.
     */
    @FXML void fazerLogin(ActionEvent event) {
        String usuario = usuarioCampo.getText();
        String senha = senhaCampo.getText();

        // Demonstração básica de como ficaria:
        if(usuario.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Erro", "Preencha todos os campos!");
            return;
        }

        if(usuario.equals("susu") && senha.equals("1234")) {
            abrirTelaUsuario();
        } else {
            mostrarAlerta("Erro", "Usuário ou senha inválidos!");
        }
    }

    /**
     * Método que aciona o evento quando o usuário esqueceu a senha e implementa a lógica da pergunta de segurança
     * para mudar a senha.
     * @author Suelle
     * @version 1.0
     * @since 22/05/25
     * @param event O evento de ação que disparou este método.
     */
    @FXML void recuperarSenha(ActionEvent event) {
        mostrarAlerta("Recuperação", "Em desenvolvimento!");
    }

    /**
     * Método que será usado para emitir alertas na tela caso o usuário tenha feito algo fora do planejado e não possa prosseguir.
     * @author Suelle
     * @version 1.0
     * @since 22/05/25
     * @param titulo O título do alerta.
     * @param mensagem A mensagem a ser exibida no alerta.
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
     * Método que aciona o botão "cadastre-se aqui" e abre a tela para o usuário se cadastrar.
     * @author Suelle
     * @version 1.0
     * @since 31/05/25
     * @param actionEvent O evento de ação que disparou este método.
     */
    public void abrirCadastro(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/cadastro.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent telaDeCadastro = loader.load();
            Scene cenaAtual = cadastroBotao.getScene();
            Stage palco = (Stage) cenaAtual.getWindow();
            palco.setScene(new Scene(telaDeCadastro));

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível carregar a tela de cadastro.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Ocorreu um erro inesperado ao abrir a tela de cadastro.");
        }
    }

    private void abrirTelaUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/telaUsuario.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent telaUsuario = loader.load();
            if (usuarioCampo != null) {
                Scene cenaAtual = usuarioCampo.getScene();
                Stage palco = (Stage) cenaAtual.getWindow();
                palco.setScene(new Scene(telaUsuario));
                palco.sizeToScene();
            } else {
                mostrarAlerta("Erro Interno", "Campo de usuário não foi inicializado corretamente (usuariocampo é null).");
            }


        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível carregar a tela do usuário.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Ocorreu um erro inesperado ao abrir a tela do usuário.");
        }
    }
}
