package com.niceprepaid.model;

public class CreditCard extends Card {
    
    private double creditLimit;
    private double availableCredit;
    private double currentDebt;
    private double minimumPayment;
    private String dueDate;
    private String type = "Credit";

    public CreditCard(int customerID, String cardNumber, String cardLogo, String expirationDate, String cvv,
            double creditLimit, double availableCredit, double currentDebt, double minimumPayment, String dueDate) {
        super(customerID, cardNumber, cardLogo, expirationDate, cvv);
        this.creditLimit = creditLimit;
        this.availableCredit = availableCredit;
        this.currentDebt = currentDebt;
        this.minimumPayment = minimumPayment;
        this.dueDate = dueDate;
    }
    
    public double getCreditLimit() {
        return creditLimit;
    }
    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }
    public double getAvailableCredit() {
        return availableCredit;
    }
    public void setAvailableCredit(double availableCredit) {
        this.availableCredit = availableCredit;
    }
    public double getCurrentDebt() {
        return currentDebt;
    }
    public void setCurrentDebt(double currentDebt) {
        this.currentDebt = currentDebt;
    }
    public double getMinimumPayment() {
        return minimumPayment;
    }
    public void setMinimumPayment(double minimumPayment) {
        this.minimumPayment = minimumPayment;
    }
    public String getDueDate() {
        return dueDate;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    

}
