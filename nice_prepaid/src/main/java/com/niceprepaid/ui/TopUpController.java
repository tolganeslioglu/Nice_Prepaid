package com.niceprepaid.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TopUpController {
    
    @FXML
    private Label cardNumberField;

    @FXML
    private Label balanceLabel;

    @FXML
    private TextField amountField;

    @FXML
    private Label creditCardNumberLabel;

    @FXML
    private Label availableLimitLabel;

    @FXML
    private Button topUpButton;

    @FXML
    private Button backButton;
    
    @FXML
    public void initialize() {
        String cardNum = LoginController.prepaid.getCardNumber();
        String formattedCardNumber = cardNum.replaceAll("(.{4})", "$1 ").trim();
        cardNumberField.setText(formattedCardNumber);
        balanceLabel.setText(String.format("%.2f₺", LoginController.prepaid.getBalance()));
        String creditCardNum = HomeController.creditCard.getCardNumber();
        String formattedCreditCardNumber = creditCardNum.replaceAll("(.{4})", "$1 ").trim();
        creditCardNumberLabel.setText(formattedCreditCardNumber);
        availableLimitLabel.setText(String.format("%.2f₺",HomeController.creditCard.getAvailableCredit()));
    }
   
    @FXML
    private void handleTopUpButton(){
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

            if (amount > HomeController.creditCard.getAvailableCredit()) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                alert.setTitle("Insufficient Credit Card Limit");
                alert.setHeaderText(null);
                alert.setContentText("Your credit card limit is insufficient for the amount you are trying to top-up!");
                alert.showAndWait();
            } 
            
            else {
                LoginController.prepaid.setBalance(LoginController.prepaid.getBalance() + amount);

                HomeController.creditCard.setAvailableCredit(HomeController.creditCard.getAvailableCredit() - amount);
                HomeController.creditCard.setCurrentDebt(HomeController.creditCard.getCurrentDebt() + amount);

                SaveChangesToJson.addTransactionRecord(LoginController.customer.getCustomerID(), amount, "TOP-UP");

                SaveChangesToJson.saveCreditCard(HomeController.creditCard);
                SaveChangesToJson.savePrepaidCard(LoginController.prepaid);

                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Top-Up Successful");
                alert.setHeaderText(null);
                alert.setContentText(String.format("%.2f₺ amount has been added to your prepaid balance.", amount));
                alert.showAndWait();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
                    Scene scene = new Scene(loader.load());
                    Stage stage = (Stage) topUpButton.getScene().getWindow();
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
