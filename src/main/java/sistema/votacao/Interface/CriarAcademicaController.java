package sistema.votacao.Interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import sistema.votacao.Votacao.Model.TipoCargoAcademico;
import sistema.votacao.Votacao.Model.VotacaoAcademica;
import sistema.votacao.Voto.Model.OpcaoVoto;
import sistema.votacao.Votacao.Service.VotacaoService; // Importa o VotacaoService

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CriarAcademicaController {

    @FXML
    private TextField tituloCampo;
    @FXML
    private ComboBox<TipoCargoAcademico> cargoComboBox;
    @FXML
    private DatePicker inicioDatePicker;
    @FXML
    private TextField inicioTimeField;
    @FXML
    private DatePicker fimDatePicker;
    @FXML
    private TextField fimTimeField;
    @FXML
    private TextField novaOpcaoCampo;
    @FXML
    private ListView<String> opcoesListView;
    @FXML
    private Label mensagemStatusLabel;
    @FXML
    private Label mensagemErroLabel;
    private VotacaoService votacaoService;
    private ObservableList<String> opcoesDeVoto = FXCollections.observableArrayList();

    /**
     * Setter para injetar o VotacaoService.
     * Este método será chamado pelo seu código que carrega o FXML.
     *
     * @param votacaoService A instância do VotacaoService.
     * @since 26/05/25
     * @veersion 1.0
     */
    public void setVotacaoService(VotacaoService votacaoService) {
        this.votacaoService = votacaoService;
    }

    /**
     * Método de inicialização do controlador.
     * Chamado automaticamente após o carregamento do FXML.
     *
     * @since 26/05/25
     * @veersion 1.0
     */
    @FXML
    public void initialize() {
        // Popula o ComboBox com os valores do enum TipoCargoAcademico
        cargoComboBox.setItems(FXCollections.observableArrayList(TipoCargoAcademico.values()));

        // Associa a lista observável à ListView
        opcoesListView.setItems(opcoesDeVoto);

        // Limpa as mensagens de status/erro ao iniciar
        mensagemStatusLabel.setText("");
        mensagemErroLabel.setText("");
    }

    /**
     * Lida com a ação de adicionar uma nova opção de voto.
     * Pega o texto do campo de nova opção e adiciona à lista de opções da ListView.
     * @param event O evento de clique no botão.
     */
    @FXML
    private void adicionarOpcao(ActionEvent event) {
        String novaOpcao = novaOpcaoCampo.getText().trim();
        if (!novaOpcao.isEmpty()) {
            opcoesDeVoto.add(novaOpcao); // Adiciona a opção à lista observável
            novaOpcaoCampo.clear(); // Limpa o campo de texto
            mensagemErroLabel.setText(""); // Limpa qualquer mensagem de erro anterior
        } else {
            mensagemErroLabel.setText("Por favor, digite uma opção de voto.");
        }
    }

    /**
     * Lida com a ação de criar a votação acadêmica.
     * Coleta todos os dados dos campos, valida e tenta criar a VotacaoAcademica.
     * @param event O evento de clique no botão.
     * @version 1.0
     * @since 26/05/25
     */
    @FXML
    private void criarVotacao(ActionEvent event) {
        mensagemStatusLabel.setText("");
        mensagemErroLabel.setText("");

        String titulo = tituloCampo.getText().trim();
        TipoCargoAcademico cargo = cargoComboBox.getValue();
        LocalDate inicioDate = inicioDatePicker.getValue();
        String inicioTimeStr = inicioTimeField.getText().trim();
        LocalDate fimDate = fimDatePicker.getValue();
        String fimTimeStr = fimTimeField.getText().trim();

        if (titulo.isEmpty() || cargo == null || inicioDate == null || inicioTimeStr.isEmpty() ||
                fimDate == null || fimTimeStr.isEmpty() || opcoesDeVoto.isEmpty()) {
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

        List<OpcaoVoto> opcoesParaVotacao = new ArrayList<>();
        for (String descricaoOpcao : opcoesDeVoto) {
            OpcaoVoto opcao = new OpcaoVoto();
            opcao.setDescricao(descricaoOpcao);
            opcao.setQuantidadeVotos(0L);
            opcoesParaVotacao.add(opcao);
        }

        VotacaoAcademica novaVotacao = new VotacaoAcademica();
        novaVotacao.setTitulo(titulo);
        novaVotacao.setCargo(cargo);
        novaVotacao.setInicio(inicioTimestamp);
        novaVotacao.setFim(fimTimestamp);

        try {
            if (votacaoService == null) {
                mensagemErroLabel.setText("Erro interno: Serviço de votação não disponível.");
                return;
            }

            novaVotacao.setOpcoes(opcoesParaVotacao);

            VotacaoAcademica votacaoSalva = (VotacaoAcademica) votacaoService.criarVotacao(novaVotacao);

            mensagemStatusLabel.setText("Votação acadêmica criada com sucesso! ID: " + votacaoSalva.getId());
            limparCampos();
        } catch (IllegalArgumentException e) {
            mensagemErroLabel.setText("Erro ao criar votação: " + e.getMessage());
        } catch (Exception e) {
            mensagemErroLabel.setText("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lida com a ação de voltar para a tela anterior.
     * @param event O evento de clique no hyperlink.
     *
     * @since 26/05/25
     * @version 1.0
     */
    @FXML
    private void voltar(ActionEvent event) {
        System.out.println("...");
        //TODO
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
}
