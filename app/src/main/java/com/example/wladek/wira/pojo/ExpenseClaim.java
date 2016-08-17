package com.example.wladek.wira.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wladek on 8/16/16.
 */
public class ExpenseClaim implements Serializable{
    private Long id;
    private String title;
    private String description;
    private ArrayList<ExpenseItem> expenses;
    private Double totalAmount;

    public ExpenseClaim(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<ExpenseItem> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<ExpenseItem> expenses) {
        this.expenses = expenses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
