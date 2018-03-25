package presentation;

import business.model.User;
import business.service.CashierService;
import business.service.CashierServiceImpl;
import business.service.ShowService;
import business.service.ShowServiceImpl;
import dataaccess.repository.ShowRepositoryMySql;
import dataaccess.repository.UserRepositoryMySql;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class AdminView extends Scene {

    public AdminView(BorderPane pane) {
        super(pane, 1000, 600);

        //TODO: add button to go back to login view

        ShowService showService = new ShowServiceImpl(new ShowRepositoryMySql());
        CashierService cashierService = new CashierServiceImpl(new UserRepositoryMySql());

        Label label = new Label("ADMIN VIEW");
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

        selectionBox.getChildren().addAll(showsSel, cashiersSel, exportSel);
        pane.setLeft(selectionBox);

        BorderPane centerPane = new BorderPane();
        pane.setCenter(centerPane);

    }
}
