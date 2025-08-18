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
import com.niceprepaid.model.Customer;
import java.util.Date;

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

    private Customer loggedInCustomer;

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

    private Customer validateCredentials(String tckn, String passcode) {
        if (credentials == null) return null;

        JsonNode customers = credentials.get("customers");
        if (customers == null || !customers.isArray()) return null;

        int passcodeInt;
        try {
            passcodeInt = Integer.parseInt(passcode);
        } catch (NumberFormatException e) {
            return null;
        }

        for (JsonNode customerNode : customers) {
            // Null-safe access for TCKN
            String jsonTckn = customerNode.hasNonNull("tckn") ? customerNode.get("tckn").asText() : null;

            // Check passcode exists and is a valid integer
            if (!customerNode.hasNonNull("passcode")) continue;
            String jsonPasscodeStr = customerNode.get("passcode").asText();
            int jsonPasscodeInt;
            try {
                jsonPasscodeInt = Integer.parseInt(jsonPasscodeStr);
            } catch (NumberFormatException e) {
                continue;
            }

            if (tckn != null && tckn.equals(jsonTckn) && passcodeInt == jsonPasscodeInt) {
                int customerID = customerNode.hasNonNull("customerID") ? customerNode.get("customerID").asInt() : 0;
                String name = customerNode.hasNonNull("name") ? customerNode.get("name").asText() : "";
                String surname = customerNode.hasNonNull("surname") ? customerNode.get("surname").asText() : "";
                String email = customerNode.hasNonNull("email") ? customerNode.get("email").asText() : "";
                String phoneNumber = customerNode.hasNonNull("phoneNumber") ? customerNode.get("phoneNumber").asText() : "";
                // Use "birthDate" from JSON, handle both timestamp and string
                Date dateOfBirth = null;
                if (customerNode.has("birthDate") && !customerNode.get("birthDate").isNull()) {
                    JsonNode birthDateNode = customerNode.get("birthDate");
                    if (birthDateNode.isNumber()) {
                        dateOfBirth = new Date(birthDateNode.asLong());
                    } else if (birthDateNode.isTextual()) {
                        String dobString = birthDateNode.asText();
                        try {
                            dateOfBirth = new Date(Long.parseLong(dobString));
                        } catch (NumberFormatException e) {
                            try {
                                dateOfBirth = new Date(dobString);
                            } catch (Exception ex) {
                                dateOfBirth = null;
                            }
                        }
                    }
                }
                return new Customer(tckn, customerID, name, surname, email, phoneNumber, dateOfBirth, jsonPasscodeInt);
            }
        }
        return null;
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        if (usernameField != null && passwordField != null) {
            String tckn = usernameField.getText();
            String passcode = passwordField.getText();

            Customer customer = validateCredentials(tckn, passcode);
            if (customer != null) {
                loggedInCustomer = customer;
                errorLabel.setText("Giriş başarılı!");
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
                    Parent homeView = loader.load();

                    HomeController homeController = loader.getController();
                    homeController.setCustomer(loggedInCustomer);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(homeView);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    errorLabel.setText("Error loading home page");
                    System.err.println("Error loading Home.fxml: " + e.getMessage());
                }
            } else {
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