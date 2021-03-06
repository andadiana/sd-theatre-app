package presentation;

import business.model.User;
import business.service.CashierService;
import business.service.ServiceProviderImpl;
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

public class CashierPane {

    private BorderPane pane;
    private CashierService cashierService;

    //cashier table
    private TableView<User> cashierTable;
    private TableColumn<User, String> usernameCol;
    private TableColumn<User, String> passwordCol;

    //cashier CRUD fields
    private TextField usernameField;
    private TextField passwordField;
    private Label cashierError;

    public CashierPane() {
        pane = new BorderPane();
        pane.setPadding(new Insets(10, 20, 10, 20));

        ServiceProviderImpl serviceProvider = new ServiceProviderImpl();
        cashierService = serviceProvider.getCashierService();

        createCashierTable();

        cashierError = new Label();
        Button addCashier = new Button("Add Cashier");
        addCashier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addCashierScene();
            }
        });

        Button updateCashier = new Button("Update");
        updateCashier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                User selectedCashier = cashierTable.getSelectionModel().getSelectedItem();
                if (selectedCashier != null) {
                    updateCashierScene(selectedCashier);
                }
                else {
                    cashierError.setText("Please select cashier from table!");
                }
            }
        });

        Button deleteCashier = new Button("Delete");
        deleteCashier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (cashierTable.getSelectionModel().getSelectedItem() != null) {
                    User cashier = cashierTable.getSelectionModel().getSelectedItem();

                    System.out.println("Deleting " + cashier);
                    if (cashierService.deleteCashier(cashier)) {
                        System.out.println("Successful delete for cashier");
                        populateCashierTable();
                        cashierError.setText("");
                        usernameField.setText("");
                        passwordField.setText("");
                    } else {
                        cashierError.setText("Unsuccessful delete operation");
                    }

                }
                else {
                    cashierError.setText("Please select cashier from table!");
                }
            }
        });

        usernameField = new TextField();
        usernameField.setFocusTraversable(false);

        passwordField = new TextField();
        passwordField.setFocusTraversable(false);

        cashierTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                User cashier = cashierTable.getSelectionModel().getSelectedItem();
                if (cashier != null) {
                    usernameField.setText(cashier.getUsername());
                    passwordField.setText(cashier.getPassword());
                }
            }
        });

        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.getChildren().addAll(addCashier, usernameField, passwordField, cashierError, updateCashier, deleteCashier);

        pane.setRight(vBox);

    }

    public BorderPane getPane() {
        return pane;
    }

    private void createCashierTable() {
        cashierTable = new TableView<>();
        usernameCol = new TableColumn<>("Username");
        passwordCol = new TableColumn<>("Password");

        populateCashierTable();

        cashierTable.getColumns().addAll(usernameCol, passwordCol);
        pane.setCenter(cashierTable);

    }

    private void populateCashierTable() {
        ObservableList<User> cashiers = FXCollections.observableArrayList(cashierService.findallCashiers());
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        cashierTable.setItems(cashiers);
    }

    private boolean validCashierInput(String username, String password, Label errorLabel) {
        if (username.length() == 0) {
            errorLabel.setText("Username field cannot be empty!");
            return false;
        }
        if (password.length() <= 0) {
            errorLabel.setText("Password field cannot be empty!");
            return false;
        }
        return true;
    }

    private void addCashierScene() {
        Stage stage = new Stage();
        VBox vbox = new VBox();

        TextField addUsername = new TextField();
        addUsername.setPromptText("Username");
        addUsername.setFocusTraversable(false);

        TextField addPassword = new TextField();
        addPassword.setPromptText("Password");
        addPassword.setFocusTraversable(false);

        Label addError = new Label();

        Button add = new Button("Add");
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (validCashierInput(addUsername.getText(), addPassword.getText(), addError)) {
                    User cashier = new User();
                    cashier.setUsername(addUsername.getText());
                    cashier.setPassword(addPassword.getText());
                    cashier.setUserType(User.UserType.Cashier);
                    System.out.println("Inserting " + cashier);
                    int res = cashierService.createCashier(cashier);
                    if (res != 0) {
                        System.out.println("Successful create for cashier");
                        populateCashierTable();
                        addError.setText("");
                        stage.close();
                    }
                    else {
                        addError.setText("Unsuccessful add operation");
                    }
                }
            }
        });

        vbox.setSpacing(6);
        vbox.setPadding(new Insets(10, 20, 10, 20));
        vbox.getChildren().addAll(addUsername, addPassword, addError, add);
        stage.setScene(new Scene(vbox, 400, 200));
        stage.setTitle("Add new cashier");
        stage.show();
    }

    private void updateCashierScene(User selectedCashier) {
        Stage stage = new Stage();
        VBox vbox = new VBox();

        TextField updateUsername = new TextField();
        updateUsername.setText(selectedCashier.getUsername());
        updateUsername.setFocusTraversable(false);

        TextField updatePassword = new TextField();
        updatePassword.setText(selectedCashier.getPassword());
        updatePassword.setFocusTraversable(false);

        Label updateError = new Label();

        Button update = new Button("Update");
        update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (validCashierInput(updateUsername.getText(), updatePassword.getText(), updateError)) {
                    boolean success = false;
                    if (selectedCashier.getPassword().equals(updatePassword.getText())) {
                        //password unchanged
                        selectedCashier.setUsername(updateUsername.getText());
                        success = cashierService.updateUsername(selectedCashier);
                        System.out.println("Updating username " + selectedCashier);
                    }
                    else {
                        //password updated
                        selectedCashier.setUsername(updateUsername.getText());
                        selectedCashier.setPassword(updatePassword.getText());
                        success = cashierService.updatePassword(selectedCashier);
                        System.out.println("success?: " + success);
                        System.out.println("Updating password too " + selectedCashier);
                    }
                    if (success) {
                        System.out.println("Successful update for cashier");
                        populateCashierTable();
                        updateError.setText("");
                        stage.close();
                    }
                    else {
                        updateError.setText("Unsuccessful update operation");
                    }
                }
            }
        });

        vbox.setSpacing(6);
        vbox.setPadding(new Insets(10, 20, 10, 20));
        vbox.getChildren().addAll(updateUsername, updatePassword, updateError, update);
        stage.setScene(new Scene(vbox, 400, 200));
        stage.setTitle("Update cashier");
        stage.show();
    }
}
