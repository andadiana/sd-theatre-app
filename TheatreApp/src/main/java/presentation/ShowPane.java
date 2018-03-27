package presentation;

import business.model.Show;
import business.service.SeatService;
import business.service.ServiceProvider;
import business.service.ServiceProviderImpl;
import business.service.ShowService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowPane{

    private final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss.S";
    private BorderPane pane;
    private ShowService showService;

    //show table
    private TableView<Show> showTable;
    private TableColumn<Show, String> titleCol;
    private TableColumn<Show, Show.Genre> genreCol;
    private TableColumn<Show, Timestamp> dateCol;
    private TableColumn<Show, Integer> nrTicketsCol;

    //show CRUD fields
    private TextField titleField;
    private ComboBox<Show.Genre> genreBox;
    private TextField dateField;
    private TextArea castField;
    private TextField nrTicketsField;
    private Label showError;


    public ShowPane() {

        pane = new BorderPane();
        pane.setPadding(new Insets(10, 20, 10, 20));

        ServiceProvider serviceProvider = new ServiceProviderImpl();
        showService = serviceProvider.getShowService();

        createShowTable();

        titleField = new TextField();
        titleField.setFocusTraversable(false);
        titleField.setPromptText("title");

        ObservableList<Show.Genre> genreOptions =
                FXCollections.observableArrayList(Show.Genre.values());
        genreBox = new ComboBox<>(genreOptions);


        dateField = new TextField();
        dateField.setFocusTraversable(false);
        dateField.setPromptText("date");

        castField = new TextArea();
        castField.setFocusTraversable(false);
        castField.setPromptText("cast");

        nrTicketsField = new TextField();
        nrTicketsField.setFocusTraversable(false);
        nrTicketsField.setPromptText("number of tickets");

        showError = new Label();

        Button addShow = new Button("Add Show");
        addShow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addShowScene();
            }
        });

        Button updateShow = new Button("Update");
        updateShow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (showTable.getSelectionModel().getSelectedItem() != null) {
                    if (validShowInput(titleField.getText(), genreBox.getSelectionModel().getSelectedItem(),
                            castField.getText(), dateField.getText(), nrTicketsField.getText(), showError)) {
                        Show show = showTable.getSelectionModel().getSelectedItem();
                        show.setTitle(titleField.getText());
                        show.setCast(castField.getText());
                        show.setGenre(genreBox.getSelectionModel().getSelectedItem());
                        show.setNrTickets(Integer.parseInt(nrTicketsField.getText()));
                        Timestamp date = stringToTimestamp(dateField.getText());
                        if (date == null) {
                            showError.setText("Invalid format for date! Correct format is: yyyy-MM-dd hh:mm");
                        }
                        else {
                            show.setDate(date);

                            System.out.println("Updating" + show);
                            if (showService.update(show)) {
                                System.out.println("Successful update for show");
                                populateShowTable();
                                showError.setText("");
                            } else {
                                showError.setText("Unsuccessful update operation");
                            }
                        }
                    }
                }
                else {
                    showError.setText("Please select show from table!");
                }
            }
        });

        Button deleteShow = new Button("Delete");
        deleteShow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (showTable.getSelectionModel().getSelectedItem() != null) {
                    Show show = showTable.getSelectionModel().getSelectedItem();

                    System.out.println("Deleting " + show);
                    if (showService.delete(show)) {
                        System.out.println("Successful delete for show");
                        populateShowTable();
                        showError.setText("");
                        titleField.clear();
                        dateField.clear();
                        castField.clear();
                        dateField.clear();
                        genreBox.setValue(Show.Genre.Opera);
                    } else {
                        showError.setText("Unsuccessful delete operation");
                    }

                }
                else {
                    showError.setText("Please select show from table!");
                }
            }
        });

        showTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Show show = showTable.getSelectionModel().getSelectedItem();
                if (show != null) {
                    titleField.setText(show.getTitle());
                    genreBox.setValue(show.getGenre());
                    dateField.setText(show.getDate().toString());
                    castField.setText(show.getCast());
                    nrTicketsField.setText(Integer.toString(show.getNrTickets()));
                }
            }
        });

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().addAll(addShow, titleField, genreBox, dateField,
                castField, nrTicketsField, showError, updateShow, deleteShow);
        pane.setRight(vbox);
    }

    public BorderPane getPane() {
        return pane;
    }

    private void createShowTable() {
        showTable = new TableView<>();
        titleCol = new TableColumn<>("Title");
        genreCol = new TableColumn<>("Genre");
        dateCol = new TableColumn<>("Date");
        nrTicketsCol = new TableColumn<>("Nr Tickets");

        populateShowTable();

        showTable.getColumns().addAll(titleCol, genreCol, dateCol, nrTicketsCol);
        pane.setCenter(showTable);

    }

    private void populateShowTable() {
        ObservableList<Show> shows = FXCollections.observableArrayList(showService.findAll());
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        nrTicketsCol.setCellValueFactory(new PropertyValueFactory<>("nrTickets"));

        showTable.setItems(shows);
    }

    private Timestamp stringToTimestamp(String s){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            Date parsedDate = dateFormat.parse(s);
            System.out.println("parsed date: " + parsedDate);
            return new java.sql.Timestamp(parsedDate.getTime());
        } catch(Exception e) {
            return null;
        }
    }

    private boolean validShowInput(String title, Show.Genre genre, String cast, String date, String nrTickets, Label errorLabel) {
        if (title.length() == 0) {
            errorLabel.setText("Title field cannot be empty!");
            return false;
        }
        if (genre == null) {
            errorLabel.setText("Genre must be selected!");
            return false;
        }
        if (cast.length() == 0) {
            errorLabel.setText("Cast field cannot be empty!");
            return false;
        }
        if (date.length() == 0) {
            errorLabel.setText("Date field cannot be empty!");
            return false;
        }
        if (nrTickets.length() == 0) {
            errorLabel.setText("Number of tickets field cannot be empty!");
            return false;
        }
        if (!nrTickets.matches("-?\\d+")) {
            errorLabel.setText("Number of tickets must be an integer!");
            return false;
        }
        int nrSeats = SeatService.THEATRE_COLS * SeatService.THEATRE_ROWS;
        if (Integer.parseInt(nrTickets) > nrSeats){
            errorLabel.setText("Number of tickets cannot exceed number of seats in theatre (" + nrSeats + ")!");
            return  false;
        }
        return true;
    }

    private void addShowScene() {
        Stage stage = new Stage();
        VBox vbox = new VBox();

        TextField addTitle = new TextField();
        addTitle.setPromptText("Title");
        addTitle.setFocusTraversable(false);

        ObservableList<Show.Genre> genreOptions =
                FXCollections.observableArrayList(Show.Genre.values());
        ComboBox<Show.Genre> addGenre = new ComboBox<>(genreOptions);

        TextField addDate = new TextField();
        addDate.setPromptText("yyyy-MM-dd hh:mm");
        addDate.setFocusTraversable(false);

        TextArea addCast = new TextArea();
        addCast.setPromptText("Cast");
        addCast.setFocusTraversable(false);

        TextField addNrTickets = new TextField();
        addNrTickets.setPromptText("Number of tickets");
        addNrTickets.setFocusTraversable(false);

        Label addError = new Label();

        Button add = new Button("Add");
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (validShowInput(addTitle.getText(), addGenre.getSelectionModel().getSelectedItem(),
                        addCast.getText(), addDate.getText(), addNrTickets.getText(), addError)) {
                    Show show = new Show();
                    show.setTitle(addTitle.getText());
                    show.setCast(addCast.getText());
                    show.setGenre(addGenre.getSelectionModel().getSelectedItem());
                    show.setNrTickets(Integer.parseInt(addNrTickets.getText()));
                    String dateString = addDate.getText() + ":00.0";
                    Timestamp date = stringToTimestamp(dateString);
                    if (date == null) {
                        addError.setText("Invalid format for date! Correct format is: yyyy-MM-dd hh:mm");
                    }
                    else {
                        show.setDate(date);

                        System.out.println("Inserting " + show);
                        int res = showService.create(show);
                        if (res != 0) {
                            System.out.println("Successful create for show");
                            populateShowTable();
                            addError.setText("");
                            stage.close();
                        } else {
                            addError.setText("Unsuccessful add operation");
                        }
                    }
                }
            }
        });

        vbox.setSpacing(6);
        vbox.setPadding(new Insets(10, 20, 10, 20));
        vbox.getChildren().addAll(addTitle, addGenre, addDate,
                addCast, addNrTickets, addError, add);
        stage.setScene(new Scene(vbox, 500, 400));
        stage.setTitle("Add new show");
        stage.show();
    }
}
