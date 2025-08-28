package com.niceprepaid.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.niceprepaid.model.CreditCard;
import com.niceprepaid.model.PrepaidCard;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaveChangesToJson {

    public static void saveCreditCard(CreditCard card) {
        String fileName = "creditcards.json";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode;
        ArrayNode cardsArray;

        File file = new File(fileName);
        if (file.exists()) {
            try {
                rootNode = (ObjectNode) mapper.readTree(file);
                cardsArray = (ArrayNode) rootNode.get("creditCards");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            rootNode = mapper.createObjectNode();
            cardsArray = mapper.createArrayNode();
            rootNode.set("creditCards", cardsArray);
        }

        // Update existing card or add new one
        boolean updated = false;
        for (int i = 0; i < cardsArray.size(); i++) {
            ObjectNode node = (ObjectNode) cardsArray.get(i);
            if (node.get("customerID").asInt() == card.getCustomerID()) {
                node.put("availableCredit", card.getAvailableCredit());
                node.put("currentDebt", card.getCurrentDebt());
                node.put("minimumPayment", card.getMinimumPayment());
                updated = true;
                break;
            }
        }

        if (!updated) {
            ObjectNode newCard = mapper.createObjectNode();
            newCard.put("customerID", card.getCustomerID());
            newCard.put("cardNumber", card.getCardNumber());
            newCard.put("cardLogo", card.getCardLogo());
            newCard.put("expirationDate", card.getExpirationDate());
            newCard.put("cvv", card.getCvv());
            newCard.put("creditLimit", card.getCreditLimit());
            newCard.put("availableCredit", card.getAvailableCredit());
            newCard.put("currentDebt", card.getCurrentDebt());
            newCard.put("minimumPayment", card.getMinimumPayment());
            newCard.put("dueDate", card.getDueDate());
            cardsArray.add(newCard);
        }

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePrepaidCard(PrepaidCard card) {
        String fileName = "prepaidcards.json";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode;
        ArrayNode cardsArray;

        File file = new File(fileName);
        if (file.exists()) {
            try {
                rootNode = (ObjectNode) mapper.readTree(file);
                cardsArray = (ArrayNode) rootNode.get("cards");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            rootNode = mapper.createObjectNode();
            cardsArray = mapper.createArrayNode();
            rootNode.set("cards", cardsArray);
        }

        // Update existing card or add new one
        boolean updated = false;
        for (int i = 0; i < cardsArray.size(); i++) {
            ObjectNode node = (ObjectNode) cardsArray.get(i);
            if (node.get("customerID").asInt() == card.getCustomerID()) {
                node.put("balance", card.getBalance());
                updated = true;
                break;
            }
        }

        if (!updated) {
            ObjectNode newCard = mapper.createObjectNode();
            newCard.put("customerID", card.getCustomerID());
            newCard.put("cardNumber", card.getCardNumber());
            newCard.put("cardLogo", card.getCardLogo());
            newCard.put("expirationDate", card.getExpirationDate());
            newCard.put("cvv", card.getCvv());
            newCard.put("balance", card.getBalance());
            cardsArray.add(newCard);
        }

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addTransactionRecord(int customerID, double amount, String type) {
        String fileName = customerID + "_transactions.json";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode;
        ArrayNode transactionsArray;

        File file = new File(fileName);
        if (file.exists()) {
            try {
                rootNode = (ObjectNode) mapper.readTree(file);
                transactionsArray = (ArrayNode) rootNode.get("transactions");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            rootNode = mapper.createObjectNode();
            transactionsArray = mapper.createArrayNode();
            rootNode.set("transactions", transactionsArray);
        }

        ObjectNode transactionNode = mapper.createObjectNode();
        transactionNode.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        transactionNode.put("type", type);
        transactionNode.put("amount", amount);
        double balanceAfter = LoginController.prepaid.getBalance();
        transactionNode.put("balanceAfter", balanceAfter);

        transactionsArray.add(transactionNode);

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
