package com.ali.myfarm.Classes;

public class PhoneNumber {
    public static String formatPhoneNumber(String phoneNumber) {
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

        if (digitsOnly.startsWith("1")) {
            digitsOnly = digitsOnly.substring(1);
        }

        StringBuilder formattedNumber = new StringBuilder("+");
        for (int i = 0; i < digitsOnly.length(); i++) {
            if (i == 2 || i == 5 || i == 8) {
                formattedNumber.append(" ");
            }
            formattedNumber.append(digitsOnly.charAt(i));
        }

        return formattedNumber.toString();
    }
}
