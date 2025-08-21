package com.niceprepaid.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class CustomerCreationController {

    @FXML
    private TextField tcknField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker dateOfBirthField;

    @FXML
    private PasswordField passcodeField1;

    @FXML
    private PasswordField passcodeField2;

    @FXML
    private Button signUpField;

    @FXML
    private Button backButton;

    @FXML
    private Label errorLabel;

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @FXML
    private void initialize() {
        errorLabel.setText("");
    }

    @FXML
    private void handleLogin() {
        if (validateInputs()) {
            try {
                saveCustomer();
                clearFields();
                errorLabel.setText("Customer created successfully!");
            } catch (IOException e) {
                errorLabel.setText("Error creating customer.");
                System.err.println("Error saving customer: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent loginView = loader.load();
            
            // Get the current stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Create new scene with login view
            Scene scene = new Scene(loginView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            errorLabel.setText("Error returning to login page");
            System.err.println("Error loading Login.fxml: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        String tckn = tcknField.getText();
        String passcode1 = passcodeField1.getText();
        String passcode2 = passcodeField2.getText();
        String email = emailField.getText();
        LocalDate birthDate = dateOfBirthField.getValue();
        
        if (tckn.length() != 11 || !tckn.matches("\\d+")) {
            errorLabel.setText("Turkish ID Number must be 11 digits.");
            return false;
        }

        if (!passcode1.equals(passcode2)) {
            errorLabel.setText("Passcodes do not match.");
            return false;
        }

        if (passcode1.length() != 6 || !passcode1.matches("\\d+")) {
            errorLabel.setText("Passcode must be 6 digits.");
            return false;
        }

        // Add birth year check
        if (birthDate != null) {
            String birthYear = String.valueOf(birthDate.getYear());
            if (passcode1.contains(birthYear) || passcode1.contains(birthYear.substring(2))) {
                errorLabel.setText("Passcode cannot contain your birth year.");
                return false;
            }
        }

        if (nameField.getText().trim().isEmpty() || surnameField.getText().trim().isEmpty()) {
            errorLabel.setText("Name and surname cannot be empty.");
            return false;
        }

        if (!phoneField.getText().matches("\\d{10}")) {
            errorLabel.setText("Phone number must be 10 digits.");
            return false;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            errorLabel.setText("Please enter a valid email address.");
            return false;
        }

        if (birthDate == null) {
            errorLabel.setText("Please select your date of birth.");
            return false;
        }

        if (birthDate.isAfter(LocalDate.now().minusYears(18))) {
            errorLabel.setText("You must be at least 18 years old.");
            return false;
        }

        return true;
    }

    private void saveCustomer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File credentialsFile = new File("credentials.json");
        
        if (!credentialsFile.exists()) {
            throw new IOException("credentials.json not found at: " + credentialsFile.getAbsolutePath());
        }

        ObjectNode rootNode = (ObjectNode) mapper.readTree(credentialsFile);
        ArrayNode customers = (ArrayNode) rootNode.get("customers");
        
        String newTckn = tcknField.getText();
        String newPhone = phoneField.getText();
        String newEmail = emailField.getText();

        // Check for duplicates
        for (int i = 0; i < customers.size(); i++) {
            ObjectNode customer = (ObjectNode) customers.get(i);
            if (customer.get("tckn").asText().equals(newTckn)) {
                throw new IOException("This Turkish ID number is already registered.");
            }
            if (customer.get("phoneNumber").asText().equals(newPhone)) {
                throw new IOException("This phone number is already registered.");
            }
            if (customer.get("email").asText().equals(newEmail)) {
                throw new IOException("This email is already registered.");
            }
        }

        // Create new customer
        ObjectNode newCustomer = mapper.createObjectNode();
        newCustomer.put("tckn", newTckn);
        newCustomer.put("passcode", passcodeField1.getText());
        newCustomer.put("customerID", generateNewCustomerId(customers));
        newCustomer.put("name", nameField.getText());
        newCustomer.put("surname", surnameField.getText());
        newCustomer.put("phoneNumber", newPhone);
        newCustomer.put("email", newEmail);
        newCustomer.put("birthDate", dateOfBirthField.getValue().toString());

        customers.add(newCustomer);
        mapper.writerWithDefaultPrettyPrinter().writeValue(credentialsFile, rootNode);
    }

    private int generateNewCustomerId(ArrayNode customers) {
        int maxId = 1000; // Starting ID
        for (int i = 0; i < customers.size(); i++) {
            int currentId = customers.get(i).get("customerID").asInt();
            if (currentId > maxId) {
                maxId = currentId;
            }
        }
        return maxId + 1;
    }

    private void clearFields() {
        tcknField.clear();
        passcodeField1.clear();
        passcodeField2.clear();
        nameField.clear();
        surnameField.clear();
        phoneField.clear();
        emailField.clear();
        dateOfBirthField.setValue(null);
        errorLabel.setText("");
    }
}