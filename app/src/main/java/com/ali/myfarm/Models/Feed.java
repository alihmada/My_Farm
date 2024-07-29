package com.ali.myfarm.Models;

import com.google.gson.annotations.SerializedName;

public class Feed {
    @SerializedName("growing")
    private int growing;
    @SerializedName("priceOfGrowing")
    private double priceOfGrowing;
    @SerializedName("beginning")
    private int beginning;
    @SerializedName("priceOfBeginning")
    private double priceOfBeginning;
    @SerializedName("end")
    private int end;
    @SerializedName("priceOfEnd")
    private double priceOfEnd;

    public Feed() {
    }

    public Feed(int growing, double priceOfGrowing, int beginning, double priceOfBeginning, int end, double priceOfEnd) {
        this.growing = growing;
        this.priceOfGrowing = priceOfGrowing;
        this.beginning = beginning;
        this.priceOfBeginning = priceOfBeginning;
        this.end = end;
        this.priceOfEnd = priceOfEnd;
    }

    public int getGrowing() {
        return growing;
    }

    public void setGrowing(int growing) {
        this.growing = growing;
    }

    public double getPriceOfGrowing() {
        return priceOfGrowing;
    }

    public void setPriceOfGrowing(double priceOfGrowing) {
        this.priceOfGrowing = priceOfGrowing;
    }

    public int getBeginning() {
        return beginning;
    }

    public void setBeginning(int beginning) {
        this.beginning = beginning;
    }

    public double getPriceOfBeginning() {
        return priceOfBeginning;
    }

    public void setPriceOfBeginning(double priceOfBeginning) {
        this.priceOfBeginning = priceOfBeginning;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public double getPriceOfEnd() {
        return priceOfEnd;
    }

    public void setPriceOfEnd(double priceOfEnd) {
        this.priceOfEnd = priceOfEnd;
    }

    public int getBags() {
        return getGrowing() + getBeginning() + getEnd();
    }

    public double getPrice() {
        return getPriceOfGrowing() + getPriceOfBeginning() + getPriceOfEnd();
    }

    public enum Type {
        GROWING, BEGINNING, END
    }
}
