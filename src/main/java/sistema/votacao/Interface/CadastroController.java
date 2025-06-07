package sistema.votacao.Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class CadastroController {

    @FXML
    private TextField nome;
    @FXML
    private TextField cpf;
    @FXML
    private TextField email;
    @FXML
    private PasswordField senha;

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void cadastrar(ActionEvent event) {
        String nomeText = nome.getText();
        String cpfText = cpf.getText();
        String emailText = email.getText();
        String senhaText = senha.getText();

        if (nomeText.isEmpty() || cpfText.isEmpty() || emailText.isEmpty() || senhaText.isEmpty()) {
            showAlert(AlertType.ERROR, "Erro de Cadastro", "Por favor, preencha todos os campos.");
            return;
        }

        java.util.Map<String, String> requestBodyMap = new java.util.HashMap<>();
        requestBodyMap.put("nome", nomeText);
        requestBodyMap.put("cpf", cpfText);
        requestBodyMap.put("email", emailText);
        requestBodyMap.put("senha", senhaText);
        requestBodyMap.put("tipo", "COMUM"); //tipo padrão para o cadastro


        try {
            String json = objectMapper.writeValueAsString(requestBodyMap); // Converte o Map para JSON

            RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url("http://localhost:8080/usuario/cadastro") // Altere a porta se o seu Spring Boot estiver em outra
                    .post(body)
                    .build();

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace(); // Imprime o erro no console
                    javafx.application.Platform.runLater(() ->
                            showAlert(AlertType.ERROR, "Erro de Conexão", "Não foi possível conectar ao servidor. Verifique se o backend está rodando.")
                    );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseBody = response.body().string();
                    if (response.isSuccessful()) {
                        javafx.application.Platform.runLater(() -> {
                            showAlert(AlertType.INFORMATION, "Sucesso", "Usuário cadastrado com sucesso!");
                            clearFields();
                            abrirLogin(null);
                        });
                    } else {
                        javafx.application.Platform.runLater(() -> {
                            showAlert(AlertType.ERROR, "Erro de Cadastro", "Erro ao cadastrar usuário: " + responseBody);
                        });
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro", "Erro ao preparar os dados para cadastro.");
        }
    }

    @FXML
    public void abrirLogin(ActionEvent event) {
        try {
            // Carrega o FXML da tela de login
            Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de login.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nome.clear();
        cpf.clear();
        email.clear();
        senha.clear();
    }
}