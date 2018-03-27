package presentation;

import business.model.Seat;
import business.model.Show;
import business.model.Ticket;
import business.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.List;

public class CashierView extends Scene {

    private Ticket[][] tickets;
    private Label[][] seatLabels;

    private TicketService ticketService;
    private SeatService seatService;
    private int availableTickets;


    public CashierView(BorderPane pane) {
        super(pane, 1500, 800);
        pane.setPadding(new Insets(10, 20, 10, 20));

        ServiceProvider serviceProvider = new ServiceProviderImpl();
        ShowService showService = serviceProvider.getShowService();
        ticketService = serviceProvider.getTicketService();
        seatService = serviceProvider.getSeatService();

        tickets = new Ticket[SeatService.THEATRE_ROWS + 1][SeatService.THEATRE_COLS + 1];
        seatLabels = new Label[SeatService.THEATRE_ROWS + 1][SeatService.THEATRE_COLS + 1];

        Label label = new Label("CASHIER VIEW");
        label.setFont(Font.font("Verdana", FontWeight.NORMAL,16));
        label.setStyle("-fx-text-fill: #187010");
        pane.setTop(label);

        Label titleLabel = new Label("Title: ");
        TextField titleField = new TextField();
        titleField.setPromptText("title");
        titleField.setEditable(false);
        HBox titleHBox = new HBox();
        titleHBox.setSpacing(5);
        titleHBox.getChildren().addAll(titleLabel, titleField);

        Label genreLabel = new Label("Genre: ");
        TextField genreField = new TextField();
        genreField.setPromptText("genre");
        genreField.setEditable(false);
        HBox genreHBox = new HBox();
        genreHBox.setSpacing(5);
        genreHBox.getChildren().addAll(genreLabel, genreField);

        Label dateLabel = new Label("Date: ");
        TextField dateField = new TextField();
        dateField.setPromptText("date");
        dateField.setEditable(false);
        HBox dateHBox = new HBox();
        dateHBox.setSpacing(5);
        dateHBox.getChildren().addAll(dateLabel, dateField);

        Label castLabel = new Label("Cast: ");
        TextArea castField = new TextArea();
        castField.setPromptText("cast");
        castField.setEditable(false);
        VBox castVBox = new VBox();
        castVBox.setSpacing(5);
        castVBox.getChildren().addAll(castLabel, castField);

        Label nrTicketsLabel = new Label("Number of tickets: ");
        TextField nrTicketsField = new TextField();
        nrTicketsField.setPromptText("number of tickets");
        nrTicketsField.setEditable(false);
        HBox ticketsHBox = new HBox();
        ticketsHBox.setSpacing(5);
        ticketsHBox.getChildren().addAll(nrTicketsLabel, nrTicketsField);

        GridPane seatGrid = new GridPane();
        seatGrid.setPadding(new Insets(10, 20, 10, 20));
        seatGrid.setHgap(10);
        //seatGrid.setVgap(10);

        Label rowLabel = new Label("Row:");
        TextField rowField = new TextField();
        rowField.setEditable(false);
        HBox rowHBox = new HBox();
        rowHBox.setSpacing(5);
        rowHBox.getChildren().addAll(rowLabel, rowField);

        Label seatLabel = new Label("Seat:");
        TextField seatField = new TextField();
        seatField.setEditable(false);
        HBox seatHBox = new HBox();
        seatHBox.setSpacing(5);
        seatHBox.getChildren().addAll(seatLabel, seatField);

        Label availableTicketLabel = new Label();

        Label ticketError = new Label();

        ComboBox<Show> showBox;
        List<Show> availableShows = showService.getAllAvailable(new Timestamp(System.currentTimeMillis()));
        ObservableList<Show> showOptions =
                FXCollections.observableArrayList(availableShows);
        showBox = new ComboBox<>(showOptions);

        showBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                seatGrid.getChildren().clear();

                Show selectedShow = showBox.getSelectionModel().getSelectedItem();
                if (selectedShow != null) {
                    titleField.setText(selectedShow.getTitle());
                    genreField.setText(selectedShow.getGenre().toString());
                    dateField.setText(selectedShow.getDate().toString());
                    castField.setText(selectedShow.getCast());
                    nrTicketsField.setText(Integer.toString(selectedShow.getNrTickets()));
                    availableTickets = selectedShow.getNrTickets();
                    rowField.clear();
                    seatField.clear();

                    if (ticketService.nrTicketsExceeded(selectedShow)) {
                        ticketError.setText("Number of tickets exceeded!");
                    } else {
                        ticketError.setText("");
                    }

                    List<Ticket> reservedTickets = ticketService.findSoldTicketsForShow(selectedShow);

                    for (int i = 1; i <= SeatService.THEATRE_ROWS; i++) {
                        for (int j = 1; j <= SeatService.THEATRE_COLS; j++) {
                            Label seatLabel = new Label("Row: " + i + " seat: " + j);
                            int rowNr = i;
                            int seatNr = j;
                            seatLabels[i][j] = seatLabel;
                            tickets[i][j] = null;

                            seatLabel.setStyle("-fx-background-color: #3dc764;");
                            seatLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {

                                    rowField.setText(Integer.toString(rowNr));
                                    seatField.setText(Integer.toString(seatNr));

                                }
                            });
                            seatGrid.add(seatLabel, j, i);
                        }
                    }

