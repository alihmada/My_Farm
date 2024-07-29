package com.ali.myfarm.Models;

public class Electricity extends Service {
    private double number;
    private Type type;

    public Electricity() {
    }

    public Electricity(String date, double price, Type type, double numberOfReceiptOrPetrolLiters) {
        super(date, price);
        this.type = type;
        this.number = numberOfReceiptOrPetrolLiters;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public enum Type {
        PETROL, RECEIPT
    }
}
