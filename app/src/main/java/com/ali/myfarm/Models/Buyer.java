package com.ali.myfarm.Models;

public class Buyer extends Buying {
    public Buyer() {
    }

    public Buyer(String date, double priceOFKilogram, int numberOfChickens, double weightOfChickens) {
        super(date, priceOFKilogram, numberOfChickens, weightOfChickens);
    }
}
