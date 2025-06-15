package sistema.votacao.Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import sistema.votacao.SistemaVotacaoApplication; // Importe a classe principal da aplicação

/**
 * Classe responsável pela tela do usuário do JavaFX
 * @author Suelle
 * @since 26/05/25
 * @version 1.6
 */
@Component
public class TelaUsuarioController {

    @FXML private AnchorPane backgroundTela;
    @FXML private AnchorPane telaBranca;
    @FXML private Text conectadoText;
    @FXML private Text selecioneText;
    @FXML private MenuButton menuVotacoesAbertas;
    @FXML private Hyperlink desconectar;

    private ObservableList<MenuItem> votacoesAbertas = FXCollections.observableArrayList();

    @FXML public void initialize() {
        configurarMenuVotacoes();
    }

    /** Método para configurar o botão que mostra todas as votações abertas no momento
     * @author Suelle
     * @version 1
     * @since 28/05
     */
    private void configurarMenuVotacoes() {
        //exemplo basico pra mostrar como funciona, apenas
        MenuItem votacao1 = new MenuItem("Votação Institucional");
        votacao1.setOnAction(e -> abrirVotacao("Institucional"));

        MenuItem votacao2 = new MenuItem("Votação de Projetos");
        votacao2.setOnAction(e -> abrirVotacao("Projetos"));

        menuVotacoesAbertas.getItems().setAll(votacao1, votacao2);
    }

    /** Método para configurar o botão que mostra todas as votações existentes
     * @author Suelle
     * @version 1
     * @since 28/05
     */
    private void abrirVotacao(String tipoVotacao) {
        System.out.println("Abrindo votação: " + tipoVotacao);
        // ir para a tela de votação
    }

    /** Método para o usuário retornar à tela de login.
     * @author Suelle
     * @version 1
     * @since 28/05
     */
    @FXML private void desconectar() {
        try {
            // É preicso criar um novo FXMLLoader se não dá erro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            // Configurando o ControllerFactory para que o Spring injete as dependências
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);

            Parent telaLogin = loader.load(); // Carregue a tela de login

            Scene cenaAtual = desconectar.getScene();
            Stage palco = (Stage) cenaAtual.getWindow();
            palco.setScene(new Scene(telaLogin));
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

    /** Método para atualizar dinamicamente todos os itens do menu de votações abertas
     * @author Suelle
     * @version 1
     * @since 28/05
     */
    public void atualizarVotacoesAbertas(ObservableList<MenuItem> novasVotacoes) {
        menuVotacoesAbertas.getItems().setAll(novasVotacoes);
    }
}