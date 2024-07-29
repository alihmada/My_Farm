package com.ali.myfarm.Models;

public class Expenses {
    private String date, reason, id;
    private double balance;

    public Expenses() {
    }

    public Expenses(String id, String date, String reason, double balance) {
        this.id = id;
        this.date = date;
        this.reason = reason;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
