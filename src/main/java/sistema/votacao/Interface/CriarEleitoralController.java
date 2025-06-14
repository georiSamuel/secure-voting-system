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
                // Nao limpa mensagemErroLabel aqui, pois o alerta já informa.
                return;
            }
            opcoesDeVoto.add(novaOpcao);
            novaOpcaoCampo.clear();
        } else {
            // Usa showAlert para feedback direto ao usuário, em vez de apenas mensagemErroLabel.setText
            showAlert(Alert.AlertType.ERROR, "Erro", "Por favor, digite uma opção de voto.");
            // mensagemErroLabel.setText("Por favor, digite uma opção de voto."); // Comentado, pois o alerta já informa
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
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Todos os campos e pelo menos uma opção de voto são obrigatórios.");
            // mensagemErroLabel.setText("Todos os campos e pelo menos uma opção de voto são obrigatórios."); // Comentado
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
            showAlert(Alert.AlertType.ERROR, "Erro de Data/Hora", "Formato de hora inválido. Use HH:MM. Erro: " + e.getMessage());
            // mensagemErroLabel.setText("Formato de hora inválido. Use HH:MM. Erro: " + e.getMessage()); // Comentado
            e.printStackTrace(); // Mantenha para depuração no console
            return;
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro ao Processar Data", "Ocorreu um erro ao processar data/hora: " + e.getMessage());
            // mensagemErroLabel.setText("Erro ao processar data/hora: " + e.getMessage()); // Comentado
            e.printStackTrace(); // Mantenha para depuração no console
            return;
        }

        if (inicioTimestamp.after(fimTimestamp)) {
            showAlert(Alert.AlertType.WARNING, "Erro de Período", "A data/hora de início não pode ser posterior à data/hora de fim.");
            // mensagemErroLabel.setText("A data/hora de início não pode ser posterior à data/hora de fim."); // Comentado
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

            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Votação eleitoral criada com sucesso! ID: " + votacaoSalva.getId());
            // mensagemStatusLabel.setText("Votação eleitoral criada com sucesso! ID: " + votacaoSalva.getId()); // Comentado
            limparCampos(); // Limpa os campos após o sucesso
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Erro ao Criar Votação", e.getMessage());
            // mensagemErroLabel.setText("Erro ao criar votação: " + e.getMessage()); // Comentado
            e.printStackTrace(); // Mantenha para depuração no console
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro Inesperado", "Ocorreu um erro inesperado: " + e.getMessage());
            // mensagemErroLabel.setText("Ocorreu um erro inesperado: " + e.getMessage()); // Comentado
            e.printStackTrace(); // Mantenha para depuração no console
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
            // Este alerta já estava usando showAlert, então não muda
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