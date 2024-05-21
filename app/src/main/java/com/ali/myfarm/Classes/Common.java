package com.ali.myfarm.Classes;

public class Common {
    public static final String PHONE_NUMBER = "phone";
    public static final String DATABASE_NAME = "Database";
    public static final String FIREBASE_USERS = "Users";
    public static final String USER_DATA = "user_data";

    public static final String FARM_ID = "farm_id";
    public static final String SHARED_PREFERENCE_NAME = "e4aE9coc2cY=";
    public static final String IMAGE = "image";
    public static final String MAIN_ID = "main id";
    public static final String PERIOD_ID = "period id";
    public static final String PERIOD = "period";
    public static final String CHICKEN = "chicken";
    public static final String FEED = "feed";
    public static final String HEATING = "heating";
    public static final String ELECTRICITY = "electricity";
    public static final String NUMBER_OF_DEAD = "numberOfDead";
    public static final String RUNNING_ITEMS = "havaRunningItems";
    public static final String GROWING = "growingBranch";
    public static final String INITIALIZE = "initializeBranch";
    public static final String END = "endBranch";
    public static final String VALUE = "value";
    public static final String REGEX = "regex";
    public static final String DECIMAL_REGEX = "\\d*\\.?\\d+$";
    public static final String TRANSACTION = "transaction";
    public static final String TRADERS = "traders";
    public static final String BUYERS = "buyers";
    public static final String PERSONS = "persons";
    public static final String MOVED_DATA = "moved";
    public static final String N_DAY = "nDay";
    public static final String IS_TRADER = "isTrader";
    public static final String SOLD = "numberOfSold";
    public static final String SALES = "sales";
    private static final int[] durationOfDarkness = {24, 23, 23, 23, 22, 22, 22, 22, 21, 21, 21, 21, 21, 20, 20, 20, 20, 20, 20, 20, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 20, 20, 20, 20, 20, 20, 20, 20, 20};
    private static final int[] temperature = {35, 33, 33, 32, 31, 30, 30, 30, 29, 29, 28, 28, 28, 27, 27, 27, 26, 26, 25, 25, 25, 24, 24, 24, 23, 23, 23, 22, 22, 22, 22, 22, 21, 21, 21, 21, 20, 20, 20, 21, 21, 20, 20, 20, 20};
    private static String ROOT;

    public static String getDurationOfDarkness(int index) {
        if (index > durationOfDarkness.length) {
            index = durationOfDarkness[durationOfDarkness.length - 1];
        }
        return String.valueOf(24 - durationOfDarkness[index]);
    }

    public static String getTemperature(int index) {
        if (index > temperature.length) {
            index = temperature[temperature.length - 1];
        }
        return String.valueOf(temperature[index]);
    }

    public static String getROOT() {
        return ROOT;
    }

    public static void setROOT(String ROOT) {
        Common.ROOT = ROOT;
    }
}
