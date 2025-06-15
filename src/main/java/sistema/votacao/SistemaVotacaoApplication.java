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
 *
 * <p>A anotação {@code @SpringBootApplication} habilita a configuração automática,
 * a varredura de componentes e a capacidade de ser uma fonte de definições de bean.
 * O {@code scanBasePackages} garante que todos os componentes dentro do pacote
 * "sistema.votacao" e seus subpacotes sejam detectados pelo Spring.</p>
 *
 * @version 2.1
 * @since 10/06/25
 */
@SpringBootApplication(scanBasePackages = {"sistema.votacao"})
public class SistemaVotacaoApplication extends Application {


    private static ConfigurableApplicationContext springContext;

    /**
     * Método de inicialização da aplicação JavaFX.
     * É chamado antes do método {@code start()}.
     * Neste método, o contexto da aplicação Spring Boot é inicializado.
     *
     * @throws Exception se ocorrer um erro durante a inicialização do contexto Spring.
     * @since 10/06/25
     * @version 1.0
     */
    @Override public void init() throws Exception {
        springContext = new SpringApplication(SistemaVotacaoApplication.class).run();
    }


    /**
     * Método de início da aplicação JavaFX.
     * É chamado após o método {@code init()} e é responsável por configurar e exibir
     * a interface gráfica principal da aplicação.
     *
     * @param primaryStage o palco (janela principal) da aplicação JavaFX.
     * @throws Exception se ocorrer um erro ao carregar o arquivo FXML ou configurar a cena.
     * @since 10/06/25
     * @version 1.0
     */
    @Override public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/views/login.fxml")));
        loader.setControllerFactory(springContext::getBean);

        Parent root = loader.load();

        primaryStage.setTitle("Sistema de Votação");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Método de parada da aplicação JavaFX.
     * É chamado quando a aplicação está sendo encerrada, permitindo a limpeza de recursos.
     * Neste método, o contexto do Spring é fechado.
     *
     * @throws Exception se ocorrer um erro durante o fechamento do contexto Spring ou na parada padrão.
     * @since 10/06/25
     * @version 1.0
     */
    @Override public void stop() throws Exception {
        if (springContext != null) {
            springContext.close();
        }
        super.stop();
    }

    /**
     * Método principal da aplicação.
     * Este é o ponto de entrada para a execução do programa.
     * Ele chama o método {@code launch()} do JavaFX para iniciar a aplicação.
     *
     * @param args argumentos de linha de comando.
     * @since 10/06/25
     * @version 1.0
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Método estático para permitir que os controllers acessem o contexto Spring
     * e carreguem outros FXMLs com injeção de dependências.
     *
     * @return O contexto da aplicação Spring.
     * @since 10/06/25
     * @version 1.0
     */
    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }
}
    