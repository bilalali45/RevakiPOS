package com.revaki.revakipos.beans;

public class CategoryDetail {
    private String CategoryId;
    private String CategoryName;
    private String Type;
    private String ImageURL;
    private String Discription = null;


    public CategoryDetail() {

    }

    public CategoryDetail(String CategoryId, String CategoryName, String Type, String ImageURL, String Discription) {
        this.CategoryId = CategoryId;
        this.CategoryName = CategoryName;
        this.Type = Type;
        this.ImageURL = ImageURL;
        this.Discription = Discription;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public String getType() {
        return Type;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public String getDiscription() {
        return Discription;
    }


    public void setCategoryId(String CategoryId) {
        this.CategoryId = CategoryId;
    }

    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public void setImageURL(String ImageURL) {
        this.ImageURL = ImageURL;
    }

    public void setDiscription(String Discription) {
        this.Discription = Discription;
    }
}