package sistema.votacao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

/**
 * Aplicação do JavaFX integrada com Spring Boot.
 * Esta classe inicia o contexto Spring e lança a interface gráfica.
 * @version 2.1
 * @since 10/06/25
 */
@SpringBootApplication(scanBasePackages = {"sistema.votacao"})
public class SistemaVotacaoApplication extends Application {

    private static ConfigurableApplicationContext springContext;

    @Override
    public void init() throws Exception {
        springContext = new SpringApplication(SistemaVotacaoApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/telaDeVotacao.fxml")));
        loader.setControllerFactory(springContext::getBean);

        Parent root = loader.load();

        primaryStage.setTitle("Sistema de Votação");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (springContext != null) {
            springContext.close();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Método estático para permitir que os controllers acessem o contexto Spring
     * e carreguem outros FXMLs com injeção de dependências.
     * @return O contexto da aplicação Spring.
     */
    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }
}
    