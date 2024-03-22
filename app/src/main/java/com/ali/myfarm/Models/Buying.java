package com.ali.myfarm.Models;

public class Buying extends Service {
    private int numberOfChickens;
    private double weightOfChickens;

    public Buying() {
    }

    public Buying(String date, double price, int numberOfChickens, double weightOfChickens) {
        super(date, price);
        this.numberOfChickens = numberOfChickens;
        this.weightOfChickens = weightOfChickens;
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
