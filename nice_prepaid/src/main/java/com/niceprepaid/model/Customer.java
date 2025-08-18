package com.niceprepaid.model;

import java.util.Date;

public class Customer {

    private String tckn;
    private int customerID;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private Date dateOfBirth;
    private int passcode;

    public Customer() {}

    // Full constructor
    public Customer(String tckn, int customerID, String name, String surname, String email,
                    String phoneNumber, Date dateOfBirth, int passcode) {
        this.tckn = tckn;
        this.customerID = customerID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.passcode = passcode;
    }

    public String getTckn() {
        return tckn;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public int getPasscode() {
        return passcode;
    }
}