package sistema.votacao.Interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import sistema.votacao.votacao.model.Votacao;
import sistema.votacao.votacao.service.VotacaoService;
import sistema.votacao.opcao_voto.model.OpcaoVoto;
import sistema.votacao.voto.model.VotoModel;
import sistema.votacao.opcao_voto.service.OpcaoVotoService;
import sistema.votacao.voto.service.VotoService;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Controller responsável pela tela de Resultados do JavaFX
 *
 * @author Suelle
 * @since 26/05/25
 * @version 1.0
 */
@Component
public class ResultadosController {

    @FXML private ListView<Votacao> votacoesListView;
    @FXML private Label votacaoTituloLabel;
    @FXML private TableView<OpcaoVoto> resultadosTableView;
    @FXML private TableColumn<OpcaoVoto, String> opcaoColumn;
    @FXML private TableColumn<OpcaoVoto, Long> votosColumn;
    @FXML private Button voltarButton;

    /**
     * Botão que dispara a verificação de integridade dos votos.
     * Mantenho a declaração, embora a lógica para exibir o status mude para um Alert.
     */
    @FXML private Button verificarIntegridadeButton;

    private VotacaoService votacaoService;
    private OpcaoVotoService opcaoVotoService;
    private VotoService votoService;

    /**
     * Construtor padrão da classe ResultadosController.
     * <p>
     * Este construtor é utilizado pelos frameworks Spring e JavaFX para instanciar o controller.
     * A inicialização dos componentes da interface e a configuração de dependências
     * ocorrem após a construção do objeto, através das anotações {@literal @FXML} e {@literal @Autowired}.
     * A lógica de inicialização da tela, como o carregamento dos dados, deve ser colocada
     * no método {@code initialize()}.
     * </p>
     *
     * @since 10/06/25
     * @version 1.0
     */
    public ResultadosController() {
        // Construtor vazio, a inicialização é feita pelo framework via injeção de dependência.
    }

    /**
     * Define os serviços necessários para o controlador.
     * Este método é usado para injetar as dependências (serviços) no controller.
     * @param votacaoService O serviço para operações de votação.
     * @param opcaoVotoService O serviço para operações de opções de voto.
     * @param votoService O serviço para operações de voto, incluindo a verificação de integridade.
     *
     * @author eu
     * @since 14/06/2025
     * @version 1.1
     */
    public void setServices(VotacaoService votacaoService, OpcaoVotoService opcaoVotoService, VotoService votoService) {
        this.votacaoService = votacaoService;
        this.opcaoVotoService = opcaoVotoService;
        this.votoService = votoService;
        // agora inicializa a lista de votações
        initializeVotacoesList();
    }

