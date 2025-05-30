package dev.java10x.Interface;

import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

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
        System.out.println("Usuário desconectado");
        // retorno para a tela de login
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