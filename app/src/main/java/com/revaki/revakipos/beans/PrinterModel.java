package com.revaki.revakipos.beans;

public class PrinterModel {
    private String PrinterModelId;
    private String BrandName;
    private String ModelName;
    private String ModelValue;
    private String PrinterType;
    private String PaperSize;


    public PrinterModel(String printerModelId, String brandName, String modelName, String modelValue, String printerType, String paperSize) {
        PrinterModelId = printerModelId;
        BrandName = brandName;
        ModelName = modelName;
        ModelValue = modelValue;
        PrinterType = printerType;
        PaperSize = paperSize;
    }

    public String getPrinterModelId() {
        return PrinterModelId;
    }

    public void setPrinterModelId(String printerModelId) {
        PrinterModelId = printerModelId;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String modelName) {
        ModelName = modelName;
    }

    public String getModelValue() {
        return ModelValue;
    }

    public void setModelValue(String modelValue) {
        ModelValue = modelValue;
    }

    public String getPrinterType() {
        return PrinterType;
    }

    public void setPrinterType(String printerType) {
        PrinterType = printerType;
    }

    public String getPaperSize() {
        return PaperSize;
    }

    public void setPaperSize(String paperSize) {
        PaperSize = paperSize;
    }

    @Override
    public String toString() {
        return ModelName;
    }
}
