package com.niceprepaid.model;

public class PrepaidCard extends Card{
    
    private double balance;
    private String type = "Prepaid";

    public PrepaidCard (int customerID, String cardNumber, String cardLogo, String expirationDate, String cvv, double balance){
        super(customerID, cardNumber, cardLogo, expirationDate, cvv);
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

}
