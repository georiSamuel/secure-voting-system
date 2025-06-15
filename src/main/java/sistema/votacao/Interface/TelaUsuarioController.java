package sistema.votacao.Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sistema.votacao.SistemaVotacaoApplication;
import sistema.votacao.Votacao.Service.VotacaoService;
import sistema.votacao.Votacao.Model.Votacao;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável pela tela do usuário do JavaFX
 * @author Suelle
 * @since 26/05/25
 * @version 1.7 (Atualizado para incluir a funcionalidade do botão Votar e correção de FXML)
 */
@Data
@Component
public class TelaUsuarioController {

    @FXML private AnchorPane backgroundTela;
    @FXML private AnchorPane telaBranca;
    @FXML private Text conectadoText;
    @FXML private Text selecioneText;
    @FXML private MenuButton menuVotacoesAbertas;
    @FXML private Hyperlink desconectar;
    @FXML private Button botaoVotar;

    @Autowired
    private VotacaoService votacaoService;

    private ObservableList<MenuItem> votacoesAbertas = FXCollections.observableArrayList();

    private Long usuarioLogadoId;

    /**
     * Define o ID do usuário logado neste controller.
     * Este método é crucial para que as votações possam ser buscadas corretamente para o usuário específico.
     * @param usuarioId O ID do usuário que está logado.
     */
    public void setUsuarioLogadoId(Long usuarioId) {
        this.usuarioLogadoId = usuarioId;
    }

    /**
     * Método de inicialização do controlador. Chamado automaticamente pelo FXMLLoader após o carregamento do FXML.
     * Configura os event handlers para os botões e itens de menu.
     */
    @FXML public void initialize() {
        MenuItem abrirVotacoesItem = new MenuItem("Abrir Votações Disponíveis");
        abrirVotacoesItem.setOnAction(this::handleAbrirTelaVotacao);
        menuVotacoesAbertas.getItems().add(abrirVotacoesItem);

        if (botaoVotar != null) {
            botaoVotar.setOnAction(this::handleAbrirTelaVotacao);
        }
    }

    /**
     * Manipula o clique no botão "Votar" ou no item de menu para abrir a tela de votação.
     * Realiza uma checagem para verificar se há votações ativas nas quais o usuário ainda não votou.
     * Se houver, navega para a tela de votação; caso contrário, exibe um alerta.
     *
     * @param event O evento de ação que disparou este método (pode ser de um MenuItem ou Button).
     * @since 14/06/25
     * @version 1.0 (Lógica aprimorada e reutilizada para o botão Votar)
     */
    @FXML private void handleAbrirTelaVotacao(ActionEvent event) {
        try {
            if (usuarioLogadoId == null) {
                mostrarAlerta("Erro", "ID do usuário não definido. Por favor, faça login novamente.");
                return;
            }

            List<Votacao> votacoesDisponiveis = votacaoService.buscarVotacoesAtivasNaoVotadasPeloUsuario(usuarioLogadoId);

            if (votacoesDisponiveis == null || votacoesDisponiveis.isEmpty()) {
                mostrarAlerta("Votações", "Você já votou em todas as votações disponíveis ou não há votações ativas no momento.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/telaDeVotacao.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            TeladeVotacaoController teladeVotacaoController = loader.getController();
            teladeVotacaoController.setUsuarioLogadoId(usuarioLogadoId);

            Stage stage;
            if (event.getSource() instanceof MenuItem) {
                stage = (Stage) ((MenuItem)event.getSource()).getParentPopup().getOwnerWindow();
            } else {
                stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
            }

            stage.setScene(new Scene(root));
            stage.setTitle("Tela de Votação");
            stage.show();

        } catch (IOException e) {
            mostrarAlerta("Erro de Navegação", "Não foi possível carregar a tela de votação.");
            e.printStackTrace();
        } catch (Exception e) {
            mostrarAlerta("Erro", "Ocorreu um erro ao verificar ou carregar as votações: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Método para o usuário retornar à tela de login.
     * @author Suelle
     * @version 1
     * @since 28/05
     */
    @FXML private void desconectar() {
        try {
            Parent telaLogin = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
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
     * Método utilitário que será usado para emitir alertas na tela caso o usuário tenha feito algo
     * fora do planejado e não possa prosseguir, ou para informar sucesso.
     *
     * @author Suelle
     * @version 1.0
     * @since 22/05/25
     * @param erro O título do alerta.
     * @param mensagem A mensagem a ser exibida no alerta.
     */
    private void mostrarAlerta(String erro, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(erro);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
