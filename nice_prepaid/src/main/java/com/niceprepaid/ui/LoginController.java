package com.niceprepaid.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import com.niceprepaid.model.Customer;
import com.niceprepaid.model.PrepaidCard;
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
    private JsonNode prepaidCards;

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

    private void loadPrepaidCards() {
        ObjectMapper mapper = new ObjectMapper();
        File prepaidcardsFile = new File("prepaidcards.json");
        try {
            if (prepaidcardsFile.exists()) {
                    credentials = mapper.readTree(prepaidcardsFile);
                } else {
                    System.err.println("Prepaid cards file not found: " + prepaidcardsFile.getAbsolutePath());
                }
            }
        catch (IOException e) {
            System.err.println("Error loading prepaid cards: " + e.getMessage());
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

    private PrepaidCard validatePrepaidCards(int customerID) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File cardsFile = new File("prepaidcards.json");
            
            if (!cardsFile.exists()) {
                System.err.println("Cards file not found: " + cardsFile.getAbsolutePath());
                return null;
            }

            prepaidCards = mapper.readTree(cardsFile);
            JsonNode cards = prepaidCards.get("cards");
            
            if (cards == null || !cards.isArray()) {
                System.err.println("Invalid cards format in JSON");
                return null;
            }

            for (JsonNode cardNode : cards) {
                if (cardNode.get("customerID").asInt() == customerID) {
                    return new PrepaidCard(
                        customerID,
                        cardNode.get("cardNumber").asText(),
                        cardNode.get("cardLogo").asText(),
                        cardNode.get("expirationDate").asText(),
                        cardNode.get("cvv").asText(),
                        cardNode.get("balance").asDouble()
                    );
                }
            }
            
            System.err.println("No card found for customer ID: " + customerID);
            return null;

        } catch (IOException e) {
            System.err.println("Error loading prepaid cards: " + e.getMessage());
            return null;
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        if (usernameField != null && passwordField != null) {
            String tckn = usernameField.getText();
            String passcode = passwordField.getText();

            Customer customer = validateCredentials(tckn, passcode);
            if (customer != null) {
                PrepaidCard prepaidCard = validatePrepaidCards(customer.getCustomerID());
                loggedInCustomer = customer;
                errorLabel.setText("Login Succesful!");
                
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
                    Parent homeView = loader.load();

                    HomeController homeController = loader.getController();
                    homeController.setCustomer(loggedInCustomer);
                    homeController.setPrepaidCard(prepaidCard);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(homeView);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    errorLabel.setText("Error loading home page");
                    System.err.println("Error loading Home.fxml: " + e.getMessage());
                }
            } else {
                errorLabel.setText("Turkish ID number or passcode is incorrect.");
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