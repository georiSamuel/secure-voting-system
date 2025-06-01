package dev.java10x.Interface;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CriarVotacaoController {
    @FXML private TextField campoTitulo;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TextField campoItem;
    @FXML private ListView<String> listaItens;

    @FXML
    public void initialize() {
        comboTipo.getItems().addAll(
                "Institucional",
                "Política",
                "Editável"
        );
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
        //scenemanager aqui
        System.out.println("Voltando para tela anterior");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}