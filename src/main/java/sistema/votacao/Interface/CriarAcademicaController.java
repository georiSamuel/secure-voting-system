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

    // Dependência do VotacaoService
    private VotacaoService votacaoService;

    // Lista observável para as opções de voto exibidas na ListView
    private ObservableList<String> opcoesDeVoto = FXCollections.observableArrayList();

    /**
     * Setter para injetar o VotacaoService.
     * Este método será chamado pelo seu código que carrega o FXML.
     * @param votacaoService A instância do VotacaoService.
     */
    public void setVotacaoService(VotacaoService votacaoService) {
        this.votacaoService = votacaoService;
    }

    /**
     * Método de inicialização do controlador.
     * Chamado automaticamente após o carregamento do FXML.
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
     */
    @FXML
    private void criarVotacao(ActionEvent event) {
        // Limpa mensagens anteriores
        mensagemStatusLabel.setText("");
        mensagemErroLabel.setText("");

        // 1. Coletar dados
        String titulo = tituloCampo.getText().trim();
        TipoCargoAcademico cargo = cargoComboBox.getValue();
        LocalDate inicioDate = inicioDatePicker.getValue();
        String inicioTimeStr = inicioTimeField.getText().trim();
        LocalDate fimDate = fimDatePicker.getValue();
        String fimTimeStr = fimTimeField.getText().trim();

        // 2. Validação básica de campos
        if (titulo.isEmpty() || cargo == null || inicioDate == null || inicioTimeStr.isEmpty() ||
                fimDate == null || fimTimeStr.isEmpty() || opcoesDeVoto.isEmpty()) {
            mensagemErroLabel.setText("Todos os campos e pelo menos uma opção de voto são obrigatórios.");
            return;
        }

        Timestamp inicioTimestamp;
        Timestamp fimTimestamp;

        try {
            // Formatar e combinar data e hora para o Timestamp de início
            LocalTime inicioTime = LocalTime.parse(inicioTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime inicioDateTime = LocalDateTime.of(inicioDate, inicioTime);
            inicioTimestamp = Timestamp.valueOf(inicioDateTime);

            // Formatar e combinar data e hora para o Timestamp de fim
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

        // Validação da lógica de datas (também é feita no Service, mas bom para feedback imediato)
        if (inicioTimestamp.after(fimTimestamp)) {
            mensagemErroLabel.setText("A data/hora de início não pode ser posterior à data/hora de fim.");
            return;
        }

        // 3. Criar a lista de OpcaoVoto a partir das Strings na ListView
        List<OpcaoVoto> opcoesParaVotacao = new ArrayList<>();
        for (String descricaoOpcao : opcoesDeVoto) {
            OpcaoVoto opcao = new OpcaoVoto();
            opcao.setDescricao(descricaoOpcao);
            opcao.setQuantidadeVotos(0L); // Inicializa com 0 votos
            // Note: A votação (Votacao) da OpcaoVoto será setada ao persistir no Service
            opcoesParaVotacao.add(opcao);
        }

        // 4. Criar a instância de VotacaoAcademica
        VotacaoAcademica novaVotacao = new VotacaoAcademica();
        novaVotacao.setTitulo(titulo);
        novaVotacao.setCargo(cargo);
        novaVotacao.setInicio(inicioTimestamp);
        novaVotacao.setFim(fimTimestamp);
        // Não defina as opções diretamente aqui, o Service ou a entidade JPA farão isso
        // ao salvar se houver um relacionamento @OneToMany configurado corretamente com cascade.ALL

        // 5. Chamar o serviço para salvar a votação
        try {
            // Garante que o serviço foi injetado
            if (votacaoService == null) {
                mensagemErroLabel.setText("Erro interno: Serviço de votação não disponível.");
                return;
            }

            // O service.criarVotacao() deve lidar com a persistência das opções também
            // se o relacionamento @OneToMany tiver CascadeType.ALL configurado
            // e as opções forem adicionadas à votação antes de salvar.
            novaVotacao.setOpcoes(opcoesParaVotacao); // Adiciona as opções antes de salvar

            VotacaoAcademica votacaoSalva = (VotacaoAcademica) votacaoService.criarVotacao(novaVotacao);

            mensagemStatusLabel.setText("Votação acadêmica criada com sucesso! ID: " + votacaoSalva.getId());
            // Opcional: Limpar campos após a criação bem-sucedida
            limparCampos();
        } catch (IllegalArgumentException e) {
            // Captura a exceção da validação do serviço (e.g., data de início > data de fim)
            mensagemErroLabel.setText("Erro ao criar votação: " + e.getMessage());
        } catch (Exception e) {
            // Captura outras exceções genéricas
            mensagemErroLabel.setText("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace(); // Para debug
        }
    }

    /**
     * Lida com a ação de voltar para a tela anterior.
     * (Implementação de navegação dependerá do seu sistema de rotas/gerenciamento de telas).
     * @param event O evento de clique no hyperlink.
     */
    @FXML
    private void voltar(ActionEvent event) {
        // Exemplo: Carregar outra tela FXML ou fechar a janela atual
        System.out.println("Navegar de volta...");
        // Você precisaria de um Stage para fechar a janela ou um Scene para mudar a tela
        // Ex: ((Node)event.getSource()).getScene().getWindow().hide();
    }

    /**
     * Limpa todos os campos do formulário após a criação bem-sucedida da votação.
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
