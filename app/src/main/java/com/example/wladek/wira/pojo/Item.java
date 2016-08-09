package com.example.wladek.wira.pojo;

/**
 * Created by wladek on 8/9/16.
 */
public class Item {
    private String claimTitle;
    private String claimCenter;
    private String claimDate;
    private String claimAmount;


    public Item(){

    }

    public String getClaimTitle() {
        return claimTitle;
    }

    public void setClaimTitle(String claimTitle) {
        this.claimTitle = claimTitle;
    }

    public String getClaimCenter() {
        return claimCenter;
    }

    public void setClaimCenter(String claimCenter) {
        this.claimCenter = claimCenter;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public String getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(String claimAmount) {
        this.claimAmount = claimAmount;
    }
}
