package com.niceprepaid.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.niceprepaid.model.CreditCard;
import com.niceprepaid.model.Customer;
import com.niceprepaid.model.PrepaidCard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
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
    private Button transactionsButton;
    
    @FXML
    private Button topUpButton;

    @FXML
    private Button viewCardButton;

    private Customer customer;

    private PrepaidCard prepaidCard;

    static CreditCard creditCard;

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

    @FXML
    private void handleRecentTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Transactions.fxml"));
            Scene scene = new Scene(loader.load());
            TransactionsController controller = loader.getController();
            controller.setCustomerID(customer.getCustomerID());

            Stage stage = (Stage) transactionsButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCreditCardButton() {
        try {
            boolean hasCreditCard = checkCustomerCreditCard(customer.getCustomerID());

            FXMLLoader loader;
            if (hasCreditCard) {
                loader = new FXMLLoader(getClass().getResource("/fxml/CreditCard.fxml"));

            } 
            
            else {
                loader = new FXMLLoader(getClass().getResource("/fxml/ApplyForCreditCard.fxml"));
            }

            Parent view = loader.load();
            Scene scene = new Scene(view);

            Stage stage = (Stage) topUpButton.getScene().getWindow();
            stage.setScene(scene);

        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTopUpButton() {
        try {
            boolean hasCreditCard = checkCustomerCreditCard(customer.getCustomerID());

            FXMLLoader loader;
            if (hasCreditCard) {
                loader = new FXMLLoader(getClass().getResource("/fxml/TopUp.fxml"));
            } 
            
            else {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("No Card Found to Top-Up From");
                alert.setHeaderText(null);
                alert.setContentText("You need to have a credit card to top-up your prepaid card.");

                alert.showAndWait();

                loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) topUpButton.getScene().getWindow();
                stage.setScene(scene);
            }

            Parent view = loader.load();
            Scene scene = new Scene(view);

            Stage stage = (Stage) topUpButton.getScene().getWindow();
            stage.setScene(scene);

        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkCustomerCreditCard(int customerID) {
    try {
        Path path = Paths.get("creditcards.json");
        String json = Files.readString(path);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        JsonNode cards = root.get("creditCards");
        if (cards != null && cards.isArray()) {
            for (JsonNode node : cards) {
                int cid = node.get("customerID").asInt();
                if (cid == customerID) {
                    creditCard = new CreditCard(
                        cid,
                        node.get("cardNumber").asText(),
                        node.get("cardLogo").asText(),
                        node.get("expirationDate").asText(),
                        node.get("cvv").asText(),
                        node.get("creditLimit").asDouble(),
                        node.get("availableCredit").asDouble(),
                        node.get("currentDebt").asDouble(),
                        node.get("minimumPayment").asDouble(),
                        node.get("dueDate").asText()
                    );
                    return true; // card found
                }
            }
        }
    } 
    catch (IOException e) {
        e.printStackTrace();
        }
        return false; // no card found  
    }

}
