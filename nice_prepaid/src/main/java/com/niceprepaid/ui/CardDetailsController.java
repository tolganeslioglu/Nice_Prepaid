package com.niceprepaid.ui;

import com.niceprepaid.model.Customer;
import com.niceprepaid.model.PrepaidCard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CardDetailsController {

    private PrepaidCard prepaidCard;
    private Customer customer;
    
    @FXML
    private Label cardNumberField;

    @FXML
    private Label expirationField;

    @FXML
    private Label cvvField;

    @FXML
    private Label nameField;

    public void setPrepaidCard(PrepaidCard prepaidCard) {
        this.prepaidCard = prepaidCard;
        if (prepaidCard != null) {
            cardNumberField.setText(prepaidCard.getCardNumber());
            expirationField.setText((prepaidCard.getExpirationDate()));
            cvvField.setText(prepaidCard.getCvv());
        }
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (nameField != null) {
            nameField.setText(customer.getName() + " " + customer.getSurname());
        }
    }
}