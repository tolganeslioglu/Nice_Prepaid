package com.niceprepaid.model;

public class Customer {
    
    private int tckn;
    private int customerID;
    private String name;
    private String surname;
    private String phoneNumber;
    private int passcode;


    public Customer (int CustomerID, int passcode, int tckn){
        this.customerID = customerID;
        this.tckn = tckn;
        this.passcode = passcode;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }
}
