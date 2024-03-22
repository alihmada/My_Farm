package com.ali.myfarm.Models;

public class Medicine extends Service {
    private String packageName;

    public Medicine() {
    }

    public Medicine(String date, double packagePrice, String packageName) {
        super(date, packagePrice);
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
