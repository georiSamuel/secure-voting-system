package sistema.votacao.Interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sistema.votacao.Votacao.Model.TipoCargoAcademico;
import sistema.votacao.Votacao.Model.VotacaoAcademica;
import sistema.votacao.OpcaoVoto.Model.OpcaoVoto;
import sistema.votacao.Votacao.Service.VotacaoService;
import sistema.votacao.SistemaVotacaoApplication; // Importa a aplicação principal
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
 * Classe responsável pela tela de Criar Votação Acadêmica do JavaFx.
 *
 * @author Suelle
 * @since 26/05/25
 * @version 1.0
 */
@Data
@Component
public class CriarAcademicaController {

    @FXML private TextField tituloCampo;
    @FXML private ComboBox<TipoCargoAcademico> cargoComboBox;
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
     *
     * @since 26/05/25
     * @veersion 1.0
     */
    @FXML public void initialize() {
        cargoComboBox.setItems(FXCollections.observableArrayList(TipoCargoAcademico.values()));

        opcoesListView.setItems(opcoesDeVoto);
    }

    /**
     * Lida com a ação de adicionar uma nova opção de voto.
     * Pega o texto do campo de nova opção e adiciona à lista de opções da ListView.
     * @param event O evento de clique no botão.
     *
     * @version 1.0
     * @since 26/05/25
     */
    @FXML private void adicionarOpcao(ActionEvent event) {
        String novaOpcao = novaOpcaoCampo.getText().trim();
        if (!novaOpcao.isEmpty()) {
            if (opcoesDeVoto.contains(novaOpcao)) {
                showAlert(Alert.AlertType.WARNING, "Opção Duplicada", "Esta opção já foi adicionada.");
                return;
            }
            opcoesDeVoto.add(novaOpcao); // Adiciona a opção à lista observável
            novaOpcaoCampo.clear(); // Limpa o campo de texto
            // --- LINHA DE LIMPEZA DE LABEL DE ERRO REMOVIDA ---
            // mensagemErroLabel.setText(""); // REMOVIDO
        } else {
            // Usa showAlert para feedback direto ao usuário
            showAlert(Alert.AlertType.ERROR, "Erro", "Por favor, digite uma opção de voto.");
        }
    }

    /**
     * Lida com a ação de criar a votação acadêmica.
     * Coleta todos os dados dos campos, valida e tenta criar a VotacaoAcademica.
     * @param event O evento de clique no botão.
     *
     * @version 1.0
     * @since 26/05/25
     */
    @FXML private void criarVotacao(ActionEvent event) {

        String titulo = tituloCampo.getText().trim();
        TipoCargoAcademico cargo = cargoComboBox.getValue();
        LocalDate inicioDate = inicioDatePicker.getValue();
        String inicioTimeStr = inicioTimeField.getText().trim();
        LocalDate fimDate = fimDatePicker.getValue();
        String fimTimeStr = fimTimeField.getText().trim();

        if (titulo.isEmpty() || cargo == null || inicioDate == null || inicioTimeStr.isEmpty() ||
                fimDate == null || fimTimeStr.isEmpty() || opcoesDeVoto.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Todos os campos e pelo menos uma opção de voto são obrigatórios.");
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
            e.printStackTrace(); // Mantenha para depuração no console
            return;
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro ao Processar Data", "Ocorreu um erro inesperado ao processar data/hora: " + e.getMessage());
            e.printStackTrace(); // Mantenha para depuração no console
            return;
        }

        if (inicioTimestamp.after(fimTimestamp)) {
            showAlert(Alert.AlertType.WARNING, "Erro de Período", "A data/hora de início não pode ser posterior à data/hora de fim.");
            return;
        }

        VotacaoAcademica novaVotacao = new VotacaoAcademica();
        novaVotacao.setTitulo(titulo);
        novaVotacao.setCargo(cargo);
        novaVotacao.setInicio(inicioTimestamp);
        novaVotacao.setFim(fimTimestamp);

        List<OpcaoVoto> opcoesParaVotacao = new ArrayList<>();
        for (String descricaoOpcao : opcoesDeVoto) {
            OpcaoVoto opcao = new OpcaoVoto();
            opcao.setDescricao(descricaoOpcao);
            opcao.setQuantidadeVotos(0L);
            opcao.setVotacao(novaVotacao);
            opcoesParaVotacao.add(opcao);
        }
        novaVotacao.setOpcoes(opcoesParaVotacao); // para definir a lista de opções na votação

        try {
            if (votacaoService == null) {
                showAlert(Alert.AlertType.ERROR, "Erro Interno", "Erro interno: Serviço de votação não disponível.");
                return;
            }

            VotacaoAcademica votacaoSalva = (VotacaoAcademica) votacaoService.criarVotacao(novaVotacao);

            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Votação acadêmica criada com sucesso! ID: " + votacaoSalva.getId());
            limparCampos();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Erro ao Criar Votação", e.getMessage());
            e.printStackTrace(); // Mantenha para depuração no console
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro Inesperado", "Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace(); // Mantenha para depuração no console
        }
    }

    /**
     * Lida com a ação de voltar para a tela anterior (TelaAdminController).
     * @param event O evento de clique no hyperlink.
     *
     * @since 26/05/25
     * @version 1.0
     */
    @FXML private void voltar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/telaadmin.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tela de Admin");
            stage.show();
        }
        // Este catch já estava usando showAlert, então não muda
        catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de administração.");
            e.printStackTrace();
        }
    }

    /**
     * Limpa todos os campos do formulário após a criação bem-sucedida da votação.
     * @version 1.0
     * @since 26/05/25
     */
    private void limparCampos() {
        tituloCampo.clear();
        cargoComboBox.getSelectionModel().clearSelection();
        inicioDatePicker.setValue(null);
        inicioTimeField.clear();
        fimDatePicker.setValue(null);
        fimTimeField.clear();
        novaOpcaoCampo.clear();
        opcoesDeVoto.clear();

    }

    /**
     * Exibe um alerta na tela.
     * @since 26/05/25
     * @version 1.0
     *
     * @param alertType O tipo de alerta (INFORMATION, ERROR, WARNING, etc.).
     * @param title O título do alerta.
     * @param message A mensagem a ser exibida no alerta.
     */
    @FXML private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}