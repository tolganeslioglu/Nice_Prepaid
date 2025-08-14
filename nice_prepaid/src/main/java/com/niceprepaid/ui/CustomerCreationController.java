package com.niceprepaid.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.File;
import java.io.IOException;

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
    private PasswordField passcodeField1;

    @FXML
    private PasswordField passcodeField2;

    @FXML
    private Button signUpField;

    @FXML
    private Label errorLabel;

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

    private boolean validateInputs() {
        String tckn = tcknField.getText();
        String passcode1 = passcodeField1.getText();
        String passcode2 = passcodeField2.getText();
        
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

        if (nameField.getText().trim().isEmpty() || surnameField.getText().trim().isEmpty()) {
            errorLabel.setText("Name and surname cannot be empty.");
            return false;
        }

        if (!phoneField.getText().matches("\\d{10}")) {
            errorLabel.setText("Phone number must be 10 digits.");
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

        int maxCustomerId = 0;
        for (int i = 0; i < customers.size(); i++) {
            ObjectNode customer = (ObjectNode) customers.get(i);
            String existingTckn = customer.get("tckn").asText();
            String existingPhone = customer.get("phoneNumber").asText();
            if (existingTckn.equals(newTckn) || existingPhone.equals(newPhone)) {
                throw new IOException("Duplicate TCKN or phone number.");
            }
            int existingId = customer.get("customerID").asInt(0);
            if (existingId > maxCustomerId) {
                maxCustomerId = existingId;
            }
        }

        ObjectNode newCustomer = mapper.createObjectNode();
        newCustomer.put("tckn", newTckn);
        newCustomer.put("passcode", passcodeField1.getText());
        newCustomer.put("customerID", maxCustomerId + 1);
        newCustomer.put("name", nameField.getText());
        newCustomer.put("surname", surnameField.getText());
        newCustomer.put("phoneNumber", newPhone);

        customers.add(newCustomer);
        mapper.writerWithDefaultPrettyPrinter().writeValue(credentialsFile, rootNode);
    }

    private int generateCustomerId() {
        return 0; // no longer used
    }

    private void clearFields() {
        tcknField.clear();
        passcodeField1.clear();
        passcodeField2.clear();
        nameField.clear();
        surnameField.clear();
        phoneField.clear();
        errorLabel.setText("");
    }
}