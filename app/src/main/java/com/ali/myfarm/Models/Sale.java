package com.ali.myfarm.Models;

public class Sale {
    private int sale;
    private int remaining;
    private String dateTime;

    public Sale() {
    }

    public Sale(String dateTime, int sale, int remaining) {
        this.sale = sale;
        this.remaining = remaining;
        this.dateTime = dateTime;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
