package com.example.wladek.wira.pojo;

import android.graphics.Bitmap;

import java.io.Serializable;


/**
 * Created by wladek on 8/9/16.
 */
public class Item implements Serializable {
    private String claimTitle;
    private String claimCenter;
    private String claimDate;
    private String claimAmount;
    private String imagePath;


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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
