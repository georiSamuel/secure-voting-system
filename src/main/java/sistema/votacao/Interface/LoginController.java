package sistema.votacao.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sistema.votacao.SistemaVotacaoApplication;
import sistema.votacao.Usuario.Model.UsuarioModel;
import sistema.votacao.Usuario.Model.TipoUsuario;
import sistema.votacao.Usuario.Service.UsuarioService;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

/**
 * Classe responsável pela tela de Login do JavaFX.
 *
 * @author Suelle
 * @version 1.1
 * @since 25/06/25
 */
@Component
public class LoginController {

    @FXML private Hyperlink cadastroBotao;
    @FXML private PasswordField senhaCampo;
    @FXML private TextField usuarioCampo;
    @FXML private Button botaoLogin;
    @FXML private Hyperlink linkGithub;

    @Autowired private UsuarioService usuarioService;

    /**
     * O initialize é um método para que o JavaFx inicialize e configure todos os componentes da interface
     * após a carga do arquivo fxml. Funciona como um "construtor".
     *
     * @since 22/05/25
     * @version 1.1
     */
    @FXML public void initialize() {
        if (cadastroBotao != null) {
            cadastroBotao.setOnAction(this::abrirCadastro);
        }

        if (botaoLogin != null) {
            botaoLogin.setOnAction(this::fazerLogin);
        }

        if (linkGithub != null) {
            linkGithub.setOnAction(this::abrirRepositorio);
        }
    }

    /**
     * Método que implementa a lógica de login. O usuário digita suas informações para fazer login no sistema.
     * Busca por usuários no banco de dados e redireciona para a tela correta com base no domínio do email.
     *
     * @version 1.0
     * @since 25/06/25
     * @param event O evento de ação que disparou este método.
     */
    @FXML void fazerLogin(ActionEvent event) {
        String email = usuarioCampo.getText();
        String senha = senhaCampo.getText();

        if(email.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Erro de Login", "Por favor, preencha todos os campos.");
            return;
        }

        try {
            UsuarioModel usuarioAutenticado = usuarioService.autenticar(email, senha);

            if (usuarioAutenticado != null) {
                if (usuarioAutenticado.getTipoUsuario() == TipoUsuario.Tipo.ADMIN) {
                    abrirTelaAdmin();
                } else if (usuarioAutenticado.getTipoUsuario() == TipoUsuario.Tipo.COMUM) {
                    abrirTelaUsuario();
                } else {
                    mostrarAlerta("Erro de Tipo", "Tipo de usuário não reconhecido.");
                }
            } else {
                mostrarAlerta("Erro de Login", "Email ou senha inválidos. Tente novamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro de Sistema", "Ocorreu um erro ao tentar fazer login: " + e.getMessage());
        }
    }

    /**
     * Método que será usado para emitir alertas na tela caso o usuário tenha feito algo fora do planejado e não possa prosseguir.
     *
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
     *
     * @version 1.0
     * @since 22/05/25
     * @param event O evento de ação que disparou este método (adicionado para compatibilidade com setOnAction).
     */
    @FXML private void abrirRepositorio(ActionEvent event) { // Adicionado ActionEvent event
        try {
            // Verifica se o Desktop é suportado antes de tentar usar
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();

                // Verifica se a ação BROWSE é suportada
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI("https://github.com/georiSamuel/secure-voting-system"));
                } else {
                    // Fallback: mostrar URL para o usuário copiar
                    mostrarAlerta("Link do Repositório",
                            "Não foi possível abrir o navegador automaticamente.\n" +
                                    "Acesse manualmente: https://github.com/georiSamuel/secure-voting-system");
                }
            } else {
                // Desktop não suportado - usar comando do sistema
                String os = System.getProperty("os.name").toLowerCase();
                String url = "https://github.com/georiSamuel/secure-voting-system";

                if (os.contains("win")) {
                    // Windows
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                } else if (os.contains("mac")) {
                    // macOS
                    Runtime.getRuntime().exec("open " + url);
                } else if (os.contains("nix") || os.contains("nux")) {
                    // Linux
                    Runtime.getRuntime().exec("xdg-open " + url);
                } else {
                    // Sistema não reconhecido - mostrar URL
                    mostrarAlerta("Link do Repositório",
                            "Sistema não suportado para abrir URLs automaticamente.\n" +
                                    "Acesse manualmente: " + url);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Em caso de qualquer erro, mostrar o link para o usuário
            mostrarAlerta("Erro ao Abrir Link",
                    "Não foi possível abrir o navegador.\n" +
                            "Acesse manualmente: https://github.com/georiSamuel/secure-voting-system\n\n" +
                            "Erro: " + e.getMessage());
        }
    }
    /**
     * Método que aciona o botão "cadastre-se aqui" e abre a tela para o usuário se cadastrar.
     *
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
            palco.sizeToScene();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível carregar a tela de cadastro.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Ocorreu um erro inesperado ao abrir a tela de cadastro.");
        }
    }

    /**
     * Método para abrir a tela do usuário comum.
     *
     * @version 1.0
     * @since 22/05/25
     */
    private void abrirTelaUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/telausuario.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent telaUsuario = loader.load();
            Scene cenaAtual = usuarioCampo.getScene();
            Stage palco = (Stage) cenaAtual.getWindow();
            palco.setScene(new Scene(telaUsuario));
            palco.sizeToScene();
        }
        catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível carregar a tela do usuário.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Ocorreu um erro inesperado ao abrir a tela do usuário.");
        }
    }

    /**
     * Método para abrir a tela do administrador.
     *
     * @version 1.0
     * @since 22/05/25
     */
    private void abrirTelaAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/telaadmin.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent telaAdmin = loader.load();
            Scene cenaAtual = usuarioCampo.getScene();
            Stage palco = (Stage) cenaAtual.getWindow();
            palco.setScene(new Scene(telaAdmin));
            palco.setTitle("Tela de Admin");
            palco.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível carregar a tela do administrador.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Ocorreu um erro inesperado ao abrir a tela do administrador.");
        }
    }
}
