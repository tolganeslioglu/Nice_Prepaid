package com.niceprepaid.ui;

import com.niceprepaid.model.CreditCard;
import com.niceprepaid.model.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class CreditDetailsController {
    
    @FXML
    private Label cardNumberField;

    @FXML
    private Label expirationField;

    @FXML
    private Label cvvField;

    @FXML
    private Label nameField;

    @FXML
    private Label creditLimitLabel;

    @FXML
    private Label availableCreditLabel;
        
    @FXML
    private Label currentDebtLabel;

    @FXML
    private Label minimumPaymentLabel;

    @FXML
    private Label dueDateLabel;
    
    @FXML
    private ImageView cardLogoImage;

    @FXML
    private Button backButton;

    private Customer customer;

    private CreditCard creditCard;

        public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
        if (creditCard != null) {
            // Format card number in groups of 4
            String cardNum = creditCard.getCardNumber();
            String formattedCardNumber = cardNum.replaceAll("(.{4})", "$1 ").trim();
            cardNumberField.setText(formattedCardNumber);
            expirationField.setText("EXP: " + (creditCard.getExpirationDate()));
            cvvField.setText("CVV: " + creditCard.getCvv());
            creditLimitLabel.setText("Credit Limit: " + String.format("%.2f₺", creditCard.getCreditLimit()));
            availableCreditLabel.setText(String.format("Available Credit: " + "%.2f₺", creditCard.getAvailableCredit()));
            currentDebtLabel.setText(String.format("Current Debt: " + "%.2f₺", creditCard.getCurrentDebt()));
            minimumPaymentLabel.setText(String.format("Minimum Payment Amouth: " + "%.2f₺", creditCard.getMinimumPayment()));
            dueDateLabel.setText("Payement Due Date: " + creditCard.getDueDate());


            if (creditCard.getCardLogo().equalsIgnoreCase("VISA")) {
                cardLogoImage.setImage(new Image(getClass().getResourceAsStream("/images/visa.png")));
            } 
            else if (creditCard.getCardLogo().equalsIgnoreCase("MASTERCARD")) {
                cardLogoImage.setImage(new Image(getClass().getResourceAsStream("/images/mastercard.png")));
            }
        }
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (nameField != null) {
            nameField.setText(customer.getName() + " " + customer.getSurname());
        }
    }

    @FXML
    public void initialize() {
        setCustomer(LoginController.customer);
        setCreditCard(HomeController.creditCard);
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
