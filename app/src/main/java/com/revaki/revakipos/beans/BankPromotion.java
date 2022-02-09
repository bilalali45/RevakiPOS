package com.revaki.revakipos.beans;

import java.util.Date;

public class BankPromotion {
    private String BankDetailId;
    private String BankName;
    private int DiscountPercentage;
    private int DiscountAmount;
    private Date StartDate;
    private Date EndDate;


    public BankPromotion() {

    }

    public BankPromotion(String BankDetailId, String BankName, int DiscountPercentage, int DiscountAmount, Date StartDate, Date EndDate) {
        this.BankDetailId = BankDetailId;
        this.BankName = BankName;
        this.DiscountPercentage = DiscountPercentage;
        this.DiscountAmount = DiscountAmount;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
    }

    public String getBankDetailId() {
        return BankDetailId;
    }

    public String getBankName() {
        return BankName;
    }

    public int getDiscountPercentage() {
        return DiscountPercentage;
    }

    public int getDiscountAmount() {
        return DiscountAmount;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public Date getEndDate() {
        return EndDate;
    }


    public void setBankDetailId(String BankDetailId) {
        this.BankDetailId = BankDetailId;
    }

    public void setBankName(String BankName) {
        this.BankName = BankName;
    }

    public void setDiscountPercentage(int DiscountPercentage) {
        this.DiscountPercentage = DiscountPercentage;
    }

    public void setDiscountAmount(int DiscountAmount) {
        this.DiscountAmount = DiscountAmount;
    }

    public void setStartDate(Date StartDate) {
        this.StartDate = StartDate;
    }

    public void setEndDate(Date EndDate) {
        this.EndDate = EndDate;
    }
}