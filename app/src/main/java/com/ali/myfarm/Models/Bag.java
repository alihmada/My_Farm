package com.ali.myfarm.Models;

import com.ali.myfarm.Classes.Calculation;

public class Bag {
    private int number;
    private double priceOfTon;
    private Operation operation;
    private String dateOfModification;

    public Bag() {
    }

    public Bag(int number, double priceOfTon, Operation operation, String dateOfModification) {
        this.number = number;
        this.priceOfTon = priceOfTon;
        this.operation = operation;
        this.dateOfModification = dateOfModification;
    }

    public int getNumber() {
        return number;
    }

    public double getPriceOfTon() {
        return priceOfTon;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getDateOfModification() {
        return dateOfModification;
    }

    public double getPrice() {
        return Calculation.getBagsPrice(getNumber(), getPriceOfTon());
    }

    public enum Operation {
        ADD, REMOVE
    }
}
