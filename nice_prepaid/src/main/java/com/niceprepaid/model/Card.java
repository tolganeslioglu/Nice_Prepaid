package com.niceprepaid.model;

public class Card {
    
    private int customerID;
    private String cardNumber;
    private String cardLogo;
    private String expirationDate;
    private String cvv;


    public Card (int customerID, String cardNumber, String cardLogo, String expirationDate, String cvv){
        this.customerID = customerID;
        this.cardNumber = cardNumber;
        this.cardLogo = cardLogo;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }


    public int getCustomerID() {
        return customerID;
    }


    public String getCardNumber() {
        return cardNumber;
    }


    public String getCardLogo() {
        return cardLogo;
    }


    public String getExpirationDate() {
        return expirationDate;
    }


    public String getCvv() {
        return cvv;
    }
}
