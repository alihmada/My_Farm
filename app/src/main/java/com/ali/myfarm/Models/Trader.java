package com.ali.myfarm.Models;

public class Trader extends Buying {
    private int totalNumberOfCages;
    private double totalWeightOfCages;
    private double totalWeightOfEmptyCages;

    public Trader() {
    }

    public Trader(String name, int totalNumberOfCages, double totalWeightOfEmptyCages, double totalWeightOfCages, int numberOfChickens, double priceOfKilogram, double weightOfChickens, String date) {
        super(date, priceOfKilogram, name, numberOfChickens, weightOfChickens);
        this.totalNumberOfCages = totalNumberOfCages;
        this.totalWeightOfCages = totalWeightOfCages;
        this.totalWeightOfEmptyCages = totalWeightOfEmptyCages;
    }

    public int getTotalNumberOfCages() {
        return totalNumberOfCages;
    }

    public void setTotalNumberOfCages(int totalNumberOfCages) {
        this.totalNumberOfCages = totalNumberOfCages;
    }

    public double getTotalWeightOfCages() {
        return totalWeightOfCages;
    }

    public void setTotalWeightOfCages(double totalWeightOfCages) {
        this.totalWeightOfCages = totalWeightOfCages;
    }

    public double getTotalWeightOfEmptyCages() {
        return totalWeightOfEmptyCages;
    }

    public void setTotalWeightOfEmptyCages(int totalWeightOfEmptyCages) {
        this.totalWeightOfEmptyCages = totalWeightOfEmptyCages;
    }
}
