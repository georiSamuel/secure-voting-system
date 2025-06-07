package sistema.votacao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Aplicação do JavaFX
 * @author Suelle
 * @version 1.0
 * @since 26/05/25
 */
public class SistemaVotacaoApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Método provisório, funcional mas temporário
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/login.fxml")));
        primaryStage.setTitle("Sistema Inicial");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


/* Versão anterior

package sistema_votacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class sistema.votacao.SistemaVotacaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(sistema.votacao.SistemaVotacaoApplication.class, args);
    }
}
 */