                    for (Ticket ticket : reservedTickets) {
                        int rowNr = ticket.getSeat().getRowNr();
                        int seatNr = ticket.getSeat().getSeatNr();

                        seatLabels[rowNr][seatNr].setStyle("-fx-background-color: #fe5858;");
                        tickets[rowNr][seatNr] = ticket;
                    }
                    availableTickets -= reservedTickets.size();
                    availableTicketLabel.setText("Available tickets: " + availableTickets);
                }
            }
        });

        Button reserveButton = new Button("Reserve");
        reserveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Show selectedShow = showBox.getSelectionModel().getSelectedItem();
                if (selectedShow != null) {
                    if (ticketService.nrTicketsExceeded(selectedShow)) {
                        ticketError.setText("Number of tickets exceeded!");
                    }
                    else {
                        if (rowField.getText().length() > 0 || rowField.getText().length() > 0) {
                            int rowNr = Integer.parseInt(rowField.getText());
                            int seatNr = Integer.parseInt(seatField.getText());
                            if (tickets[rowNr][seatNr] != null) {
                                ticketError.setText("Ticket is already reserved!");
                            } else {
                                Ticket newTicket = ticketService.reserveTicket(selectedShow, rowNr, seatNr);
                                seatLabels[rowNr][seatNr].setStyle("-fx-background-color: #fe5858;");
                                tickets[rowNr][seatNr] = newTicket;
                                availableTickets--;
                                availableTicketLabel.setText("Available tickets: " + availableTickets);
                                ticketError.setText("");
                            }
                        } else {
                            ticketError.setText("Please select seat!");
                        }
                    }
                }
                else {
                    ticketError.setText("Please select show!");
                }
            }
        });

        Button cancelReservation = new Button("Cancel reservation");
        cancelReservation.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (rowField.getText().length() > 0 || rowField.getText().length() > 0) {
                    int rowNr = Integer.parseInt(rowField.getText());
                    int seatNr = Integer.parseInt(seatField.getText());
                    if (tickets[rowNr][seatNr] != null) {
                        Ticket ticket = tickets[rowNr][seatNr];
                        ticket.setReserved(false);
                        ticketService.cancelReservation(ticket);
                        tickets[rowNr][seatNr] = null;
                        seatLabels[rowNr][seatNr].setStyle("-fx-background-color: #3dc764;");
                        availableTickets++;
                        availableTicketLabel.setText("Available tickets: " + availableTickets);
                        ticketError.setText("");
                    }
                    else {
                        ticketError.setText("Ticket was not reserved!");
                    }
                }
                else {
                    ticketError.setText("Please select seat!");
                }
            }
        });

        Button editSeat = new Button("Edit seat");
        editSeat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (rowField.getText().length() > 0 || rowField.getText().length() > 0) {
                    int rowNr = Integer.parseInt(rowField.getText());
                    int seatNr = Integer.parseInt(seatField.getText());
                    Ticket reservedTicket = tickets[rowNr][seatNr];
                    if (reservedTicket != null) {
                        editSeatScene(reservedTicket);
                        ticketError.setText("");
                    }
                    else {
                        ticketError.setText("Ticket was not reserved!");
                    }
                }
                else {
                    ticketError.setText("Please select seat!");
                }
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


        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.getChildren().addAll(showBox, titleHBox, genreHBox, dateHBox, castVBox, ticketsHBox,
                seatGrid, rowHBox, seatHBox, availableTicketLabel, ticketError, reserveButton,
                cancelReservation, editSeat);
        pane.setCenter(vBox);

        pane.setBottom(logout);
    }

    private void editSeatScene(Ticket ticket) {
        Stage stage = new Stage();
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 20, 10, 20));

        TextField editRowField = new TextField();
        editRowField.setPromptText("Row number");
        editRowField.setFocusTraversable(false);

        TextField editSeatField = new TextField();
        editSeatField.setPromptText("Seat number");
        editSeatField.setFocusTraversable(false);

        Label editError = new Label();

        Button edit = new Button("Edit");
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (validSeatInput(editRowField.getText(), editSeatField.getText())) {
                    int rowNr = Integer.parseInt(editRowField.getText());
                    int seatNr = Integer.parseInt(editSeatField.getText());
                    Seat oldSeat = ticket.getSeat();
                    System.out.println("Editing seat " + ticket + " new seat: row " + rowNr + " seat " + seatNr);
                    if (ticketService.editSeat(ticket, rowNr, seatNr)) {
                        System.out.println("Successful edit for seat");
                        editError.setText("");
                        tickets[oldSeat.getRowNr()][oldSeat.getSeatNr()] = null;
                        seatLabels[oldSeat.getRowNr()][oldSeat.getSeatNr()].setStyle("-fx-background-color: #3dc764;");
                        tickets[rowNr][seatNr] = ticket;
                        seatLabels[rowNr][seatNr].setStyle("-fx-background-color: #fe5858;");
                        stage.close();
                    }
                    else {
                        editError.setText("Unsuccessful edit operation");
                    }
                }
                else {
                    editError.setText("Invalid seat!");
                }
            }
        });

        vbox.getChildren().addAll(editRowField, editSeatField, editError, edit);
        stage.setScene(new Scene(vbox, 200, 200));
        stage.setTitle("Edit seat");
        stage.show();
    }

    private boolean validSeatInput(String rowNr, String seatNr) {
        int row;
        int seat;
        try {
            row = Integer.parseInt(rowNr);
            seat = Integer.parseInt(seatNr);
        } catch (NumberFormatException e) {
            return false;
        }

        if (row > 0 && row <= SeatService.THEATRE_ROWS && seat > 0 && seat <= SeatService.THEATRE_COLS) {
            return true;
        }
        return false;
    }
}
