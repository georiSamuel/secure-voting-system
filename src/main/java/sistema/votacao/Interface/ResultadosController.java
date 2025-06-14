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
import sistema.votacao.Votacao.Model.Votacao;
import sistema.votacao.Votacao.Service.VotacaoService;
import sistema.votacao.Voto.Model.OpcaoVoto;
import sistema.votacao.Voto.Model.VotoModel;
import sistema.votacao.Voto.Service.OpcaoVotoService;
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
                        integridadeStatusLabel.setText(""); // Limpa o status ao selecionar nova votação
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
                // Esses alertas são todos básicos, podem ser implementados para alerta visual depois, mas por enquanto é
                // só para ver a funcionalidade
                System.err.println("Erro ao carregar votações: " + e.getMessage());
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
                System.err.println("Erro ao carregar opções de voto para a votação " + votacao.getId() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Acionado pelo botão "Verificar Integridade". Este método busca todos os votos
     * da votação selecionada e verifica a integridade de cada um usando o serviço de voto.
     * Atualiza o label 'integridadeStatusLabel' com o resultado da verificação.
     *
     * @since 14/06/2025
     * @version 1.0
     */
    @FXML
    private void handleVerificarIntegridade() {
        Votacao votacaoSelecionada = votacoesListView.getSelectionModel().getSelectedItem();

        if (votacaoSelecionada == null) {
            integridadeStatusLabel.setText("Por favor, selecione uma votação primeiro.");
            integridadeStatusLabel.setTextFill(Color.PURPLE);
            return;
        }

        integridadeStatusLabel.setText("Verificando... por favor, aguarde.");
        integridadeStatusLabel.setTextFill(Color.PURPLE);

        try {
            List<VotoModel> votos = votoService.buscarVotosPorVotacao(votacaoSelecionada.getId());

            if (votos.isEmpty()) {
                integridadeStatusLabel.setText("Nenhum voto registrado para esta votação.");
                integridadeStatusLabel.setTextFill(Color.PURPLE);
                return;
            }

            AtomicInteger votosAdulterados = new AtomicInteger(0);
            votos.forEach(voto -> {
                try {
                    if (!votoService.verificarIntegridadeVoto(voto.getId())) {
                        votosAdulterados.incrementAndGet();
                    }
                } catch (Exception e) {
                    votosAdulterados.incrementAndGet();
                }
            });

            if (votosAdulterados.get() == 0) {
                integridadeStatusLabel.setText(String.format("Verificação concluída: %d votos analisados. Todos íntegros!", votos.size()));
                integridadeStatusLabel.setTextFill(Color.PURPLE);
            } else {
                integridadeStatusLabel.setText(String.format("ALERTA: %d de %d votos foram ADULTERADOS!", votosAdulterados.get(), votos.size()));
                integridadeStatusLabel.setTextFill(Color.RED);
            }

        } catch (Exception e) {
            integridadeStatusLabel.setText("Erro ao realizar a verificação de integridade.");
            integridadeStatusLabel.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }

    /**
     * Manipula o evento de clique do botão "Voltar".
     * Retorna para a tela do admin (TelaAdminController).
     *
     * @since 09/06/25
     * @version 1.0
     */
    @FXML
    private void handleVoltarButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistema/votacao/Interface/TelaAdmin.fxml")); // Assegure-se que o caminho está correto
            Parent root = loader.load();

            Stage stage = (Stage) voltarButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tela de Administração");

        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela de administração: " + e.getMessage());
            e.printStackTrace();
        }
    }
}