package com.ali.myfarm.Models;

public class Farm {
    private String root;
    private String name;

    public Farm() {
    }

    public Farm(String root, String name) {
        this.root = root;
        this.name = name;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
