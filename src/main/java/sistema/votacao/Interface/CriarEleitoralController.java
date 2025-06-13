package sistema.votacao.Interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sistema.votacao.Votacao.Model.TipoCargoEleitoral;
import sistema.votacao.Votacao.Model.VotacaoEleitoral;
import sistema.votacao.Voto.Model.OpcaoVoto;
import sistema.votacao.Votacao.Service.VotacaoService;
import sistema.votacao.SistemaVotacaoApplication; // Importa a aplicação principal
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller para a tela de criação de votações eleitorais.
 * Gerencia a interação entre a interface de usuário (CriacaoEleitoral.fxml) e a lógica para registro de novas votações eleitorais.
 *
 * @author Suelle
 * @version 1.0
 * @since 12/06/2025
 */
@Component
public class CriarEleitoralController {

    @FXML private TextField tituloCampo;
    @FXML private ComboBox<TipoCargoEleitoral> cargoComboBox;
    @FXML private TextField zonaEleitoralCampo;
    @FXML private TextField secaoEleitoralCampo;
    @FXML private CheckBox votoObrigatorioCheckBox;
    @FXML private CheckBox permiteVotoEmBrancoCheckBox;
    @FXML private DatePicker inicioDatePicker;
    @FXML private TextField inicioTimeField;
    @FXML private DatePicker fimDatePicker;
    @FXML private TextField fimTimeField;
    @FXML private TextField novaOpcaoCampo;
    @FXML private ListView<String> opcoesListView;
    @FXML private Label mensagemStatusLabel;
    @FXML private Label mensagemErroLabel;

    @Autowired
    private VotacaoService votacaoService;
    private ObservableList<String> opcoesDeVoto = FXCollections.observableArrayList();

    /**
     * Método de inicialização do controlador.
     * Chamado automaticamente após o carregamento do FXML.
     * @version 1.0
     * @since 26/05/25
     */
    @FXML
    public void initialize() {
        cargoComboBox.setItems(FXCollections.observableArrayList(TipoCargoEleitoral.values()));

        opcoesListView.setItems(opcoesDeVoto);

        mensagemStatusLabel.setText("");
        mensagemErroLabel.setText("");

        votoObrigatorioCheckBox.setSelected(true);
        permiteVotoEmBrancoCheckBox.setSelected(true);
    }

    /**
     * Lida com a ação de adicionar uma nova opção de voto. Pega o texto do campo de nova opção e adiciona na lista de opções da ListView.
     * @param event O evento de clique no botão.
     * @since 12/06/25
     * @version 1.0
     */
    @FXML private void adicionarOpcao(ActionEvent event) {
        String novaOpcao = novaOpcaoCampo.getText().trim();
        if (!novaOpcao.isEmpty()) {
            if (opcoesDeVoto.contains(novaOpcao)) {
                showAlert(Alert.AlertType.WARNING, "Opção Duplicada", "Esta opção já foi adicionada.");
                return;
            }
            opcoesDeVoto.add(novaOpcao);
            novaOpcaoCampo.clear();
            mensagemErroLabel.setText("");
        } else {
            mensagemErroLabel.setText("Por favor, digite uma opção de voto.");
        }
    }

