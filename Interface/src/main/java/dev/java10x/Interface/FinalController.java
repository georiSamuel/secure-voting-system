package dev.java10x.Interface;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class FinalController {

    @FXML private Text mensagemSucesso;
    @FXML private Text detalhesVoto;
    @FXML private ImageView iconeConfirmacao;
    @FXML private Button botaoResultados;
    @FXML private Button botaoVoltar;

    @FXML public void initialize() {
    }

    public void setDetalhesVoto(String itemVotado) {
        detalhesVoto.setText("Você votou em: " + itemVotado);
    }

    @FXML private void visualizarResultados() {
        System.out.println("Abrindo tela de resultados...");
    }

    @FXML private void voltar() {
        System.out.println("Voltando para tela inicial...");
    }
}