package com.ali.myfarm.Models;

public class Electricity extends Service {
    private double remainingOnTheCounter;

    public Electricity() {
    }

    public Electricity(String date, double price, double remainingOnTheCounter) {
        super(date, price);
        this.remainingOnTheCounter = remainingOnTheCounter;
    }

    public double getRemainingOnTheCounter() {
        return remainingOnTheCounter;
    }

    public void setRemainingOnTheCounter(double remainingOnTheCounter) {
        this.remainingOnTheCounter = remainingOnTheCounter;
    }
}
