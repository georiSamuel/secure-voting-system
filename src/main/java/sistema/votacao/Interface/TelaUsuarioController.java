package sistema.votacao.Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import sistema.votacao.SistemaVotacaoApplication; // Importe a classe principal da aplicação

import java.io.IOException;
import java.util.Objects;

/**
 * Controller responsável pela tela do usuário do JavaFX
 *
 * @author Suelle
 * @since 26/05/25
 * @version 1.6
 */
@Component
public class TelaUsuarioController {

    @FXML private Long usuarioLogadoId;
    @FXML private Hyperlink desconectar;
    @FXML private Button botaoVotacao;

    /**
     * Construtor padrão da classe TelaUsuarioController.
     * <p>
     * Este construtor é utilizado pelos frameworks Spring e JavaFX para instanciar o controller.
     * A inicialização dos componentes da interface e a configuração de dependências
     * ocorrem após a construção do objeto, através das anotações {@literal @FXML} e {@literal @Autowired}.
     * A lógica de inicialização da tela, como o carregamento das votações disponíveis,
     * deve ser colocada no método {@code initialize()}.
     * </p>
     *
     * @since 10/06/25
     * @version 1.0
     */
    public TelaUsuarioController() {
        // Construtor vazio, a inicialização é feita pelo framework via injeção de dependência.
    }

    /**
     * Método de inicialização que pode ser usado para configurar a tela
     * assim que ela for carregada.
     *
     * @since 10/06/25
     * @version 1.0
     */
    @FXML public void initialize() {
    }

    /** Método responsável pela ação de abrir a tela de votação.
     * @author Suelle
     * @version 1
     * @since 28/05
     */
    @FXML private void abrirTelaVotacao() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/telaDeVotacao.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            // A para configurar a tela de votação e pegar o id correto
            TeladeVotacaoController votacaoController = loader.getController();
            votacaoController.setUsuarioLogadoId(this.usuarioLogadoId); // Passa o ID que este controller já possui

            Stage stage = (Stage) botaoVotacao.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Votações Disponíveis");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Erro de Navegação", "Não foi possível carregar a tela de votação.");
            e.printStackTrace();
        }
    }

    /** Método para o usuário retornar à tela de login.
     * @author Suelle
     * @version 1
     * @since 28/05
     */
    @FXML private void desconectar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent telaLogin = loader.load();

            Scene cenaAtual = desconectar.getScene();
            Stage palco = (Stage) cenaAtual.getWindow();
            palco.setScene(new Scene(telaLogin));
            palco.setTitle("Sistema de Votação");
            palco.sizeToScene();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível desconectar.");
        }
    }

    /**
     * Método que será usado para emitir alertas na tela caso o usuário tenha feito algo fora do planejado e não possa prosseguir.
     * @author Suelle
     * @version 1.0
     * @since 22/05/25
     * @param erro, @param mensagem
     */
    private void mostrarAlerta(String erro, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(erro);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public void setUsuarioLogadoId(long id) {
        this.usuarioLogadoId = id;
    }

}