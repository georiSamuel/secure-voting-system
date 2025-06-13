package sistema.votacao.Interface;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * Controller que gerencia a tela final confirmando que o usuário completou a votação.
 *
 * @author
 * @version 1.0
 * @since 02/06/25
 */
public class FinalController {

    @FXML private Text mensagemSucesso;
    @FXML private Text detalhesVoto;
    @FXML private ImageView iconeConfirmacao;
    @FXML private Button botaoResultados;
    @FXML private Button botaoVoltar;

    @FXML public void initialize() {
    }

    @FXML private void visualizarResultados() {
        System.out.println("Abrindo tela de resultados...");
    }

    @FXML private void voltar() {
        System.out.println("Voltando para tela inicial...");
    }
}