package com.niceprepaid.ui;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ApplyForCreditController {
    
    @FXML
    private Button applyButton;
    
    @FXML
    private Button backButton;

    @FXML
    private void handleApplyButton() {
        try {
            createAndSaveCreditCard(LoginController.customer.getCustomerID());

            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Application Successful");
            alert.setHeaderText(null);
            alert.setContentText("Card application successful");

            alert.showAndWait();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) applyButton.getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAndSaveCreditCard(int customerID) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File cardsFile = new File("creditcards.json");
        
        ObjectNode rootNode;
        if (!cardsFile.exists()) {
            rootNode = mapper.createObjectNode();
            rootNode.putArray("creditCards");
        } else {
            rootNode = (ObjectNode) mapper.readTree(cardsFile);
        }

        ArrayNode creditCards = (ArrayNode) rootNode.get("creditCards");
        
        // Generate random card details
        Random random = new Random();
        
        // Generate 16-digit card number
        StringBuilder cardNumber = new StringBuilder();
        cardNumber.append("4"); // First digit for VISA
        for (int i = 0; i < 15; i++) {
            cardNumber.append(random.nextInt(10));
        }
        
        // Generate 3-digit CVV
        String cvv = String.format("%03d", random.nextInt(1000));
        
        // Calculate expiration date (5 years from now)
        LocalDate expirationDate = LocalDate.now().plusYears(5);
        String formattedExpDate = expirationDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        
        // Create new card
        ObjectNode newCard = mapper.createObjectNode();
        newCard.put("customerID", customerID);
        newCard.put("cardNumber", cardNumber.toString());
        newCard.put("cardLogo", "VISA");
        newCard.put("expirationDate", formattedExpDate);
        newCard.put("cvv", cvv);
        newCard.put("creditLimit", 10000.00);
        newCard.put("availableCredit", 10000.00);
        newCard.put("currentDebt", 0.00);
        newCard.put("minimumPayment", 0.00);
        
        // Calculate due date (1 month from now)
        LocalDate dueDate = LocalDate.now().plusMonths(1);
        String formattedDueDate = dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        newCard.put("dueDate", formattedDueDate);
        
        creditCards.add(newCard);
        mapper.writerWithDefaultPrettyPrinter().writeValue(cardsFile, rootNode);
    }
}
