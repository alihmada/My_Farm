package com.ali.myfarm.Models;

public class Chicken {
    private int alive;
    private int dead;
    private String date;

    public Chicken() {
    }

    public Chicken(int alive, int dead, String date) {
        this.alive = alive - dead;
        this.dead = dead;
        this.date = date;
    }

    public int getAlive() {
        return alive;
    }

    public int getDead() {
        return dead;
    }

    public String getDate() {
        return date;
    }
}
