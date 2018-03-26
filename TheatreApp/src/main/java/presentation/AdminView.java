package presentation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AdminView extends Scene {

    public AdminView(BorderPane pane) {
        super(pane, 1500, 800);
        pane.setPadding(new Insets(10, 20, 10, 20));

        Label label = new Label("ADMIN VIEW");
        label.setFont(Font.font("Verdana", FontWeight.NORMAL,16));
        label.setStyle("-fx-text-fill: #187010");
        pane.setTop(label);

        VBox selectionBox = new VBox();
        Button showsSel = new Button("Shows");
        showsSel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Setting up the show view");
                ShowPane showPane = new ShowPane();
                pane.setCenter(showPane.getPane());
            }
        });


        Button cashiersSel = new Button("Cashiers");
        cashiersSel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Setting up the cashiers view");
                CashierPane cashierPane = new CashierPane();
                pane.setCenter(cashierPane.getPane());
            }
        });

        Button exportSel = new Button("Export");
        exportSel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Setting up the export view");
                ExportPane exportPane = new ExportPane();
                pane.setCenter(exportPane.getPane());
            }
        });

        Button logout = new Button("Log out");
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LoginView loginView = new LoginView(new BorderPane());
                TheatreApp.getPrimaryStage().setScene(loginView);
            }
        });

        selectionBox.setSpacing(6);
        selectionBox.getChildren().addAll(showsSel, cashiersSel, exportSel);
        pane.setLeft(selectionBox);

        pane.setBottom(logout);
    }
}
