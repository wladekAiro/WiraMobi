package com.example.wladek.wira.pojo;

import java.io.Serializable;


/**
 * Created by wladek on 8/9/16.
 */
public class ExpenseItem implements Serializable {
    private Long id;
    private String expenseName;
    private String expenseDate;
    private Double expenseAmount;
    private String imagePath;


    public ExpenseItem(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Double getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(Double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
