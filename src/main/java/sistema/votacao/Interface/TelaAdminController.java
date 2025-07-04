package sistema.votacao.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Data;
import org.springframework.stereotype.Component;
import sistema.votacao.SistemaVotacaoApplication;

import java.io.IOException;
import java.util.Objects;

/**
 * Controller responsável pela tela do administrador do JavaFX
 *
 * @author Suelle
 * @version 1.1
 * @since 26/05/25
 */
@Data
@Component
public class TelaAdminController {
    @FXML private MenuButton menuCriarNovaVotacao;
    @FXML private Button botaoResultados;
    @FXML private Hyperlink desconectar;
    @FXML private Button botaoVotar;

    /**
     * Construtor padrão da classe TelaAdminController.
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
    public TelaAdminController() {
        // Construtor vazio, a inicialização é feita pelo framework via injeção de dependência.
    }

    /**
     * Lida com a ação de clicar no botão para abrir a tela de votação.
     *
     * @since 13/06/25
     * @version 1.0
     */
    @FXML private void abrirTelaVotacao() {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/telaDeVotacao.fxml")));
                loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
                Parent root = loader.load();

                TeladeVotacaoController votacaoController = loader.getController();
                votacaoController.setPreviousScreenIsAdmin(true);

                // A alteração mais importante é aqui:
                // Precisamos do ID do admin logado. O LoginController já o obtém.
                // Vamos assumir que TelaAdminController também terá acesso a ele.
                // O seu LoginController já faz a chamada: telaAdminController.setUsuarioLogadoId(usuarioLogado.getId());
                // Então, o ID já está disponível.
                votacaoController.setUsuarioLogadoId(LoginController.getUsuarioLogado().getId()); // Passa o ID do usuário logado

                Stage stage = (Stage) botaoVotar.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Votações Disponíveis");
                stage.show();
            } catch (IOException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de votação.");
                e.printStackTrace();
            }
        }

    /**
     * Lida com a ação de clicar no botão "Criar Nova Votação".
     * Redireciona para a tela de criação do tipo de votação eleitoral.
     *
     * @since 13/06/25
     * @version 1.1
     * @param event O evento de ação que disparou este método.
     */
    @FXML private void handleCriarEleitoral(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/criacaoEleitoral.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) menuCriarNovaVotacao.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Criar Votação Eleitoral");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de criação de votação eleitoral.");
            e.printStackTrace();
        }
    }

    /**
     * Lida com a ação de clicar no botão para criar uma nova votação acadêmica.
     * Redireciona para a tela de criação do tipo de votação acadêmica.
     *
     * @since 13/06/25
     * @version 1.1
     * @param event O evento de ação que disparou este método.
     */
    @FXML private void handleCriarAcademica(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/criacaoAcademica.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) menuCriarNovaVotacao.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Criar Votação Acadêmica");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de criação de votação acadêmica.");
            e.printStackTrace();
        }
    }

    /**
     * Lida com a ação de clicar no botão para criar uma nova votação personalizada.
     * Redireciona para a tela de criação do tipo de votação personalizada.
     *
     * @since 13/06/25
     * @version 1.1
     * @param event O evento de ação que disparou este método.
     */
    @FXML private void handleCriarPersonalizada(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/criacaoPersonalizavel.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) menuCriarNovaVotacao.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Criar Votação Personalizada");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de criação de votação personalizável.");
            e.printStackTrace();
        }
    }

    /**
     * Abre a tela de resultados e injeta todos os serviços necessários no controller,
     * incluindo o VotoService para a verificação de integridade.
     *
     * @since 14/06/2025
     * @version 1.1
     */
    @FXML private void abrirTelaResultados() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/Resultados.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            ResultadosController controller = loader.getController();

            controller.setServices(
                    SistemaVotacaoApplication.getSpringContext().getBean(sistema.votacao.votacao.service.VotacaoService.class),
                    SistemaVotacaoApplication.getSpringContext().getBean(sistema.votacao.opcao_voto.service.OpcaoVotoService.class),
                    SistemaVotacaoApplication.getSpringContext().getBean(sistema.votacao.voto.service.VotoService.class)
            );

            Stage stage = (Stage) botaoResultados.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Resultados e Andamento");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de resultados.");
            e.printStackTrace();
        }
    }

    /**
     * Método que desconecta o usuário e volta para a tela inicial (de login).
     * @since 13/06/25
     * @version 1.0
     * @param event O evento de ação que disparou este método.
     */
    @FXML private void desconectar(ActionEvent event) {
        try {
            // Criando um novo FXMLLoader pra não dar bug ao desconectar na tela de admin
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/login.fxml")));
            // Configurando o ControllerFactory para que o Spring injete as dependências
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);

            Parent telaLogin = loader.load(); // Carregue a tela de login

            Scene cenaAtual = desconectar.getScene();
            Stage palco = (Stage) cenaAtual.getWindow();
            palco.setScene(new Scene(telaLogin));
            palco.setTitle("Sistema de Votação");
            palco.sizeToScene();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível desconectar.");
        }
    }

    /**
     * Método que será usado para emitir alertas na tela caso o usuário tenha feito algo fora do planejado e não possa prosseguir.
     *
     * @since 13/06/25
     * @version 1.0
     * @param alertType O tipo de alerta (INFORMATION, ERROR, WARNING, etc.).
     * @param title O título do alerta.
     * @param message A mensagem a ser exibida no alerta.
     */
    @FXML private void mostrarAlerta(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setUsuarioLogadoId(long id) {
    }
}