    /**
     * Lida com a ação de criar a votação eleitoral.
     * Coleta todos os dados dos campos, valida e tenta criar a VotacaoEleitoral.
     * @param event O evento de clique no botão.
     *
     * @version 1.1
     * @since 28/05/25
     */
    @FXML
    private void criarVotacao(ActionEvent event) {
        mensagemStatusLabel.setText("");
        mensagemErroLabel.setText("");

        String titulo = tituloCampo.getText().trim();
        TipoCargoEleitoral cargo = cargoComboBox.getValue();
        String zonaEleitoral = zonaEleitoralCampo.getText().trim();
        String secaoEleitoral = secaoEleitoralCampo.getText().trim();
        boolean votoObrigatorio = votoObrigatorioCheckBox.isSelected();
        boolean permiteVotoEmBranco = permiteVotoEmBrancoCheckBox.isSelected();
        LocalDate inicioDate = inicioDatePicker.getValue();
        String inicioTimeStr = inicioTimeField.getText().trim();
        LocalDate fimDate = fimDatePicker.getValue();
        String fimTimeStr = fimTimeField.getText().trim();

        if (titulo.isEmpty() || cargo == null || zonaEleitoral.isEmpty() || secaoEleitoral.isEmpty() ||
                inicioDate == null || inicioTimeStr.isEmpty() || fimDate == null || fimTimeStr.isEmpty() ||
                opcoesDeVoto.isEmpty()) {
            mensagemErroLabel.setText("Todos os campos e pelo menos uma opção de voto são obrigatórios.");
            return;
        }

        Timestamp inicioTimestamp;
        Timestamp fimTimestamp;

        try {
            LocalTime inicioTime = LocalTime.parse(inicioTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime inicioDateTime = LocalDateTime.of(inicioDate, inicioTime);
            inicioTimestamp = Timestamp.valueOf(inicioDateTime);

            LocalTime fimTime = LocalTime.parse(fimTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime fimDateTime = LocalDateTime.of(fimDate, fimTime);
            fimTimestamp = Timestamp.valueOf(fimDateTime);

        } catch (DateTimeParseException e) {
            mensagemErroLabel.setText("Formato de hora inválido. Use HH:MM. Erro: " + e.getMessage());
            return;
        } catch (Exception e) {
            mensagemErroLabel.setText("Erro ao processar data/hora: " + e.getMessage());
            return;
        }

        if (inicioTimestamp.after(fimTimestamp)) {
            mensagemErroLabel.setText("A data/hora de início não pode ser posterior à data/hora de fim.");
            return;
        }

        VotacaoEleitoral novaVotacao = new VotacaoEleitoral();
        novaVotacao.setTitulo(titulo);
        novaVotacao.setCargo(cargo);
        novaVotacao.setZonaEleitoral(zonaEleitoral);
        novaVotacao.setSecaoEleitoral(secaoEleitoral);
        novaVotacao.setVotoObrigatorio(votoObrigatorio);
        novaVotacao.setPermiteVotoEmBranco(permiteVotoEmBranco);
        novaVotacao.setInicio(inicioTimestamp);
        novaVotacao.setFim(fimTimestamp);

        // agora associa cada OpcaoVoto com a Votacao
        List<OpcaoVoto> opcoesParaVotacao = new ArrayList<>();
        for (String descricaoOpcao : opcoesDeVoto) {
            OpcaoVoto opcao = new OpcaoVoto();
            opcao.setDescricao(descricaoOpcao);
            opcao.setQuantidadeVotos(0L);
            opcao.setVotacao(novaVotacao);
            opcoesParaVotacao.add(opcao);
        }
        novaVotacao.setOpcoes(opcoesParaVotacao);

        try {
            VotacaoEleitoral votacaoSalva = (VotacaoEleitoral) votacaoService.criarVotacao(novaVotacao);

            mensagemStatusLabel.setText("Votação eleitoral criada com sucesso! ID: " + votacaoSalva.getId());
            limparCampos();
        } catch (IllegalArgumentException e) {
            mensagemErroLabel.setText("Erro ao criar votação: " + e.getMessage());
        } catch (Exception e) {
            mensagemErroLabel.setText("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lida com a ação de voltar para a tela anterior (TelaAdminController).
     * @param event O evento de clique no botão.
     * @version 1.0
     * @since 28/05/25
     */
    @FXML
    private void voltar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/telaadmin.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tela de Admin");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de administração.");
            e.printStackTrace();
        }
    }

    /**
     * Limpa todos os campos do formulário após a criação bem-sucedida da votação.
     *
     * @version 1.0
     * @since 28/05/25
     */
    private void limparCampos() {
        tituloCampo.clear();
        cargoComboBox.getSelectionModel().clearSelection();
        zonaEleitoralCampo.clear();
        secaoEleitoralCampo.clear();
        votoObrigatorioCheckBox.setSelected(true);
        permiteVotoEmBrancoCheckBox.setSelected(true);
        inicioDatePicker.setValue(null);
        inicioTimeField.clear();
        fimDatePicker.setValue(null);
        fimTimeField.clear();
        novaOpcaoCampo.clear();
        opcoesDeVoto.clear();
    }

    /**
     * Exibe um alerta na tela.
     * @param alertType O tipo de alerta (INFORMATION, ERROR, WARNING, etc.).
     * @param title O título do alerta.
     * @param message A mensagem a ser exibida no alerta.
     *
     * @version 1.0
     * @since 28/05/25
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
