package com.example.wladek.wira.pojo;

import com.example.wladek.wira.utils.ExpenseCategory;

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
    private Long claimId;
    private ExpenseCategory category;
    private ExpenseClaim claim;


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

    public Long getClaimId() {
        return claimId;
    }

    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }

    @Override
    public String toString() {
        return expenseName + "  "+expenseDate;
    }

    @Override
    public boolean equals(Object o) {

        ExpenseItem i = (ExpenseItem) o;

        if (id.equals(i.getId())){
            return true;
        }else if (imagePath.equals(i.getImagePath())){
            return true;
        }

        return id.equals(i.getId());
    }

    public ExpenseClaim getClaim() {
        return claim;
    }

    public void setClaim(ExpenseClaim claim) {
        this.claim = claim;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }
}
