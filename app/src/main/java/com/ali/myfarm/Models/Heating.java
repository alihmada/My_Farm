package com.ali.myfarm.Models;

public class Heating extends Service {
    private double number;
    private Type type;

    public Heating() {
    }

    public Heating(Type type, double numberOfGasCylindersOrDieselLiters, String date, double price) {
        super(date, price);
        this.type = type;
        this.number = numberOfGasCylindersOrDieselLiters;
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
        DIESEL, GAS
    }
}
