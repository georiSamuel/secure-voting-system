package sistema.votacao.Interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox; // Importe VBox
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sistema.votacao.Votacao.Model.Votacao;
import sistema.votacao.Votacao.Service.VotacaoService;
import sistema.votacao.Voto.DTO.VotoRequestDTO;
import sistema.votacao.Voto.Model.OpcaoVoto;
import sistema.votacao.Voto.Service.OpcaoVotoService;
import sistema.votacao.Voto.Service.VotoService;
import sistema.votacao.Usuario.Service.UsuarioService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class TeladeVotacaoController {

    @FXML private ListView<Votacao> votacoesDisponiveisListView;
    @FXML private Label votacaoTituloLabel;
    @FXML private Label votacaoDescricaoLabel;
    @FXML private Label mensagemErroLabel;
    @FXML private Button votarButton;
    @FXML private Button voltarButton;
    @FXML private VBox opcoesVotoContainer;

    private ToggleGroup opcoesVotoGroup;

    @Autowired private VotacaoService votacaoService;
    @Autowired private OpcaoVotoService opcaoVotoService;
    @Autowired private VotoService votoService;
    @Autowired private UsuarioService usuarioService;

    private ObservableList<Votacao> votacoesAbertas = FXCollections.observableArrayList();
    private Votacao votacaoSelecionada;
    private List<OpcaoVoto> opcoesAtuais;

    @FXML
    public void initialize() {
        opcoesVotoGroup = new ToggleGroup();

        if (votacaoService != null) {
            initializeVotacoesList();
        }

        votacoesDisponiveisListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        handleVotacaoSelection(newValue);
                    }
                });
    }

    private void initializeVotacoesList() {
        try {
            List<Votacao> ativas = votacaoService.buscarVotacoesAtivas();
            votacoesAbertas.setAll(ativas);
            votacoesDisponiveisListView.setItems(votacoesAbertas);

            votacoesDisponiveisListView.setCellFactory(lv -> new javafx.scene.control.ListCell<Votacao>() {
                @Override protected void updateItem(Votacao votacao, boolean empty) {
                    super.updateItem(votacao, empty);
                    setText(empty ? null : votacao.getTitulo());
                }
            });

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar as votações disponíveis: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleVotacaoSelection(Votacao votacao) {
        votacaoSelecionada = votacao;
        votacaoTituloLabel.setText(votacao.getTitulo());
        votacaoDescricaoLabel.setText("Escolha uma opção de voto para: " + votacao.getTitulo());
        mensagemErroLabel.setText("");

        opcoesVotoContainer.getChildren().clear();
        opcoesVotoGroup.selectToggle(null);

        try {
            opcoesAtuais = opcaoVotoService.buscarOpcoesPorVotacao(votacao.getId());
            if (opcoesAtuais != null && !opcoesAtuais.isEmpty()) {
                for (OpcaoVoto opcao : opcoesAtuais) {
                    RadioButton radioButton = new RadioButton(opcao.getDescricao());
                    radioButton.setUserData(opcao.getId());
                    radioButton.setToggleGroup(opcoesVotoGroup);
                    opcoesVotoContainer.getChildren().add(radioButton);
                }
            } else {
                mensagemErroLabel.setText("Nenhuma opção de voto encontrada para esta votação.");
            }
        } catch (Exception e) {
            mensagemErroLabel.setText("Erro ao carregar opções de voto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVotarButton(ActionEvent event) {
        if (votacaoSelecionada == null) {
            showAlert(Alert.AlertType.WARNING, "Erro de Voto", "Por favor, selecione uma votação.");
            return;
        }

        RadioButton selectedRadioButton = (RadioButton) opcoesVotoGroup.getSelectedToggle();
        if (selectedRadioButton == null) {
            showAlert(Alert.AlertType.WARNING, "Erro de Voto", "Por favor, selecione uma opção de voto.");
            return;
        }

        Long opcaoVotoId = (Long) selectedRadioButton.getUserData();

        // TODO: pegar o id atual do bd
        Long usuarioId = 1L;

        VotoRequestDTO votoRequestDTO = new VotoRequestDTO();
        votoRequestDTO.setUsuarioId(usuarioId);
        votoRequestDTO.setVotacaoId(votacaoSelecionada.getId());
        votoRequestDTO.setOpcaoVotoId(opcaoVotoId);

        try {
            if (usuarioService.verificarSeUsuarioJaVotou(usuarioId, votacaoSelecionada.getId())) {
                showAlert(Alert.AlertType.WARNING, "Voto Já Registrado", "Você já votou nesta eleição.");
                return;
            }

            votoService.registrarVoto(votoRequestDTO);
            showAlert(Alert.AlertType.INFORMATION, "Voto Registrado", "Seu voto foi registrado com sucesso!");

            initializeVotacoesList();
            votacaoTituloLabel.setText("Selecione uma votação.");
            votacaoDescricaoLabel.setText("");
            opcoesVotoContainer.getChildren().clear();
            opcoesVotoGroup.selectToggle(null);

            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/final.fxml")));
                Parent root = loader.load();
                Stage stage = (Stage) votarButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Voto Confirmado");
                stage.show();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela final.");
                e.printStackTrace();
            }


        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Erro ao Votar", e.getMessage());
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erro ao Votar", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro Inesperado", "Ocorreu um erro inesperado ao registrar o voto: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleVoltarButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/telausuario.fxml")));
            Parent root = loader.load();
            Stage stage = (Stage) voltarButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tela do Usuário");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela do usuário.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}