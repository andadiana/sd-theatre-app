package presentation;

import business.model.Show;
import business.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ExportPane {

    private BorderPane pane;
    private ExportService exportService;

    public ExportPane() {
        pane = new BorderPane();

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

        Label exportMessage = new Label();

        ComboBox<Show> showBox;
        ServiceProvider serviceProvider = new ServiceProvider();
        ShowService showService = serviceProvider.getShowService();
        List<Show> availableShows = showService.findAll();
        ObservableList<Show> showOptions =
                FXCollections.observableArrayList(availableShows);
        showBox = new ComboBox<>(showOptions);
        showBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Show selectedShow = showBox.getSelectionModel().getSelectedItem();
                titleField.setText(selectedShow.getTitle());
                genreField.setText(selectedShow.getGenre().toString());
                dateField.setText(selectedShow.getDate().toString());
                castField.setText(selectedShow.getCast());
                exportMessage.setText("");
            }
        });

        Button csvExport = new Button("Export to CSV");
        csvExport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Show selectedShow = showBox.getSelectionModel().getSelectedItem();
                if (selectedShow != null) {
                    exportService = new CSVExportService();
                    if (exportService.exportSoldTickets(selectedShow)) {
                        exportMessage.setText("Successful export operation!");
                    }
                    else {
                        exportMessage.setText("Unsuccessful export operation!");
                    }
                }
                else {
                    exportMessage.setText("Please select show!");
                }
            }
        });

        Button xmlExport = new Button("Export to XML");
        xmlExport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Show selectedShow = showBox.getSelectionModel().getSelectedItem();
                if (selectedShow != null) {
                    exportService = new XMLExportService();
                    if (exportService.exportSoldTickets(selectedShow)) {
                        exportMessage.setText("Successful export operation!");
                    }
                    else {
                        exportMessage.setText("Unsuccessful export operation!");
                    }
                }
                else {
                    exportMessage.setText("Please select show!");
                }
            }
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(showBox, titleField, genreField, dateField,
                castField, exportMessage, csvExport, xmlExport);
        pane.setCenter(vBox);

    }

    public BorderPane getPane() {
        return pane;
    }
}
