package com.ali.myfarm.Classes;

public class Common {
    public static final String REGEX_NAME = "^[a-zA-Zء-ي]?/?\\s?+(([a-zA-Zء-ي]{3,10})(?:\\s|$)){1,6}$";
    public static final String REGEX_ERROR = "[A-Za-zء-ى0-9]+$|^(.*?)$";

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
    public static final String NUMBER_OF_DEAD = "numberOfDead";
    public static final String RUNNING_ITEMS = "havaRunningItems";
    public static final String GROWING = "growingBranch";
    public static final String INITIALIZE = "initializeBranch";
    public static final String END = "endBranch";
    private static String ROOT;

    public static String getROOT() {
        return ROOT;
    }

    public static void setROOT(String ROOT) {
        Common.ROOT = ROOT;
    }
}
