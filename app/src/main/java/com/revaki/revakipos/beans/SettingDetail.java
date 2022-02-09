package com.revaki.revakipos.beans;

public class SettingDetail {

    private String SettingDetailId;
    private String DefaultPrinterId;
    private String KitchenPrinterId;
    private int KitchenPintCopy;
    private String KitchenPrinterCategories;
    private boolean ShowPreBillPreview;
    private boolean ShowPostBillPreview;
    private boolean ShowKitchenPrintPreview;
    private boolean AutoPrintAfterCheckout;
    private boolean OpenCashDrawerAfterCheckout;

    public SettingDetail() {

    }

    public SettingDetail(String settingDetailId, String defaultPrinterId, String kitchenPrinterId, int kitchenPintCopy, String kitchenPrinterCategories, boolean showPreBillPreview, boolean showPostBillPreview, boolean showKitchenPrintPreview, boolean autoPrintAfterCheckout, boolean openCashDrawerAfterCheckout) {
        SettingDetailId = settingDetailId;
        DefaultPrinterId = defaultPrinterId;
        KitchenPrinterId = kitchenPrinterId;
        KitchenPintCopy = kitchenPintCopy;
        KitchenPrinterCategories = kitchenPrinterCategories;
        ShowPreBillPreview = showPreBillPreview;
        ShowPostBillPreview = showPostBillPreview;
        ShowKitchenPrintPreview = showKitchenPrintPreview;
        AutoPrintAfterCheckout = autoPrintAfterCheckout;
        OpenCashDrawerAfterCheckout = openCashDrawerAfterCheckout;
    }

    public String getSettingDetailId() {
        return SettingDetailId;
    }

    public void setSettingDetailId(String settingDetailId) {
        SettingDetailId = settingDetailId;
    }


    public String getDefaultPrinterId() {
        return DefaultPrinterId;
    }

    public void setDefaultPrinterId(String defaultPrinterId) {
        DefaultPrinterId = defaultPrinterId;
    }

    public String getKitchenPrinterId() {
        return KitchenPrinterId;
    }

    public void setKitchenPrinterId(String kitchenPrinterId) {
        KitchenPrinterId = kitchenPrinterId;
    }

    public int getKitchenPintCopy() {
        return KitchenPintCopy;
    }

    public void setKitchenPintCopy(int kitchenPintCopy) {
        KitchenPintCopy = kitchenPintCopy;
    }

    public String getKitchenPrinterCategories() {
        return KitchenPrinterCategories;
    }

    public void setKitchenPrinterCategories(String kitchenPrinterCategories) {
        KitchenPrinterCategories = kitchenPrinterCategories;
    }

    public boolean isShowPreBillPreview() {
        return ShowPreBillPreview;
    }

    public void setShowPreBillPreview(boolean showPreBillPreview) {
        ShowPreBillPreview = showPreBillPreview;
    }

    public boolean isShowPostBillPreview() {
        return ShowPostBillPreview;
    }

    public void setShowPostBillPreview(boolean showPostBillPreview) {
        ShowPostBillPreview = showPostBillPreview;
    }

    public boolean isShowKitchenPrintPreview() {
        return ShowKitchenPrintPreview;
    }

    public void setShowKitchenPrintPreview(boolean showKitchenPrintPreview) {
        ShowKitchenPrintPreview = showKitchenPrintPreview;
    }

    public boolean isAutoPrintAfterCheckout() {
        return AutoPrintAfterCheckout;
    }

    public void setAutoPrintAfterCheckout(boolean autoPrintAfterCheckout) {
        AutoPrintAfterCheckout = autoPrintAfterCheckout;
    }

    public boolean isOpenCashDrawerAfterCheckout() {
        return OpenCashDrawerAfterCheckout;
    }

    public void setOpenCashDrawerAfterCheckout(boolean openCashDrawerAfterCheckout) {
        OpenCashDrawerAfterCheckout = openCashDrawerAfterCheckout;
    }
}