package com.niceprepaid.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.ListCell;

public class TransactionsController {

    @FXML
    private ListView<String> transactionList;

    @FXML
    private Label noTransactionsLabel;

    @FXML
    private Button backButton;

    private int customerID;

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
        loadTransactions();
    }

    private void loadTransactions() {
        try {
            File transactionFile = new File(customerID + "_transactions.json");
            if (!transactionFile.exists()) {
                noTransactionsLabel.setVisible(true);
                transactionList.setVisible(false);
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(transactionFile);
            JsonNode transactions = rootNode.get("transactions");

            if (transactions == null || transactions.size() == 0) {
                noTransactionsLabel.setVisible(true);
                transactionList.setVisible(false);
                return;
            }

            noTransactionsLabel.setVisible(false);
            transactionList.setVisible(true);
            transactionList.getItems().clear();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            for (JsonNode transaction : transactions) {
                String date = transaction.get("date").asText();
                String type = transaction.get("type").asText();
                double amount = transaction.get("amount").asDouble();
                double balance = transaction.get("balanceAfter").asDouble();

                String transactionText = String.format("%s | %s | %s%.2f₺ | Balance: %.2f₺",
                    date,
                    type,
                    type.equals("DEPOSIT") ? "+" : "-",
                    amount,
                    balance
                );

                transactionList.getItems().add(transactionText);
            }

        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
            noTransactionsLabel.setVisible(true);
            transactionList.setVisible(false);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
            Parent homeView = loader.load();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(homeView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error returning to home: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        transactionList.setCellFactory(list -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: black; -fx-font-size: 10px;");
                }
            }
        });
    }
}