package com.ali.myfarm.Models;

public class Trader extends Buying {
    private int totalNumberOfCages;
    private double totalWeightOfCages;
    private int totalWeightOfEmptyCages;

    public Trader() {
    }

    public Trader(String date, double priceOfKilogram, int numberOfChickens, double weightOfChickens, int totalNumberOfCages, double totalWeightOfCages, int totalWeightOfEmptyCages) {
        super(date, priceOfKilogram, numberOfChickens, weightOfChickens);
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

    public int getTotalWeightOfEmptyCages() {
        return totalWeightOfEmptyCages;
    }

    public void setTotalWeightOfEmptyCages(int totalWeightOfEmptyCages) {
        this.totalWeightOfEmptyCages = totalWeightOfEmptyCages;
    }
}
