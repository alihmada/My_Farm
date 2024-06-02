package com.ali.myfarm.Models;

public class Transaction {
    private double weightForTraders;
    private double weightForBuyers;
    private double priceForTraders;
    private double priceForBuyers;

    public Transaction() {
    }

    public Transaction(double weightForTraders, double weightForBuyers, double priceForTraders, double priceForBuyers) {
        this.weightForTraders = weightForTraders;
        this.weightForBuyers = weightForBuyers;
        this.priceForTraders = priceForTraders;
        this.priceForBuyers = priceForBuyers;
    }

    public double getWeightForTraders() {
        return weightForTraders;
    }

    public void setWeightForTraders(double weightForTraders) {
        this.weightForTraders = weightForTraders;
    }

    public double getWeightForBuyers() {
        return weightForBuyers;
    }

    public void setWeightForBuyers(double weightForBuyers) {
        this.weightForBuyers = weightForBuyers;
    }

    public double getPriceForTraders() {
        return priceForTraders;
    }

    public void setPriceForTraders(double priceForTraders) {
        this.priceForTraders = priceForTraders;
    }

    public double getPriceForBuyers() {
        return priceForBuyers;
    }

    public void setPriceForBuyers(double priceForBuyers) {
        this.priceForBuyers = priceForBuyers;
    }

    public double getTotalWeight() {
        return getWeightForTraders() + getWeightForBuyers();
    }

    public double getTotalPrice() {
        return getPriceForTraders() + getPriceForBuyers();
    }
}
