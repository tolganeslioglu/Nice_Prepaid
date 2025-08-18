package com.niceprepaid.ui;

import com.niceprepaid.model.Customer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeController {
    
    
    @FXML
    private Label welcomeLabel;

    @FXML
    private Label balanceLabel;

    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (welcomeLabel != null && customer != null) {
            welcomeLabel.setText("Welcome back, " + customer.getName());
        }
    }

    @FXML
    public void initialize() {
        if (customer != null) {
            welcomeLabel.setText("Welcome back, " + customer.getName());
        } else {
            welcomeLabel.setText("Welcome back");
        }
        balanceLabel.setText("₺0.00");
    }
}
