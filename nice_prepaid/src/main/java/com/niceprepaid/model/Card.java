package com.niceprepaid.model;

public class Card {
    
    private int CustomerID;
    private int cardNumber;
    private String cardLogo;
    private double expirationDate;
    private int cvv;


    public Card (int CustomerID, int cardNumber, String cardLogo, double expirationDate, int cvv){
        this.CustomerID = CustomerID;
        this.cardNumber = cardNumber;
        this.cardLogo = cardLogo;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }
}
