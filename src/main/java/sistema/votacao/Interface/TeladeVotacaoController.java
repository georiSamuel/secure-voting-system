package sistema.votacao.Interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox; // Importe VBox
import javafx.stage.Stage;
import lombok.Setter; // Setter para setpreviousScreenIsAdmin
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sistema.votacao.usuario.model.TipoUsuario;
import sistema.votacao.usuario.model.UsuarioModel;
import sistema.votacao.votacao.model.Votacao;
import sistema.votacao.votacao.model.VotacaoAcademica;
import sistema.votacao.votacao.model.VotacaoEleitoral;
import sistema.votacao.votacao.service.VotacaoService;
import sistema.votacao.voto.dto.VotoRequestDTO;
import sistema.votacao.opcao_voto.model.OpcaoVoto;
import sistema.votacao.opcao_voto.service.OpcaoVotoService;
import sistema.votacao.voto.service.VotoService;
import sistema.votacao.usuario.service.UsuarioService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller responsável pela tela de votação.
 *
 * @author Suelle
 * @author Georis
 * @since 26/05/25
 * @version 1.6
 */
@Component
public class TeladeVotacaoController {
    @FXML private ListView<Votacao> votacoesDisponiveisListView;
    @FXML private Label votacaoTituloLabel;
    @FXML private Label votacaoDescricaoLabel;
    @FXML private Label mensagemErroLabel;
    @FXML private Button votarButton;
    @FXML private VBox opcoesVotoContainer;
    @FXML private ToggleGroup opcoesVotoGroup;

    @Setter private boolean previousScreenIsAdmin = false;

    @Autowired private VotacaoService votacaoService;
    @Autowired private OpcaoVotoService opcaoVotoService;
    @Autowired private VotoService votoService;
    @Autowired private UsuarioService usuarioService;
    private UsuarioModel usuarioLogado;

    @Setter private Long usuarioLogadoId;

    private ObservableList<Votacao> votacoesAbertas = FXCollections.observableArrayList();
    private Votacao votacaoSelecionada;
    private List<OpcaoVoto> opcoesAtuais;

    /**
     * Construtor padrão da classe TeladeVotacaoController.
     * <p>
     * Este construtor é utilizado pelos frameworks Spring e JavaFX para instanciar o controller.
     * A inicialização dos componentes da interface e a configuração de dependências
     * ocorrem após a construção do objeto, através das anotações {@literal @FXML} e {@literal @Autowired}.
     * A lógica de inicialização da tela, como o carregamento dos dados da votação,
     * deve ser colocada no método {@code initialize()}.
     * </p>
     *
     * @since 10/06/25
     * @version 1.0
     */
    public TeladeVotacaoController() {
        // Construtor vazio, a inicialização é feita pelo framework via injeção de dependência.
    }

