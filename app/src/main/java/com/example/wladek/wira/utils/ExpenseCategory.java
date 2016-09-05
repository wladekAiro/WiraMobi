package com.example.wladek.wira.utils;

/**
 * Created by wladek on 9/5/16.
 */
public enum ExpenseCategory {
    Fuel("Fuel/Mileage"),
    GADGET("Gadget"),
    Meals("Meals"),
    Lodging("Lodging"),
    Transportation("Transportation"),
    Other("Other");

    private final String textRep;

    ExpenseCategory(String textRep) {
        this.textRep = textRep;
    }

    @Override
    public String toString() {
        return textRep;
    }
}
