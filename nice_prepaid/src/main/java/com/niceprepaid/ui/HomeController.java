package com.niceprepaid.ui;

import com.niceprepaid.model.Customer;
import com.niceprepaid.model.PrepaidCard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeController {
    
    
    @FXML
    private Label welcomeLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Button viewCardButton;

    private Customer customer;

    public PrepaidCard prepaidCard;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (welcomeLabel != null && customer != null) {
            welcomeLabel.setText("Welcome back, " + customer.getName());
        }
    }

    public void setPrepaidCard (PrepaidCard prepaidCard) {
        this.prepaidCard = prepaidCard;
        if (balanceLabel != null && prepaidCard != null) {
            balanceLabel.setText(String.format("%.2f₺", prepaidCard.getBalance()));
        } 
        else {
            balanceLabel.setText("----");
        }
    }

    @FXML
    public void initialize() {
        setCustomer(LoginController.customer);
        setPrepaidCard(LoginController.prepaid);
    }

    @FXML
    private void handleViewCard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PrepaidCard.fxml"));
            Scene scene = new Scene(loader.load());
            CardDetailsController controller = loader.getController();

            Stage stage = (Stage) viewCardButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
