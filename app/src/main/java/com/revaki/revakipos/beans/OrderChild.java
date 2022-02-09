package com.revaki.revakipos.beans;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderChild {
    private String OrderChildId;
    private String OrderMasterId;
    private String DishId;
    private String DishName;
    private String Quantity = "0";
    private String Price = "0";
    private String Amount = "0";
    private String VariantAmount = "0";
    private String PrintQuantity = "0";
    private String SpecialInstruction = "";
    private int DiscountTypeId = 1;
    private String DiscountPercentage = "0";
    private String DiscountAmount = "0";
    private String ComplementaryQuantity = "0";
    private String ComplementaryAmount = "0";
    private String NetAmount = "0";
    private String TotalAmount = "0";
    private List<OrderChildVariant> Variants = new ArrayList<OrderChildVariant>();
    private String VariantData;
    private transient Date DatetimeStamp = null;
    private transient boolean ApplyGST;
    private transient boolean ApplyDiscount;
    private int StatusId = 0;

    public OrderChild() {

    }

    public OrderChild(String dishId, String dishName, String quantity, String price, String amount, String variantAmount, String printQuantity, String specialInstruction, int discountTypeId, String discountPercentage, String discountAmount, String complementaryQuantity, String complementaryAmount, String netAmount, String totalAmount, String VariantData, Date datetimeStamp, boolean applyGST, boolean applyDiscount, int statusId) {
        DishId = dishId;
        DishName = dishName;
        Quantity = quantity;
        Price = price;
        Amount = amount;
        VariantAmount = variantAmount;
        PrintQuantity = printQuantity;
        SpecialInstruction = specialInstruction;
        DiscountTypeId = discountTypeId;
        DiscountPercentage = discountPercentage;
        DiscountAmount = discountAmount;
        ComplementaryQuantity = complementaryQuantity;
        ComplementaryAmount = complementaryAmount;
        NetAmount = netAmount;
        TotalAmount = totalAmount;
        this.VariantData = VariantData;
        DatetimeStamp = datetimeStamp;
        ApplyGST = applyGST;
        ApplyDiscount = applyDiscount;
        StatusId = statusId;

        loadVariantFromRawData();
    }

    public String getOrderChildId() {
        return OrderChildId;
    }

    public void setOrderChildId(String orderChildId) {
        OrderChildId = orderChildId;
    }

    public String getOrderMasterId() {
        return OrderMasterId;
    }

    public void setOrderMasterId(String orderMasterId) {
        OrderMasterId = orderMasterId;
    }

    public String getDishId() {
        return DishId;
    }

    public void setDishId(String dishId) {
        DishId = dishId;
    }

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getVariantAmount() {
        return VariantAmount;
    }

    public void setVariantAmount(String variantAmount) {
        VariantAmount = variantAmount;
    }

    public String getPrintQuantity() {
        return PrintQuantity;
    }

    public void setPrintQuantity(String printQuantity) {
        PrintQuantity = printQuantity;
    }

    public String getSpecialInstruction() {
        return SpecialInstruction;
    }

    public void setSpecialInstruction(String specialInstruction) {
        SpecialInstruction = specialInstruction;
    }

    public int getDiscountTypeId() {
        return DiscountTypeId;
    }

    public void setDiscountTypeId(int discountTypeId) {
        DiscountTypeId = discountTypeId;
    }

    public String getDiscountPercentage() {
        return DiscountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        DiscountPercentage = discountPercentage;
    }

    public String getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        DiscountAmount = discountAmount;
    }

    public String getComplementaryQuantity() {
        return ComplementaryQuantity;
    }

    public void setComplementaryQuantity(String complementaryQuantity) {
        ComplementaryQuantity = complementaryQuantity;
    }

    public String getComplementaryAmount() {
        return ComplementaryAmount;
    }

    public void setComplementaryAmount(String complementaryAmount) {
        ComplementaryAmount = complementaryAmount;
    }

    public String getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(String netAmount) {
        NetAmount = netAmount;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public List<OrderChildVariant> getVariants() {
        return Variants;
    }

    public void setVariants(List<OrderChildVariant> variants) {
        Variants = variants;
        VariantData = new Gson().toJson(Variants);
    }

    public String getVariantData() {
        VariantData = new Gson().toJson(Variants);
        return VariantData;
    }

    public void setVariantData(String variantData) {
        VariantData = variantData;
    }

    public Date getDatetimeStamp() {
        return DatetimeStamp;
    }

    public void setDatetimeStamp(Date datetimeStamp) {
        DatetimeStamp = datetimeStamp;
    }

    public boolean isApplyGST() {
        return ApplyGST;
    }

    public void setApplyGST(boolean applyGST) {
        ApplyGST = applyGST;
    }

    public boolean isApplyDiscount() {
        return ApplyDiscount;
    }

    public void setApplyDiscount(boolean applyDiscount) {
        ApplyDiscount = applyDiscount;
    }

    public int getStatusId() {
        return StatusId;
    }

    public void setStatusId(int statusId) {
        StatusId = statusId;
    }

    public void loadVariantFromRawData() {
        // VariantData = new String(Base64.decode(VariantData, Base64.NO_WRAP));
        Variants = new Gson().fromJson(VariantData, new TypeToken<List<OrderChildVariant>>() {
        }.getType());
    }

    public void calculateValues() {
        double quantity = Double.valueOf("0" + Quantity);
        double price = Double.valueOf("0" + Price);
        double amount = quantity * price;
        double discountAmount = Double.valueOf("0" + DiscountAmount);
        double discountPercentage = Double.valueOf("0" + DiscountPercentage);
        double netAmount = 0;
        double variantAmount = 0;
        double totalAmount = 0;
        if (amount > 0) {
            if (DiscountTypeId == 1 && discountAmount > 0) {
                discountPercentage = (discountAmount / amount) * 100;
            } else if (DiscountTypeId == 2 && discountPercentage > 0) {
                discountAmount = (amount / 100) * discountPercentage;
            }
            discountAmount = Math.floor(discountAmount);
        } else {
            discountAmount = 0;
            discountPercentage = 0;
        }

        for (OrderChildVariant orderChildVariant : Variants) {
            for (VariantDetail variantDetail : orderChildVariant.getData()) {
                variantAmount += (quantity * variantDetail.getPrice());
            }
        }

        if (discountAmount > amount) {
            discountAmount = amount;
        }
        netAmount = amount - discountAmount;
        totalAmount = netAmount + variantAmount;

        setDiscountAmount(String.format("%.2f", discountAmount).replaceFirst("\\.?0*$", ""));
        setDiscountPercentage(String.format("%.2f", discountPercentage).replaceFirst("\\.?0*$", ""));
        setAmount(String.format("%.2f", amount).replaceFirst("\\.?0*$", ""));
        setVariantAmount(String.format("%.2f", variantAmount).replaceFirst("\\.?0*$", ""));
        setNetAmount(String.format("%.2f", netAmount).replaceFirst("\\.?0*$", ""));
        setTotalAmount(String.format("%.2f", totalAmount).replaceFirst("\\.?0*$", ""));
    }
}