    /**
     * Método de inicialização do controlador. Chamado automaticamente pelo FXMLLoader.
     * Configura as colunas da tabela e os listeners.
     *
     * @since 09/06/25
     * @version 1.0
     */
    @FXML
    public void initialize() {
        // Configura as colunas da TableView para mapear as propriedades de OpcaoVoto
        opcaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        votosColumn.setCellValueFactory(new PropertyValueFactory<>("quantidadeVotos"));

        // Adiciona um listener para a seleção de itens na ListView de votações
        votacoesListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        handleVotacaoSelection(newValue);
                    }
                });
    }

    /**
     * Carrega e exibe a lista de todas as votações na ListView.
     * Este método é chamado após os serviços serem definidos.
     *
     * @since 09/06/25
     * @version 1.0
     */
    private void initializeVotacoesList() {
        if (votacaoService != null) {
            try {
                // Busca todas as votações e adiciona na ListView
                List<Votacao> todasVotacoes = votacaoService.buscarTodasVotacoes();
                ObservableList<Votacao> observableVotacoes = FXCollections.observableArrayList(todasVotacoes);
                votacoesListView.setItems(observableVotacoes);

                // Define como os objetos Votacao são exibidos na List (só o título)
                votacoesListView.setCellFactory(lv -> new javafx.scene.control.ListCell<Votacao>() {
                    @Override
                    protected void updateItem(Votacao votacao, boolean empty) {
                        super.updateItem(votacao, empty);
                        setText(empty ? null : votacao.getTitulo());
                    }
                });

            } catch (Exception e) {
                // Exibe alerta visual em caso de erro ao carregar votações
                showAlert(Alert.AlertType.ERROR, "Erro de Carregamento", "Não foi possível carregar as votações disponíveis: " + e.getMessage());
                System.err.println("Erro ao carregar votações: " + e.getMessage()); // Mantenha para depuração no console
                e.printStackTrace();
            }
        }
    }

    /**
     * Manipula a seleção de uma votação na ListView.
     * Atualiza o título da votação selecionada e carrega seus resultados na TableView.
     * @param votacao A votação selecionada.
     *
     * @since 09/06/25
     * @version 1.0
     */
    private void handleVotacaoSelection(Votacao votacao) {
        // Usa getDescricaoCargo() para exibir o cargo (que pode ser "Não Aplicável" para personalizada)
        // Atualiza o título da votação selecionada
        votacaoTituloLabel.setText("Resultados para: " + votacao.getTitulo() + " (Cargo: " + votacao.getDescricaoCargo() + ")");

        // Verifica se a votação está ativa
        if (!votacao.isAtiva()) {
            resultadosTableView.setItems(FXCollections.emptyObservableList());
            Label placeholderLabel = new Label("Esta votação não está ativa ou já foi encerrada.");
            placeholderLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 14px; -fx-alignment: CENTER;");
            resultadosTableView.setPlaceholder(placeholderLabel);
            return;
        } else {
            // Se a votação está ativa, remove qualquer mensagem de placeholder de votação inativa
            resultadosTableView.setPlaceholder(null);
        }

            try {
                // busca as opções de voto para a votação que foi selecionada
                List<OpcaoVoto> opcoesDaVotacao = opcaoVotoService.buscarOpcoesPorVotacao(votacao.getId());

                // ordena as opções pela quantidade de votos em ordem decrescente
                opcoesDaVotacao.sort(Comparator.comparing(OpcaoVoto::getQuantidadeVotos).reversed());

                ObservableList<OpcaoVoto> observableResultados = FXCollections.observableArrayList(opcoesDaVotacao);
                resultadosTableView.setItems(observableResultados);

                // Se não houver votos para uma votação ativa, exibe uma mensagem específica
                if (opcoesDaVotacao.isEmpty()) {
                    Label noResultsLabel = new Label("Nenhum voto registrado para esta votação ativa.");
                    noResultsLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 14px; -fx-alignment: CENTER;");
                    resultadosTableView.setPlaceholder(noResultsLabel);
                }

            } catch (Exception e) {
                // Exibe alerta visual em caso de erro ao carregar opções de voto
                showAlert(Alert.AlertType.ERROR, "Erro de Carregamento", "Erro ao carregar opções de voto para a votação " + votacao.getId() + ": " + e.getMessage());
                System.err.println("Erro ao carregar opções de voto para a votação " + votacao.getId() + ": " + e.getMessage());
                e.printStackTrace();
                // Em caso de erro, remove qualquer placeholder e exibe um alerta
                resultadosTableView.setPlaceholder(null);
            }
        }

    /**
     * Acionado pelo botão "Verificar Integridade". Este método busca todos os votos
     * da votação selecionada e verifica a integridade de cada um usando o serviço de voto.
     * Exibe o resultado da verificação em uma janela de alerta (Alert).
     *
     * @since 14/06/2025
     * @version 1.0
     */
    @FXML
    private void handleVerificarIntegridade() {
        Votacao votacaoSelecionada = votacoesListView.getSelectionModel().getSelectedItem();

        if (votacaoSelecionada == null) {
            showAlert(Alert.AlertType.WARNING, "Seleção Necessária", "Por favor, selecione uma votação primeiro para verificar a integridade.");
            // integridadeStatusLabel.setText("Por favor, selecione uma votação primeiro."); // REMOVIDO
            // integridadeStatusLabel.setTextFill(Color.PURPLE); // REMOVIDO
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Verificação de Integridade", "Iniciando verificação... Por favor, aguarde.");


        try {
            List<VotoModel> votos = votoService.buscarVotosPorVotacao(votacaoSelecionada.getId());

            if (votos.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Verificação Concluída", "Nenhum voto registrado para esta votação. Integridade não aplicável.");
                // integridadeStatusLabel.setText("Nenhum voto registrado para esta votação."); // REMOVIDO
                // integridadeStatusLabel.setTextFill(Color.PURPLE); // REMOVIDO
                return;
            }

            AtomicInteger votosAdulterados = new AtomicInteger(0);
            // Itera sobre a lista de votos para verificar a integridade de cada um
            votos.forEach(voto -> {
                try {
                    // Se o voto não for íntegro, incrementa o contador de adulterados
                    if (!votoService.verificarIntegridadeVoto(voto.getId())) {
                        votosAdulterados.incrementAndGet();
                    }
                } catch (Exception e) {
                    // Em caso de erro na verificação de um voto específico, também o considera adulterado
                    votosAdulterados.incrementAndGet();
                    System.err.println("Erro ao verificar integridade do voto ID " + voto.getId() + ": " + e.getMessage()); // Para depuração
                    e.printStackTrace();
                }
            });

            if (votosAdulterados.get() == 0) {
                showAlert(Alert.AlertType.INFORMATION, "Integridade Verificada",
                        String.format("Verificação concluída: %d votos analisados. Todos os votos estão íntegros!", votos.size()));
                // integridadeStatusLabel.setText(String.format("Verificação concluída: %d votos analisados. Todos íntegros!", votos.size())); // REMOVIDO
                // integridadeStatusLabel.setTextFill(Color.PURPLE); // REMOVIDO
            } else {
                showAlert(Alert.AlertType.ERROR, "ALERTA DE SEGURANÇA: Votos Adulterados!",
                        String.format("ALERTA: %d de %d votos foram ADULTERADOS! Verifique os logs para detalhes.", votosAdulterados.get(), votos.size()));
                // integridadeStatusLabel.setText(String.format("ALERTA: %d de %d votos foram ADULTERADOS!", votosAdulterados.get(), votos.size())); // REMOVIDO
                // integridadeStatusLabel.setTextFill(Color.RED); // REMOVIDO
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro na Verificação", "Ocorreu um erro ao realizar a verificação de integridade: " + e.getMessage());
            // integridadeStatusLabel.setText("Erro ao realizar a verificação de integridade."); // REMOVIDO
            // integridadeStatusLabel.setTextFill(Color.RED); // REMOVIDO
            e.printStackTrace(); // Mantenha para depuração
        }
    }

    /**
     * Manipula o evento de clique do botão "Voltar".
     * Retorna para a tela de admin.
     *
     * @since 09/06/25
     * @version 1.0
     */
    @FXML private void handleVoltarButton() {
        try {
            // Volta para a tela de admin, que é o ponto de entrada principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/telaadmin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) voltarButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tela de Admin");
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de administração    .");
            System.err.println("Erro ao carregar a tela de administração: " + e.getMessage()); // Mantenha para depuração
            e.printStackTrace();
        }
    }

    /**
     * Exibe um alerta na tela.
     * Este método é uma utilidade para exibir mensagens de forma consistente.
     * @param alertType O tipo de alerta (INFORMATION, ERROR, WARNING, etc.).
     * @param title O título do alerta.
     * @param message A mensagem a ser exibida no alerta.
     *
     * @version 1.0
     * @since 28/05/25 (ajustado de outros controllers para consistência)
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Opcional: pode ser usado para um cabeçalho mais detalhado
        alert.setContentText(message);
        alert.showAndWait();
    }
}