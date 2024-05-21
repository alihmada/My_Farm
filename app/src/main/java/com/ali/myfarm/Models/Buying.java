package com.ali.myfarm.Models;

public class Buying extends Service {
    private String name;
    private int numberOfChickens;
    private double weightOfChickens;

    public Buying() {
    }

    public Buying(String date, double price, String name, int numberOfChickens, double weightOfChickens) {
        super(date, price);
        this.name = name;
        this.numberOfChickens = numberOfChickens;
        this.weightOfChickens = weightOfChickens;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfChickens() {
        return numberOfChickens;
    }

    public void setNumberOfChickens(int numberOfChickens) {
        this.numberOfChickens = numberOfChickens;
    }

    public double getWeightOfChickens() {
        return weightOfChickens;
    }

    public void setWeightOfChickens(double weightOfChickens) {
        this.weightOfChickens = weightOfChickens;
    }
}
