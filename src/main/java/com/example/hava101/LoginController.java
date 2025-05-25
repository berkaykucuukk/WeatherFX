package com.example.hava101;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label loginMessage;
    @FXML private Button registerButton;

    @FXML  // This is the hover effect for the register button.
    public void initialize() {
        
        registerButton.setOnMouseEntered(e -> {  // When the mouse is over the register button, the button will change color.
            registerButton.setStyle("-fx-background-color: #E8E8E8; -fx-text-fill: #4a79ff;");
        });
        
        registerButton.setOnMouseExited(e -> {   // When the mouse is not over the register button, the button will change color.
            registerButton.setStyle("-fx-background-color: #F4F7FA; -fx-text-fill: #7b7b7b;");
        });
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            loginMessage.setText("Fill the fields!");
            return;
        }

        try {
            if (UserService.validateUser(username, password)) {  // If the user is valid, the login message will be shown.
                loginMessage.setText("Login successful!");
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/deneme.fxml"));
                    Parent root = loader.load();  //
                    
                    // Weather_FXML controller'ını al ve kullanıcı adını ayarla
                    Weather_FXML weatherController = loader.getController();
                    weatherController.setCurrentUser(username);
                    
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("WeatherFX - Main Page");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                loginMessage.setText("Username or password is incorrect!");
            }
        } catch (IOException e) {
            loginMessage.setText("System error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/register.fxml"));
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("WeatherFX - Register");
    }
}