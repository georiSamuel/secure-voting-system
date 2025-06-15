package sistema.votacao.Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import sistema.votacao.SistemaVotacaoApplication;

import java.util.Objects;

/**
 * Controller que gerencia a tela final confirmando que o usuário completou a votação.
 *
 * @author
 * @version 1.0
 * @since 02/06/25
 */
@Component
public class FinalController {

    @FXML private Text mensagemSucesso;
    @FXML private Button botaoVoltar;

    @FXML public void initialize() {
    }

    /**
     * Método que desconecta o usuário e volta para a tela inicial (de login).
     *
     * @since 14/06/25
     * @version 1.0
     */
    @FXML private void voltar() {
            try {
                // É preicso criar um novo FXMLLoader se não dá erro
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));

                // Configurando o ControllerFactory para que o Spring injete as dependências
                loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
                Parent telaLogin = loader.load(); // Carregue a tela de login

                Scene cenaAtual = botaoVoltar.getScene();
                Stage palco = (Stage) cenaAtual.getWindow();
                palco.setScene(new Scene(telaLogin));
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

}