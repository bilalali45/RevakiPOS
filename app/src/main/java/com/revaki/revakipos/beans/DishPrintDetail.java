package com.revaki.revakipos.beans;

public class DishPrintDetail {

    private String dishId;
    private String categoryId;
    private int printerIp;
    private int port;

    public DishPrintDetail(String dishId, String categoryId, int printerIp, int port) {
        this.dishId = dishId;
        this.categoryId = categoryId;
        this.printerIp = printerIp;
        this.port = port;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getPrinterIp() {
        return printerIp;
    }

    public void setPrinterIp(int printerIp) {
        this.printerIp = printerIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
