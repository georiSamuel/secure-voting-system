package dev.java10x;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class InterfaceApplication extends Application {
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