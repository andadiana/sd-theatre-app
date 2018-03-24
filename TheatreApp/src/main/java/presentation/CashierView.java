package presentation;

import business.model.Show;
import business.service.ShowService;
import business.service.ShowServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.sql.Timestamp;
import java.util.List;

public class CashierView extends Scene {

    private ShowService showService;


    public CashierView(BorderPane pane) {
        super(pane, 1000, 600);

        showService = new ShowServiceImpl();

        Label label = new Label("CASHIER VIEW");
        pane.setTop(label);

        TextField titleField = new TextField();
        titleField.setPromptText("title");
        titleField.setEditable(false);

        TextField genreField = new TextField();
        genreField.setPromptText("genre");
        genreField.setEditable(false);

        TextField dateField = new TextField();
        dateField.setPromptText("date");
        dateField.setEditable(false);

        TextField castField = new TextField();
        castField.setPromptText("cast");
        castField.setEditable(false);

        ComboBox<Show> showBox;
        List<Show> availableShows = showService.getAllAvailable(new Timestamp(System.currentTimeMillis()));
        ObservableList<Show> showOptions =
                FXCollections.observableArrayList(availableShows);
        showBox = new ComboBox<>(showOptions);
        showBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Show show = showBox.getSelectionModel().getSelectedItem();
                titleField.setText(show.getTitle());
                genreField.setText(show.getGenre().toString());
                dateField.setText(show.getDate().toString());
                castField.setText(show.getCast());
            }
        });





        VBox vBox = new VBox();
        vBox.getChildren().addAll(showBox, titleField, genreField, dateField, castField);
        pane.setCenter(vBox);
    }
}
