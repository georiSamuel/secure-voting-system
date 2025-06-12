package sistema.votacao.Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Data;
import sistema.votacao.Votacao.Service.VotacaoService;

/**
 * Classe responsável pela tela da criação de Votação personalizável.
 *
 * @author Suelle
 * @since 12/06/25
 * @version 1.0
 */
@Data
public class CriarVotacaoController {
    @FXML private TextField campoTitulo;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TextField campoItem;
    @FXML private ListView<String> listaItens;

    @FXML
    public void initialize() {
    }

    /**
     * Método responsável pela mudança de tela para tela da criação de votação acadêmica.
     * @version 1.0
     * @since 12/06/25
     * @param primaryStage
     * @param service
     */
    public void abrirTelaCriacaoAcademica(Stage primaryStage, VotacaoService service) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistema/votacao/Interface/CriacaoAcademica.fxml"));
            Parent root = loader.load();
            CriarAcademicaController controller = loader.getController();
            controller.setVotacaoService(service);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Criar Votação Acadêmica");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método responsável por adicionar cada item na lista de itens "votáveis".
     *
     * @version 1.0
     * @since 12/05/25
     */
    @FXML private void adicionarItem() {
        String item = campoItem.getText().trim();
        if (!item.isEmpty()) {
            listaItens.getItems().add(item);
            campoItem.clear();
        }
    }

    /**
     * Método responsável por criar a votação apenas após ao menos um item ser adicionado.
     *
     * @version 1.0
     * @since 12/05/25
     */
    @FXML private void criarVotacao() {
        if (listaItens.getItems().isEmpty()) {
            mostrarAlerta("Erro", "Adicione pelo menos um item votável");
            return;
        }

        System.out.println("Criando votação: " + campoTitulo.getText());
    }

    /**
     * Método responsável pela ação do botão "Cancelar". Limpa todos os campos
     *
     * @version 1.0
     * @since 12/06/25
     */
    @FXML private void cancelar() {
        listaItens.getItems().clear();
        campoTitulo.clear();
        comboTipo.getSelectionModel().clearSelection();
    }

    /**
     * Método responsável pela ação do botão "Voltar". Volta para a tela anterior
     *
     * @version 1.0
     * @since 12/06/25
     */
    @FXML private void voltar() {
        try {
            Parent telaadmin = FXMLLoader.load(getClass().getResource("/views/telaadmin.fxml"));
            Scene cenaAtual = telaadmin.getScene();
            Stage palco = (Stage) cenaAtual.getWindow();
            palco.setScene(new Scene(telaadmin));
            palco.sizeToScene();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível carregar a tela do usuário.");
        }
    }

    /**
     * Exibe um alerta na tela.
     * @param titulo Título do alerta
     * @param mensagem Mensagem do alerta
     *
     * @version 1.0
     * @since 12/06/25
     */
    @FXML private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}