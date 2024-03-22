package com.ali.myfarm.Classes;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Calculation {
    private static final int NUMBER_OF_BAGS_ON_TON = 20;

    public static double evaluateExpression(String expression) {
        Expression exp = new ExpressionBuilder(expression).build();
        return exp.evaluate();
    }

    public static double getBagsPrice(int number, double priceOfTon) {
        return (number * priceOfTon) / NUMBER_OF_BAGS_ON_TON;
    }

    public static double getHeatingPrice(double number, double price) {
        return number * price;
    }
}
