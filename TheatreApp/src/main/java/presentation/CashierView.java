package presentation;

import business.model.Seat;
import business.model.Show;
import business.model.Ticket;
import business.service.*;
import dataaccess.repository.SeatRepositoryMySql;
import dataaccess.repository.ShowRepositoryMySql;
import dataaccess.repository.TicketRepositoryCacheDecorator;
import dataaccess.repository.TicketRepositoryMySql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.List;

public class CashierView extends Scene {

    private Ticket[][] tickets;
    private Label[][] ticketLabels;

    private TicketService ticketService;
    public CashierView(BorderPane pane) {
        super(pane, 1000, 600);

        //TODO add button to log out and go back to login view

        ShowService showService = new ShowServiceImpl(new ShowRepositoryMySql());
        ticketService = new TicketServiceImpl(new TicketRepositoryCacheDecorator(new TicketRepositoryMySql()));

        Show selectedShow = null;

        tickets = new Ticket[SeatService.THEATRE_ROWS + 1][SeatService.THEATRE_COLS + 1];
        ticketLabels = new Label[SeatService.THEATRE_ROWS + 1][SeatService.THEATRE_COLS + 1];

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

        GridPane seatGrid = new GridPane();

        TextField rowField = new TextField();
        rowField.setEditable(false);

        TextField seatField = new TextField();
        seatField.setEditable(false);

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
                titleField.setText(selectedShow.getTitle());
                genreField.setText(selectedShow.getGenre().toString());
                dateField.setText(selectedShow.getDate().toString());
                castField.setText(selectedShow.getCast());
                rowField.clear();
                seatField.clear();

                if (ticketService.nrTicketsExceeded(selectedShow)) {
                    ticketError.setText("Number of tickets exceeded!");
                }
                else {
                    ticketError.setText("");
                }

                for (int i = 1; i <= SeatService.THEATRE_ROWS; i++) {
                    for (int j = 1; j <= SeatService.THEATRE_COLS; j++) {
                        Ticket ticket = ticketService.findSeatTicketForShow(selectedShow, i, j);
                        tickets[i][j] = ticket;

                        Label ticketLabel = new Label(ticket.toString());
                        ticketLabels[i][j] = ticketLabel;
                        if (ticket.isReserved()) {
                            ticketLabel.setStyle("-fx-background-color: #fe5858;");
                        }
                        else {
                            ticketLabel.setStyle("-fx-background-color: #3dc764;");
                        }
                        ticketLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {

                                rowField.setText(Integer.toString(ticket.getSeat().getRowNr()));
                                seatField.setText(Integer.toString(ticket.getSeat().getSeatNr()));

                            }
                        });
                        seatGrid.add(ticketLabel, j, i);
                    }
                }
            }
        });

        Button reserveButton = new Button("Reserve");
        reserveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (rowField.getText().length() > 0 || rowField.getText().length() > 0) {
                    int rowNr = Integer.parseInt(rowField.getText());
                    int seatNr = Integer.parseInt(seatField.getText());
                    Ticket reservedTicket = tickets[rowNr][seatNr];
                    if (reservedTicket.isReserved()) {
                        ticketError.setText("Ticket is already reserved!");
                    }
                    else {
                        reservedTicket.setReserved(true);
                        Label ticketLabel = ticketLabels[rowNr][seatNr];
                        ticketLabel.setStyle("-fx-background-color: #fe5858;");
                        ticketService.reserveTicket(reservedTicket);
                        if (selectedShow != null && ticketService.nrTicketsExceeded(selectedShow)) {
                            ticketError.setText("Number of tickets exceeded!");
                        } else {
                            ticketError.setText("");
                        }
                    }
                }
                else {
                    ticketError.setText("Please select seat!");
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
                    Ticket reservedTicket = tickets[rowNr][seatNr];
                    if (reservedTicket.isReserved()) {
                        reservedTicket.setReserved(false);
                        Label ticketLabel = ticketLabels[rowNr][seatNr];
                        ticketLabel.setStyle("-fx-background-color: #3dc764;");
                        ticketService.cancelReservation(reservedTicket);
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
                    if (reservedTicket.isReserved()) {
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

        VBox vBox = new VBox();
        vBox.getChildren().addAll(showBox, titleField, genreField, dateField,
                castField, seatGrid, rowField, seatField, ticketError, reserveButton, cancelReservation, editSeat);
        pane.setCenter(vBox);
    }

    private void editSeatScene(Ticket ticket) {
        Stage stage = new Stage();
        VBox vbox = new VBox();

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
                    SeatService seatService = new SeatServiceImpl(new SeatRepositoryMySql());
                    Seat newSeat = seatService.getByPosition(rowNr, seatNr);
                    Seat oldSeat = ticket.getSeat();

                    System.out.println("Editing seat " + ticket + " new seat: " + newSeat);
                    if (ticketService.editSeat(ticket, newSeat)) {
                        System.out.println("Successful edit for seat");
                        editError.setText("");
                        tickets[oldSeat.getRowNr()][oldSeat.getSeatNr()].setReserved(false);
                        ticketLabels[oldSeat.getRowNr()][oldSeat.getSeatNr()].setStyle("-fx-background-color: #3dc764;");
                        tickets[rowNr][seatNr].setReserved(true);
                        ticketLabels[rowNr][seatNr].setStyle("-fx-background-color: #fe5858;");
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
