package dev.java10x.Interface;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class TelaAdminController {
    @FXML private AnchorPane backgroundTela;
    @FXML private AnchorPane telaBranca;
    @FXML private Text conectadoText;
    @FXML private Text selecioneText;
    @FXML private MenuButton menuCriarVotacao;
    @FXML private MenuItem votInstitucional;
    @FXML private MenuItem votPolitica;
    @FXML private MenuItem votEditavel;
    @FXML private MenuButton menuVotacoesAbertas;
    @FXML private Button botaoAndamento;
    @FXML private Hyperlink desconectar;

    @FXML
    public void initialize() {
        configurarMenuVotacoesAbertas();
    }

    // Método para configurar as votações abertas
    private void configurarMenuVotacoesAbertas() {
        menuVotacoesAbertas.getItems().clear();

        //exemplo de como ficaria
        MenuItem item1 = new MenuItem("Votação Institucional 2025");
        MenuItem item2 = new MenuItem("Eleição de Representantes");

        menuVotacoesAbertas.getItems().addAll(item1, item2);
    }

    @FXML
    private void criarVotInstitucional() {
        System.out.println("Criando votação institucional...");
    }

    @FXML
    private void criarVotPolitica() {
        System.out.println("Criando votação política...");
    }

    @FXML
    private void criarVotEditavel() {
        System.out.println("Criando votação editável...");
    }

    @FXML
    private void abrirTelaAndamento() {
        System.out.println("Abrindo tela de andamento...");
    }

    @FXML
    private void desconectar() {
        System.out.println("Desconectado");
    }
}