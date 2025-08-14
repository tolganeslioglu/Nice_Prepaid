package com.niceprepaid.model;

public class PrepaidCard extends Card{
    
    private double balance;

    public PrepaidCard (int customerID, int cardNumber, String cardLogo, double expirationDate, int cvv, double balance){
        super(customerID, cardNumber, cardLogo, expirationDate, cvv);
        this.balance = balance;
    }

}
