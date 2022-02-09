package com.revaki.revakipos.printer;

import com.revaki.revakipos.beans.PrinterModel;
import java.util.ArrayList;
import java.util.List;

public class Printer {

    private static PrinterType printerType;
    protected PrinterState printerState = PrinterState.UNKNOWN;
    protected OnPrinterStateChangeListener printerStateChangeListener = null;
    private static List<String> printerTypes;
    private static List<PrinterModel> printerModels;

    static {
        printerTypes = new ArrayList<String>();
        printerTypes.add("Network Printer");
        printerTypes.add("Print Server");
        printerTypes.add("Embedded");

        printerModels = new ArrayList<PrinterModel>();
        printerModels.add(new PrinterModel("1", "BayLan", "Bay Lan", "0", "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("1", "BlackCopper", "Black Copper", "0", "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("2", "Print Server", "Print Server", "0", "Print Server", "80mm"));
        printerModels.add(new PrinterModel("3", "Sunmi", "Sunmi V1", "0", "Embedded", "58mm"));
        printerModels.add(new PrinterModel("4", "Epson", "Epson TM-M10 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_M10), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("5", "Epson", "Epson TM-M30 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_M30), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("6", "Epson", "Epson TM_P20 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_P20), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("7", "Epson", "Epson TM_P60 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_P60), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("8", "Epson", "Epson TM_P60II Series", String.valueOf(com.epson.epos2.printer.Printer.TM_P60II), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("9", "Epson", "Epson TM_P80 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_P80), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("10", "Epson", "Epson TM_T20 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_T20), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("11", "Epson", "Epson TM_T60 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_T60), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("12", "Epson", "Epson TM_T70 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_T70), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("13", "Epson", "Epson TM_T81 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_T81), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("14", "Epson", "Epson TM_T82 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_T82), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("15", "Epson", "Epson TM_T83 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_T83), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("15", "Epson", "Epson TM_T83III Series", String.valueOf(com.epson.epos2.printer.Printer.TM_T83III), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("15", "Epson", "Epson TM_T88 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_T88), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("15", "Epson", "Epson TM_T90 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_T90), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("15", "Epson", "Epson TM_T90KP Series", String.valueOf(com.epson.epos2.printer.Printer.TM_T90KP), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("15", "Epson", "Epson TM_T100 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_T100), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("15", "Epson", "Epson TM_U220 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_U220), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("15", "Epson", "Epson TM_U330 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_U330), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("15", "Epson", "Epson TM_L90 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_L90), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("15", "Epson", "Epson TM_H6000 Series", String.valueOf(com.epson.epos2.printer.Printer.TM_H6000), "Network Printer", "80mm"));
        printerModels.add(new PrinterModel("15", "Epson", "Epson TM_M30II Series", String.valueOf(com.epson.epos2.printer.Printer.TM_M30II), "Network Printer", "80mm"));

    }

    public static void setPrinterType(PrinterType printerType) {
        Printer.printerType = printerType;
    }

    public static Printer.PrinterType getPrinterType() {
        return Printer.printerType;
    }

    public static List<String> getPrinterTypes() {
        return printerTypes;
    }

    public static List<PrinterModel> getPrinterModels() {
        return printerModels;
    }

    public static PrinterModel getPrinterModel(String brandName, String modelName, String printerType) {
        PrinterModel findPrinterModel = null;
        for (PrinterModel printerModel : printerModels) {
            if (printerModel.getBrandName().equals(brandName) && printerModel.getModelName().equals(modelName) && printerModel.getPrinterType().equals(printerType)) {
                findPrinterModel = printerModel;
                break;
            }
        }
        return findPrinterModel;
    }

    public PrinterState getPrinterState() {
        return printerState;
    }

    public void setPrinterState(PrinterState printerState) {
        this.printerState = printerState;
    }

    public void setOnPrinterStateChangeListener(OnPrinterStateChangeListener printerStateChangeListener) {
        this.printerStateChangeListener = printerStateChangeListener;
    }

    public void connectAsync() {
        throw new RuntimeException("Stub!");
    }

    public boolean connect() {
        throw new RuntimeException("Stub!");
    }

    public boolean disconnect() {
        throw new RuntimeException("Stub!");
    }

    public void writeText(String text) {
        throw new RuntimeException("Stub!");
    }

    public void writeLine(String text) {
        throw new RuntimeException("Stub!");
    }


    public void addNewLine() {
        throw new RuntimeException("Stub!");
    }

    public void addNewLines(int count) {
        throw new RuntimeException("Stub!");
    }

    public void setTextAlign(TextAlign textAlign) {
        throw new RuntimeException("Stub!");
    }

    public void boldOn() {
        throw new RuntimeException("Stub!");
    }

    public void boldOff() {
        throw new RuntimeException("Stub!");
    }

    public void italicOn() {
        throw new RuntimeException("Stub!");
    }

    public void italicOff() {
        throw new RuntimeException("Stub!");
    }

    public void underlineOn() {
        throw new RuntimeException("Stub!");
    }

    public void underlineOff() {
        throw new RuntimeException("Stub!");
    }

    public void doubleWidthOn() {
        throw new RuntimeException("Stub!");
    }

    public void doubleWidthOff() {
        throw new RuntimeException("Stub!");
    }

    public void doubleHeightOn() {
        throw new RuntimeException("Stub!");
    }

    public void doubleHeightOff() {
        throw new RuntimeException("Stub!");
    }

    public void smallCharacterOn() {
        throw new RuntimeException("Stub!");
    }

    public void smallCharacterOff() {
        throw new RuntimeException("Stub!");
    }


    public enum PaperSize {
        Paper_80mm("0"), Paper_58mm("1");
        public String value;

        private PaperSize(String value) {
            this.value = value;
        }
    }

    public enum PrinterType {
        PRP80250B(0), SunmiV1(1), Bluetooth(2);
        public int value;

        private PrinterType(int value) {
            this.value = value;
        }
    }

    public enum TextAlign {
        ALIGN_LEFT, ALIGN_CENTER, ALIGN_RIGHT;
    }


    public enum PrinterState {
        CONNECTING, CONNECTED, INVALID, FAILED, DISCONNECTED, UNKNOWN;
    }

    public enum BarcodeType {
        QR_CODE,
        CODE39,
        CODE128,
        CODABAR
    }

    public interface OnPrinterStateChangeListener {
        void onStateChange(PrinterState printerState);
    }

}
