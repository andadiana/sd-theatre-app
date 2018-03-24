package presentation;

import business.model.User.UserType;
import business.service.LoginService;
import business.service.LoginServiceImpl;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class LoginView extends Scene {

    private TextField username;
    private PasswordField password;
    private LoginService loginService;
    private UserType userType;
    private Label errorMessage;

    public LoginView(BorderPane pane) {
        super(pane, 500, 400);
        loginService = new LoginServiceImpl();

        Label title = new Label("Theatre Application");
        pane.setTop(title);

        username = new TextField();
        username.setPromptText("Enter username");
        username.setFocusTraversable(false);


        password = new PasswordField();
        password.setPromptText("Enter password");
        password.setFocusTraversable(false);

        errorMessage = new Label();

        Button loginButton = new Button("Login");

        loginButton.setOnAction((event) -> {
                try {
                    userType = loginService.logIn(username.getText(), password.getText());
                    System.out.println(userType);
                    errorMessage.setText("");
                    launchSpecificView(userType);
                } catch (Exception e) {
                    errorMessage.setText(e.getMessage());
                }
            });


        VBox vBox = new VBox();
        vBox.getChildren().addAll(username, password, errorMessage, loginButton);
        vBox.setSpacing(10);
        pane.setCenter(vBox);

    }

    private void launchSpecificView(UserType userType) {
        switch (userType){
            case Admin: {
                AdminView adminView = new AdminView(new BorderPane());
                TheatreApp.getPrimaryStage().setScene(adminView);
                break;
            }
            case Cashier: {
                CashierView cashierView = new CashierView(new BorderPane());
                TheatreApp.getPrimaryStage().setScene(cashierView);
                break;
            }
        }
    }


}
