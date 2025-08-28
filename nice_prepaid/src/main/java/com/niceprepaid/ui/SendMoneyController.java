package com.niceprepaid.ui;

import java.io.IOException;

import com.niceprepaid.model.PrepaidCard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SendMoneyController {
        
    @FXML
    private Label cardNumberField;

    @FXML
    private Label balanceLabel;

    @FXML
    private TextField amountField;

    @FXML
    private TextField recipientField;

    @FXML
    private Button sendButton;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        String cardNum = LoginController.prepaid.getCardNumber();
        String formattedCardNumber = cardNum.replaceAll("(.{4})", "$1 ").trim();
        cardNumberField.setText(formattedCardNumber);
        balanceLabel.setText(String.format("%.2f₺", LoginController.prepaid.getBalance()));
    }

    @FXML
    private void handleSendButton(){
        try {
            
            double amount = Double.parseDouble(amountField.getText().trim());

            if (amountField.getText().trim().isEmpty() || amount == 0.00) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid amount!");
            alert.showAndWait();
            return;
            }

            if (amount > LoginController.prepaid.getBalance()) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                alert.setTitle("Insufficient Balance");
                alert.setHeaderText(null);
                alert.setContentText("Your card balance is insufficient for the amount you are trying to send!");
                alert.showAndWait();
            } 
            
            else {
                String recipientCardNumber = recipientField.getText().trim();
                if (recipientCardNumber.isEmpty() || recipientCardNumber.length() != 16 || !recipientCardNumber.matches("\\d{16}")) {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter a valid 16-digit recipient card number!");
                    alert.showAndWait();
                    return;
                }

                // Update recipient's balance using helper and add transaction for recipient
                com.fasterxml.jackson.databind.node.ObjectNode recipientCard = SaveChangesToJson.getPrepaidCardByNumber(recipientCardNumber);
                if (recipientCard != null) {
                    LoginController.prepaid.setBalance(LoginController.prepaid.getBalance() - amount);
                    SaveChangesToJson.addTransactionRecord(LoginController.customer.getCustomerID(), amount, "MONEY SEND");
                    SaveChangesToJson.savePrepaidCard(LoginController.prepaid);

                    double recipientCurrentBalance = recipientCard.get("balance").asDouble();
                    recipientCard.put("balance", recipientCurrentBalance + amount);
                    // Persist recipient changes via model-save
                    PrepaidCard recipientModel = new PrepaidCard(
                            recipientCard.get("customerID").asInt(),
                            recipientCard.get("cardNumber").asText(),
                            recipientCard.get("cardLogo").asText(),
                            recipientCard.get("expirationDate").asText(),
                            recipientCard.get("cvv").asText(),
                            recipientCard.get("balance").asDouble()
                    );
                    SaveChangesToJson.savePrepaidCard(recipientModel);
                    // Add transaction for recipient
                    int recipientID = recipientCard.get("customerID").asInt();
                    SaveChangesToJson.addTransactionRecord(recipientID, amount, "MONEY RECEIVED");
                } 
                else {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                    alert.setTitle("Recipient Not Found");
                    alert.setHeaderText(null);
                    alert.setContentText("Recipient card number not found!");
                    alert.showAndWait();
                    return;
                }

                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Money Sent Successfully");
                alert.setHeaderText(null);
                alert.setContentText(String.format("%.2f₺ amount has been sent to the recipient.", amount));
                alert.showAndWait();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
                    Scene scene = new Scene(loader.load());
                    Stage stage = (Stage) sendButton.getScene().getWindow();
                    stage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } 
        
        catch (NumberFormatException e) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a numeric value!");
            alert.showAndWait();
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
}
