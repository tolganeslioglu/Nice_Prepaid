package com.niceprepaid.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import java.io.InputStream;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class LoginController {

    @FXML
    private TextField usernameField;  // This will be used for TCKN

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Button becomeCustomerButton;

    private JsonNode credentials;

    @FXML
    private void initialize() {
        if (errorLabel != null) {
            errorLabel.setText("");
        }
        loadCredentials();
    }

    private void loadCredentials() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File credentialsFile = new File("credentials.json");
            if (credentialsFile.exists()) {
                credentials = mapper.readTree(credentialsFile);
            } else {
                System.err.println("Credentials file not found: " + credentialsFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error loading credentials: " + e.getMessage());
        }
    }

    private boolean validateCredentials(String tckn, String passcode) {
        if (credentials == null) return false;

        JsonNode customers = credentials.get("customers");
        for (JsonNode customer : customers) {
            if (customer.get("tckn").asText().equals(tckn) && 
                customer.get("passcode").asText().equals(passcode)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        if (usernameField != null && passwordField != null) {
            String tckn = usernameField.getText();
            String passcode = passwordField.getText();

            if (validateCredentials(tckn, passcode)) {
                // Login successful
                errorLabel.setText("Giriş başarılı!");
                // Add code here to switch to main application scene
            } else {
                // Login failed
                errorLabel.setText("TCKN veya şifre hatalı.");
            }
        }
    }

    @FXML
    private void handleBecomeCustomer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BecomeACustomer.fxml"));
            Parent becomeCustomerView = loader.load();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(becomeCustomerView);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            errorLabel.setText("Error loading customer registration page");
            System.err.println("Error loading BecomeACustomer.fxml: " + e.getMessage());
        }
    }
}