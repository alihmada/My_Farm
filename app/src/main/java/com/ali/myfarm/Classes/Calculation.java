package com.ali.myfarm.Classes;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Calculation {
    private static final int NUMBER_OF_BAGS_ON_TON = 20;

    public static String evaluateExpression(String expression) {
        Expression exp = new ExpressionBuilder(expression).build();
        double result = exp.evaluate();
        if (result == (int) result) {
            return String.valueOf((int) result);
        } else {
            return String.valueOf(result);
        }
    }

    public static double getBagsPrice(int number, double priceOfTon) {
        return (number * priceOfTon) / NUMBER_OF_BAGS_ON_TON;
    }

    public static double getElectricityOrHeatingPrice(double number, double price) {
        return number * price;
    }

    public static double getChickensWeight(double weightOfFullCages, double weightOfEmptyCages) {
        return weightOfFullCages - weightOfEmptyCages;
    }

    public static String getNumber(double number) {
        if (number == (int) number) {
            return String.valueOf((int) number);
        } else {
            return String.valueOf(number);
        }
    }

    public static double getAverage(double weight, int count) {
        return weight / count;
    }

    public static double getTotalPrice(double chickensWeight, double price) {
        return chickensWeight * price;
    }

    public static String formatNumberWithCommas(double number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat formatter = new DecimalFormat("#,###.###", symbols);
        return formatter.format(number);
    }

    public static String needWater(double numberOfChickens, int nDay) {
        return formatNumberWithCommas(2 * getFeed(numberOfChickens, nDay));
    }

    public static String needFeed(double numberOfChickens, int nDay) {
        return formatNumberWithCommas(getFeed(numberOfChickens, nDay));
    }

    private static double getFeed(double numberOfChickens, int nDay) {
        return numberOfChickens / 1000 * nDay * 4.5;
    }


}
