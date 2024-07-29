package com.ali.myfarm.Models;

import java.io.Serializable;

public class Period implements Serializable {
    private String name;
    private String number;
    private String beginningDate;
    private String endDate;
    private int numberOfChicken;
    private int numberOfDead;
    private int numberOfSold;
    private int numberOfFeedBags;
    private double chickenPrice;


    public Period() {
    }

    public Period(String name, String number, String beginningDate, String endDate, int numberOfChicken, int numberOfDead, int numberOfSold, int numberOfFeedBags, double chickenPrice) {
        this.name = name;
        this.number = number;
        this.beginningDate = beginningDate;
        this.endDate = endDate;
        this.numberOfChicken = numberOfChicken;
        this.numberOfDead = numberOfDead;
        this.numberOfSold = numberOfSold;
        this.numberOfFeedBags = numberOfFeedBags;
        this.chickenPrice = chickenPrice;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getBeginningDate() {
        return beginningDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getNumberOfChicken() {
        return numberOfChicken;
    }

    public int getNumberOfDead() {
        return numberOfDead;
    }

    public void setNumberOfDead(int numberOfDead) {
        this.numberOfDead = numberOfDead;
    }

    public int getNumberOfSold() {
        return numberOfSold;
    }

    public int getNumberOfFeedBags() {
        return numberOfFeedBags;
    }

    public double getChickenPrice() {
        return chickenPrice;
    }

    public int getNumberOfAliveChickens() {
        return getNumberOfChicken() - getNumberOfDead();
    }
}
