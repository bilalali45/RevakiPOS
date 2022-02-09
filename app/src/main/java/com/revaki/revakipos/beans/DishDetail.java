package com.revaki.revakipos.beans;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class DishDetail {

    private String DishId;
    private String DishName;
    private String CategoryId;
    private String ImageURL;
    private String BarCode;
    private int InMinut;
    private int InSec;
    private String WeightInGram;
    private double TotalPrice;
    private double PriceStartFrom;
    private boolean ApplyGST;
    private boolean ApplyDiscount;
    private String Discription = null;
    private String CategoryName;
    private List<DishVariant> Variants = new ArrayList<DishVariant>();
    private String VariantData;

    public DishDetail() {

    }

    public DishDetail(String DishId, String DishName, String CategoryId, String ImageURL, String BarCode, int InMinut, int InSec, String WeightInGram, double TotalPrice, double PriceStartFrom, boolean ApplyGST, boolean ApplyDiscount, String Discription, String VariantData) {
        this.DishId = DishId;
        this.DishName = DishName;
        this.CategoryId = CategoryId;
        this.ImageURL = ImageURL;
        this.BarCode = BarCode;
        this.InMinut = InMinut;
        this.InSec = InSec;
        this.WeightInGram = WeightInGram;
        this.TotalPrice = TotalPrice;
        this.PriceStartFrom = PriceStartFrom;
        this.ApplyGST = ApplyGST;
        this.ApplyDiscount = ApplyDiscount;
        this.Discription = Discription;
        this.VariantData = VariantData;

        loadVariantFromRawData();
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

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public int getInMinut() {
        return InMinut;
    }

    public void setInMinut(int inMinut) {
        InMinut = inMinut;
    }

    public int getInSec() {
        return InSec;
    }

    public void setInSec(int inSec) {
        InSec = inSec;
    }

    public String getWeightInGram() {
        return WeightInGram;
    }

    public void setWeightInGram(String weightInGram) {
        WeightInGram = weightInGram;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public double getPriceStartFrom() {
        return PriceStartFrom;
    }

    public void setPriceStartFrom(double priceStartFrom) {
        PriceStartFrom = priceStartFrom;
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

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public List<DishVariant> getVariants() {
        return Variants;
    }

    public void setVariants(List<DishVariant> variants) {
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


    public void loadVariantFromRawData() {
        // VariantData = new String(Base64.decode(VariantData, Base64.NO_WRAP));
        Variants = new Gson().fromJson(VariantData, new TypeToken<List<DishVariant>>() {
        }.getType());
    }

}
