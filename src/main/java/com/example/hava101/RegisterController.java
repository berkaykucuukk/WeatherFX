package com.example.hava101;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField prefCountryField;
    @FXML private Label registerMessage;

    @FXML
    private void handleRegister() {
        // I'm getting the username, password and prefCountry from the text fields.
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String prefCountry = prefCountryField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            registerMessage.setText("Fill the fields!");
            return;
        }

        if (password.length() < 4) {
            registerMessage.setText("Password must be at least 4 characters!");
            return;
        }

        try {
            if (UserService.userExists(username)) {
                registerMessage.setText("This username is already taken!");
                return;
            }

            UserService.saveUser(username, password,prefCountry);
            registerMessage.setText("Registration successful! You can login now.");

            // Clear the fields
            usernameField.setText("");
            passwordField.setText("");
            prefCountryField.setText("");
            
        } catch (IOException e) {
            registerMessage.setText("Error during registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToLogin() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("WeatherFX - Login");
    }
}