package com.ali.myfarm.Models;

public class Buyer extends Buying {

    public Buyer() {
    }

    public Buyer(String date, double priceOFKilogram, String id, int numberOfChickens, double weightOfChickens) {
        super(date, priceOFKilogram, id, numberOfChickens, weightOfChickens);
    }
}
