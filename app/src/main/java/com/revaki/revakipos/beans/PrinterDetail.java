package com.revaki.revakipos.beans;

public class PrinterDetail {

    private String PrinterId;
    private String Title;
    private String PrinterType;
    private String PrinterBrand;
    private String PrinterModel;
    private String PrinterIp;
    private int Port;
    private String PrintServerURL;
    private String PrinterName;

    public PrinterDetail() {

    }

    public PrinterDetail(String printerId, String title, String printerType, String printerBrand, String printerModel, String printerIp, int port, String printServerURL, String printerName) {
        PrinterId = printerId;
        Title = title;
        PrinterType = printerType;
        PrinterBrand = printerBrand;
        PrinterModel = printerModel;
        PrinterIp = printerIp;
        Port = port;
        PrintServerURL = printServerURL;
        PrinterName = printerName;
    }

    public String getPrinterId() {
        return PrinterId;
    }

    public void setPrinterId(String printerId) {
        PrinterId = printerId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPrinterType() {
        return PrinterType;
    }

    public void setPrinterType(String printerType) {
        PrinterType = printerType;
    }

    public String getPrinterBrand() {
        return PrinterBrand;
    }

    public void setPrinterBrand(String printerBrand) {
        PrinterBrand = printerBrand;
    }

    public String getPrinterModel() {
        return PrinterModel;
    }

    public void setPrinterModel(String printerModel) {
        PrinterModel = printerModel;
    }

    public String getPrinterIp() {
        return PrinterIp;
    }

    public void setPrinterIp(String printerIp) {
        PrinterIp = printerIp;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }

    public String getPrintServerURL() {
        return PrintServerURL;
    }

    public void setPrintServerURL(String printServerURL) {
        PrintServerURL = printServerURL;
    }

    public String getPrinterName() {
        return PrinterName;
    }

    public void setPrinterName(String printerName) {
        PrinterName = printerName;
    }

    public String getPrinterKey() {
        String printerKey = PrinterType + "," + PrinterBrand + "," + PrinterModel + "," + PrinterIp + "," + Port + "," + PrintServerURL + "," + PrinterName;
        return printerKey;
    }

    @Override
    public String toString() {
        return Title;
    }
}
