package com.fluffy.universe.utils;

import com.fluffy.universe.exceptions.HttpException;
import io.javalin.http.HttpCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ValidationUtils {
    private ValidationUtils() {}

    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-z0-9]).{8,30}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidMail(String mail) {
        Pattern pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher matcher = pattern.matcher(mail);
        return mail.length() <= 255 && matcher.matches();
    }

    public static boolean isDateValid(String date) {
        Pattern dateFormat = Pattern.compile("^\\d{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$");
        if (dateFormat.matcher(date).matches()) {
            String[] parsedDate = date.split("-");
            int year = Integer.parseInt(parsedDate[0]);
            int month  = Integer.parseInt(parsedDate[1]);
            int day = Integer.parseInt(parsedDate[2]);

            if (month == 2) {
                boolean leapYear = (year % 4 == 0 && (year % 100 != 0)) || (year % 400 == 0);
                return (!leapYear || day <= 29) && (leapYear || day < 29);
            } else {
                return day <= new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}[month - 1];
            }
        } else {
            return false;
        }
    }

    public static void validateParametersPresence(Object... parameters) {
        for (Object parameter : parameters) {
            if (parameter == null) {
                throw new HttpException(HttpCode.BAD_REQUEST);
            }
        }
    }
}
