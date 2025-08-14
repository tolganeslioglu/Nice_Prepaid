package com.niceprepaid.model;

public class Card {
    
    private int customerID;
    private int cardNumber;
    private String cardLogo;
    private double expirationDate;
    private int cvv;


    public Card (int customerID, int cardNumber, String cardLogo, double expirationDate, int cvv){
        this.customerID = customerID;
        this.cardNumber = cardNumber;
        this.cardLogo = cardLogo;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }
}
