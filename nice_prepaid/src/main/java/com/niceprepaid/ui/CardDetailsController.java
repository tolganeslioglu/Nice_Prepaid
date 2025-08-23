package com.niceprepaid.ui;

import com.niceprepaid.model.Customer;
import com.niceprepaid.model.PrepaidCard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class CardDetailsController {
    
    @FXML
    private Label cardNumberField;

    @FXML
    private Label expirationField;

    @FXML
    private Label cvvField;

    @FXML
    private Label nameField;

    @FXML
    private Label balanceLabel;
    
    @FXML
    private ImageView cardLogoImage;

    @FXML
    private Button backButton;

    private Customer customer;

    public PrepaidCard prepaidCard;

    public void setPrepaidCard(PrepaidCard prepaidCard) {
        this.prepaidCard = prepaidCard;
        if (prepaidCard != null) {
            // Format card number in groups of 4
            String cardNum = prepaidCard.getCardNumber();
            String formattedCardNumber = cardNum.replaceAll("(.{4})", "$1 ").trim();
            cardNumberField.setText(formattedCardNumber);
            expirationField.setText("EXP: " + (prepaidCard.getExpirationDate()));
            cvvField.setText("CVV: " + prepaidCard.getCvv());
            balanceLabel.setText(String.format("%.2f₺", prepaidCard.getBalance()));

            if (prepaidCard.getCardLogo().equalsIgnoreCase("VISA")) {
                cardLogoImage.setImage(new Image(getClass().getResourceAsStream("/images/visa.png")));
            } 
            else if (prepaidCard.getCardLogo().equalsIgnoreCase("MASTERCARD")) {
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
        setPrepaidCard(LoginController.prepaid);
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