package presentation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TheatreApp extends Application  {

    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        stage = primaryStage;
        Scene loginView = new LoginView(new BorderPane());
        stage.setTitle("Theatre Ticketing Application");
        stage.setScene(loginView);
        stage.show();
    }

    public static Stage getPrimaryStage() {
        return stage;
    }
}