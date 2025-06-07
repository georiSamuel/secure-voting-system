package sistema.votacao.Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sistema.votacao.Votacao.Service.VotacaoService;

public class CriarVotacaoController {
    @FXML private TextField campoTitulo;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TextField campoItem;
    @FXML private ListView<String> listaItens;

    @FXML
    public void initialize() {
    }

    public void showCriacaoAcademicaScreen(Stage primaryStage, VotacaoService service) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistema/votacao/Interface/CriacaoAcademica.fxml"));
            Parent root = loader.load();

            // Obtenha o controlador
            CriarAcademicaController controller = loader.getController();
            // Injete o serviço no controlador
            controller.setVotacaoService(service);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Criar Votação Acadêmica");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Lidar com o erro ao carregar a tela
        }
    }

    @FXML
    private void adicionarItem() {
        String item = campoItem.getText().trim();
        if (!item.isEmpty()) {
            listaItens.getItems().add(item);
            campoItem.clear();
        }
    }

    @FXML
    private void criarVotacao() {
        if (listaItens.getItems().isEmpty()) {
            showAlert("Erro", "Adicione pelo menos um item votável");
            return;
        }

        System.out.println("Criando votação: " + campoTitulo.getText());
        System.out.println("Itens: " + listaItens.getItems());
    }

    @FXML
    private void cancelar() {
        //clear em tudo
        listaItens.getItems().clear();
        campoTitulo.clear();
        comboTipo.getSelectionModel().clearSelection();
    }

    @FXML
    private void voltar() {
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

    @FXML 
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}