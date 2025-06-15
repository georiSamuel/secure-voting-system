package sistema.votacao.Interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import sistema.votacao.Votacao.Model.Votacao;
import sistema.votacao.Votacao.Service.VotacaoService;
import sistema.votacao.OpcaoVoto.Model.OpcaoVoto;
import sistema.votacao.Voto.Model.VotoModel;
import sistema.votacao.OpcaoVoto.Service.OpcaoVotoService;
import sistema.votacao.Voto.Service.VotoService;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe responsável pela tela de Resultados do JavaFX
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
     */
    @FXML private Button verificarIntegridadeButton;

    /**
     * Label para exibir o status da verificação de integridade.
     */
    @FXML private Label integridadeStatusLabel;

    private VotacaoService votacaoService;
    private OpcaoVotoService opcaoVotoService;
    private VotoService votoService;

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
        opcaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        votosColumn.setCellValueFactory(new PropertyValueFactory<>("quantidadeVotos"));

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
                List<Votacao> todasVotacoes = votacaoService.buscarTodasVotacoes();
                ObservableList<Votacao> observableVotacoes = FXCollections.observableArrayList(todasVotacoes);
                votacoesListView.setItems(observableVotacoes);

                votacoesListView.setCellFactory(lv -> new javafx.scene.control.ListCell<Votacao>() {
                    @Override
                    protected void updateItem(Votacao votacao, boolean empty) {
                        super.updateItem(votacao, empty);
                        setText(empty ? null : votacao.getTitulo());
                    }
                });

            } catch (Exception e) {
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
        votacaoTituloLabel.setText("Resultados para: " + votacao.getTitulo() + " (Cargo: " + votacao.getDescricaoCargo() + ")");

        if (opcaoVotoService != null) {
            try {
                // busca as opções de voto para a votação que foi selecionada
                List<OpcaoVoto> opcoesDaVotacao = opcaoVotoService.buscarOpcoesPorVotacao(votacao.getId());

                // ordena as opções pela quantidade de votos em ordem decrescente
                opcoesDaVotacao.sort(Comparator.comparing(OpcaoVoto::getQuantidadeVotos).reversed());

                ObservableList<OpcaoVoto> observableResultados = FXCollections.observableArrayList(opcoesDaVotacao);
                resultadosTableView.setItems(observableResultados);

            } catch (Exception e) {
                // Exibe alerta visual em caso de erro ao carregar opções de voto
                showAlert(Alert.AlertType.ERROR, "Erro de Carregamento", "Erro ao carregar opções de voto para a votação " + votacao.getId() + ": " + e.getMessage());
                System.err.println("Erro ao carregar opções de voto para a votação " + votacao.getId() + ": " + e.getMessage()); // Mantenha para depuração
                e.printStackTrace();
            }
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
     * Retorna para a tela do admin (TelaAdminController).
     *
     * @since 09/06/25
     * @version 1.0
     */
    @FXML private void handleVoltarButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/telaadmin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) voltarButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Área de Administração");
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de administração    .");
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