    /**
     * Método de inicialização do controller. Chamado automaticamente pelo FXMLLoader após o carregamento do FXML.
     *
     * @since 13/05/25
     * @version 1.0
     */
    @FXML public void initialize() {
        this.usuarioLogado = LoginController.getUsuarioLogado();
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

    /**
     * Inicializa a lista de votações disponíveis na {@link #votacoesDisponiveisListView}.
     * Busca as votações ativas através do {@link VotacaoService} e exibe na tela
     *
     * @since 13/06/25
     * @version 1.0
     */
    @FXML private void initializeVotacoesList() {
        try {
            List<Votacao> ativas = votacaoService.buscarVotacoesAtivas();
            votacoesAbertas.setAll(ativas);
            votacoesDisponiveisListView.setItems(votacoesAbertas);

            votacoesDisponiveisListView.setCellFactory(lv -> new javafx.scene.control.ListCell<Votacao>() {
                @Override protected void updateItem(Votacao votacao, boolean empty) {
                    super.updateItem(votacao, empty);
                    if (empty || votacao == null) {
                        setText(null);
                    } else {
                        String titulo = votacao.getTitulo();
                        String cargoStr = "";
                        if (votacao instanceof VotacaoAcademica) {
                            cargoStr = ((VotacaoAcademica) votacao).getCargo().toString().replace("_", " ");
                        } else if (votacao instanceof VotacaoEleitoral) {
                            cargoStr = ((VotacaoEleitoral) votacao).getCargo().toString().replace("_", " ");
                        }

                        if (cargoStr != null && !cargoStr.isEmpty()) {
                            setText(titulo + " - Cargo: " + cargoStr);
                        } else {
                            setText(titulo);
                        }
                    }
                }
            });

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar as votações disponíveis: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Manipula a seleção de uma votação na {@link #votacoesDisponiveisListView}.
     * Atualiza os labels com o título da votação e carrega as opções de voto dinamicamente.
     *
     * @param votacao A votação selecionada.
     * @since 13/06/25
     * @version 1.0
     */
    @FXML private void handleVotacaoSelection(Votacao votacao) {
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

    /**
     * Manipula o clique do botão "Votar".
     * Verifica se uma votação e uma opção de voto foram selecionadas,
     * e então tenta registrar o voto do usuário. Após o voto, atualiza a lista de votações
     *
     * @param event O evento de ação que disparou este método.
     * @since 13/06/25
     * @version 1.0
     */
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

        if (this.usuarioLogado == null) {
            showAlert(Alert.AlertType.ERROR, "Erro de Autenticação", "Não foi possível identificar o usuário. Tente fazer login novamente.");
            return;
        }

        Long usuarioId = this.usuarioLogado.getId();
        Long opcaoVotoId = (Long) selectedRadioButton.getUserData();

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

            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Votação Concluída");
            confirmationDialog.setHeaderText("O que você gostaria de fazer agora?");
            confirmationDialog.setContentText("Escolha sua opção.");

            ButtonType buttonTypeContinuar = new ButtonType("Continuar Votando");
            ButtonType buttonTypeFinalizar = new ButtonType("Finalizar Sessão");

            confirmationDialog.getButtonTypes().setAll(buttonTypeContinuar, buttonTypeFinalizar);

            Optional<ButtonType> result = confirmationDialog.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeFinalizar) {
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
            } else {
                initializeVotacoesList();
                votacaoTituloLabel.setText("Selecione uma votação.");
                votacaoDescricaoLabel.setText("");
                opcoesVotoContainer.getChildren().clear();
                opcoesVotoGroup.selectToggle(null);
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro Inesperado", "Ocorreu um erro inesperado ao registrar o voto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Manipula o clique do botão "Voltar", redirecionando o usuário para a tela de usuário.
     *
     * @param event O evento de ação que disparou este método.
     * @since 13/06/25
     * @version 1.0
     */
    @FXML private void handleVoltarButton(ActionEvent event) {
        try {
            String fxmlPath;
            String newTitle; // Variável para guardar o novo título

            // Verifica o tipo de usuário para definir o caminho do FXML e o título
            if (usuarioLogado != null && usuarioLogado.getTipoUsuario() == TipoUsuario.Tipo.ADMIN) {
                fxmlPath = "/views/telaadmin.fxml";
                newTitle = "Tela de Admin"; // Define o título para Admin
            } else {
                fxmlPath = "/views/telausuario.fxml";
                newTitle = "Tela do Usuário"; // Define o título para Usuário
            }

            // Carrega o FXML de forma estática
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));

            // Obtém a janela atual
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Define a nova cena e o novo título
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(newTitle);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,"Erro", "Não foi possível voltar para a tela anterior.");
        }
    }

    /**
     * Exibe um alerta pop-up com o tipo, título e mensagem especificados.
     *
     * @param alertType O tipo de alerta (e.g., INFORMATION, WARNING, ERROR).
     * @param title     O título do pop-up do alerta.
     * @param message   A mensagem a ser exibida no corpo do alerta.
     * @since 13/06/25
     * @version 1.0
     */
    @FXML private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}