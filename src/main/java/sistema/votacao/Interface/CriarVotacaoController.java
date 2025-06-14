package sistema.votacao.Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import sistema.votacao.SistemaVotacaoApplication;
import sistema.votacao.Votacao.Service.VotacaoService;
import sistema.votacao.Votacao.Model.VotacaoPersonalizada; // Import para VotacaoPersonalizada
import sistema.votacao.Voto.Model.OpcaoVoto; // Import para OpcaoVoto
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.sql.Timestamp; // Import para Timestamp
import java.time.LocalDateTime; // Import para LocalDateTime
import java.time.format.DateTimeFormatter; // Import para DateTimeFormatter
import java.time.LocalTime;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component; // Adicionado para autowire em JavaFX

/**
 * Classe responsável pela tela da criação de Votação personalizável.
 *
 * @author Suelle
 * @since 12/06/25
 * @version 1.0
 */
@Data
@Component
public class CriarVotacaoController {
    @FXML private TextField campoTitulo;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TextField campoItem;
    @FXML private ListView<String> listaItens;
    @FXML private DatePicker inicioDatePicker;
    @FXML private TextField inicioTimeField;
    @FXML private DatePicker fimDatePicker;
    @FXML private TextField fimTimeField;

    @Autowired
    private VotacaoService votacaoService;

    @FXML
    public void initialize() {
    }

    /**
     * Método responsável pela mudança de tela para tela da criação de votação acadêmica.
     * @version 1.0
     * @since 12/06/25
     * @param primaryStage
     * @param service
     */
    public void abrirTelaCriacaoAcademica(Stage primaryStage, VotacaoService service) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistema/votacao/Interface/CriacaoAcademica.fxml"));
            Parent root = loader.load();
            CriarAcademicaController controller = loader.getController();
            controller.setVotacaoService(service);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Criar Votação Acadêmica");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método responsável por adicionar cada item na lista de itens "votáveis".
     *
     * @version 1.0
     * @since 12/05/25
     */
    @FXML private void adicionarItem() {
        String item = campoItem.getText().trim();
        if (!item.isEmpty()) {
            if (listaItens.getItems().contains(item)) {
                mostrarAlerta("Opção Duplicada", "Esta opção já foi adicionada.");
                return;
            }
            listaItens.getItems().add(item);
            campoItem.clear();
        } else {
            mostrarAlerta("Erro", "Por favor, digite uma opção de voto.");
        }
    }

    /**
     * Método responsável por criar a votação personalizada.
     * Coleta os dados dos campos, valida e tenta criar a VotacaoPersonalizada.
     * Após a criação bem-sucedida, navega para a tela de votação.
     *
     * @version 1.0
     * @since 12/05/25
     */
    @FXML private void criarVotacao() {
        String titulo = campoTitulo.getText().trim();
        LocalDate inicioDate = inicioDatePicker.getValue();
        String inicioTimeStr = inicioTimeField.getText().trim();
        LocalDate fimDate = fimDatePicker.getValue();
        String fimTimeStr = fimTimeField.getText().trim();

        if (titulo.isEmpty() || listaItens.getItems().isEmpty() || inicioDate == null || inicioTimeStr.isEmpty() ||
                fimDate == null || fimTimeStr.isEmpty()) {
            mostrarAlerta("Erro", "Todos os campos e pelo menos um item votável são obrigatórios.");
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

        } catch (Exception e) {
            mostrarAlerta("Erro de Data/Hora", "Formato de hora inválido. Use HH:MM. Erro: " + e.getMessage());
            return;
        }

        if (inicioTimestamp.after(fimTimestamp)) {
            mostrarAlerta("Erro de Data", "A data/hora de início não pode ser posterior à data/hora de fim.");
            return;
        }

        VotacaoPersonalizada novaVotacao = getVotacaoPersonalizada(titulo, inicioTimestamp, fimTimestamp);

        try {
            VotacaoPersonalizada votacaoSalva = votacaoService.criarVotacaoPersonalizada(novaVotacao);
            mostrarAlerta("Sucesso", "Votação personalizada criada com sucesso! ID: " + votacaoSalva.getId());
            cancelar();

        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro ao Criar Votação", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Erro Inesperado", "Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *
     *
     * @param titulo
     * @param inicioTimestamp
     * @param fimTimestamp
     * @return
     */
    @NotNull
    private VotacaoPersonalizada getVotacaoPersonalizada(String titulo, Timestamp inicioTimestamp, Timestamp fimTimestamp) {
        VotacaoPersonalizada novaVotacao = new VotacaoPersonalizada();
        novaVotacao.setTitulo(titulo);
        novaVotacao.setInicio(inicioTimestamp);
        novaVotacao.setFim(fimTimestamp);

        List<OpcaoVoto> opcoesParaVotacao = new ArrayList<>();
        for (String descricaoOpcao : listaItens.getItems()) {
            OpcaoVoto opcao = new OpcaoVoto();
            opcao.setDescricao(descricaoOpcao);
            opcao.setQuantidadeVotos(0L);
            opcao.setVotacao(novaVotacao);
            opcoesParaVotacao.add(opcao);
        }
        novaVotacao.setOpcoes(opcoesParaVotacao);
        return novaVotacao;
    }

    /**
     * Método responsável pela ação do botão "Cancelar". Limpa todos os campos
     *
     * @version 1.0
     * @since 12/06/25
     */
    @FXML private void cancelar() {
        listaItens.getItems().clear();
        campoTitulo.clear();
        comboTipo.getSelectionModel().clearSelection();
        campoItem.clear();
        inicioDatePicker.setValue(null);
        inicioTimeField.clear();
        fimDatePicker.setValue(null);
        fimTimeField.clear();
    }

    /**
     * Método responsável pela ação do botão "Voltar". Volta para a tela anterior
     *
     * @version 1.0
     * @since 12/06/25
     */
    @FXML private void voltar() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/telaadmin.fxml")));
            loader.setControllerFactory(SistemaVotacaoApplication.getSpringContext()::getBean);
            Parent telaadmin = loader.load();
            Stage palco = (Stage) campoTitulo.getScene().getWindow();
            palco.setScene(new Scene(telaadmin));
            palco.setTitle("Tela de Admin");
            palco.sizeToScene();
            palco.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível carregar a tela do usuário.");
        }
    }

    /**
     * Exibe um alerta na tela.
     * @param titulo Título do alerta
     * @param mensagem Mensagem do alerta
     *
     * @version 1.0
     * @since 12/06/25
     */
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
