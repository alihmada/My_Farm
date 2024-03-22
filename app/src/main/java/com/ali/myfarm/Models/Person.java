package com.ali.myfarm.Models;

public class Person extends User {
    private Type type;
    private double forHim;
    private double forMe;

    public Person() {
    }

    public Person(String firstName, String lastName, String phoneNumber, Type type, double forHim, double forMe) {
        super(firstName, lastName, phoneNumber);
        this.type = type;
        this.forHim = forHim;
        this.forMe = forMe;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getForHim() {
        return forHim;
    }

    public void setForHim(double forHim) {
        this.forHim = forHim;
    }

    public double getForMe() {
        return forMe;
    }

    public void setForMe(double forMe) {
        this.forMe = forMe;
    }

    public static enum Type {
        BUYER,
        TRADER
    }
}
