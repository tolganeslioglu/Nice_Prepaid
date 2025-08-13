package com.niceprepaid.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    // Örnek kullanıcı adı ve şifre
    private final String validUsername = "admin";
    private final String validPassword = "1234";

    @FXML
    private void initialize() {
        if (errorLabel != null) {
            errorLabel.setText(""); // Başlangıçta hata yok
        }
    }

    @FXML
    private void handleLogin(javafx.event.ActionEvent event) {
        if (usernameField != null && passwordField != null) {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.equals(validUsername) && password.equals(validPassword)) {
                // Giriş başarılı
                if (errorLabel != null) {
                    errorLabel.setText("Giriş başarılı!");
                }
                // Burada sahne değiştirme veya ana uygulamayı açma kodu eklenebilir
            } else {
                // Giriş başarısız
                if (errorLabel != null) {
                    errorLabel.setText("Kullanıcı adı veya şifre hatalı.");
                }
            }
        }
    }
}