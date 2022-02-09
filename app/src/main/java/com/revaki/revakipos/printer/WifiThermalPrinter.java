package com.revaki.revakipos.printer;

import android.graphics.Bitmap;

import com.rt.printerlibrary.bean.WiFiConfigBean;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscCmd;
import com.rt.printerlibrary.connect.PrinterInterface;
import com.rt.printerlibrary.connect.WiFiInterface;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.enumerate.ConnectStateEnum;
import com.rt.printerlibrary.enumerate.SettingEnum;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.printer.ThermalPrinter;
import com.rt.printerlibrary.setting.BarcodeSetting;
import com.rt.printerlibrary.setting.BitmapSetting;
import com.rt.printerlibrary.setting.TextSetting;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class WifiThermalPrinter extends Printer {


    private RTPrinter rtPrinter = null;
    private Cmd cmdText = null;
    private TextSetting textSetting = null;
    private long connectionCheckingInterval = 300;
    private long timeoutCheckingInterval = 10000;

    private String printerIp;
    private int port;


    public WifiThermalPrinter() {
        rtPrinter = new ThermalPrinter();
        //PrinterObserverManager.getInstance().add(printerObserver);

        textSetting = new TextSetting();
        textSetting.setAlign(CommonEnum.ALIGN_LEFT);
        textSetting.setBold(SettingEnum.Disable);
        textSetting.setUnderline(SettingEnum.Disable);
        textSetting.setIsAntiWhite(SettingEnum.Disable);
        textSetting.setDoubleHeight(SettingEnum.Disable);
        textSetting.setDoubleWidth(SettingEnum.Disable);
        textSetting.setItalic(SettingEnum.Disable);
        textSetting.setIsEscSmallCharactor(SettingEnum.Disable);
    }

    public String getPrinterIp() {
        return printerIp;
    }

    public void setPrinterIp(String printerIp) {
        this.printerIp = printerIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean connect(String printerIp, int port) {
        this.printerIp = printerIp;
        this.port = port;
        boolean isConnected = false;
        boolean stopChecking = false;
        long lastConnectionCheck = new Date().getTime();
        long lastTimeoutCheck = new Date().getTime();

        try {
            rtPrinter = new ThermalPrinter();
            PrinterInterface printerInterface = new WiFiInterface();
            WiFiConfigBean wiFiConfigBean = new WiFiConfigBean(printerIp, port);
            rtPrinter.setPrinterInterface(printerInterface);

            rtPrinter.connect(wiFiConfigBean);

            while (!stopChecking) {
                if ((new Date().getTime() - lastConnectionCheck) > connectionCheckingInterval) {
                    lastConnectionCheck = new Date().getTime();
                    if (rtPrinter.getConnectState() == ConnectStateEnum.Connected) {
                        isConnected = true;
                        stopChecking = true;
                    }
                }
                if ((new Date().getTime() - lastTimeoutCheck) > timeoutCheckingInterval) {
                    stopChecking = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isConnected;
    }

    @Override
    public boolean connect() {
        return connect(printerIp, port);
    }

    @Override
    public boolean disconnect() {
        boolean status = false;
        try {
            if (rtPrinter != null) {
                if (rtPrinter.getConnectState() == ConnectStateEnum.Connected) {
                    Thread.sleep(1000);
                    rtPrinter.disConnect();
                    Thread.sleep(500);
                }
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public void initializeTextMsg() {
        cmdText = new EscCmd();
        cmdText.append(cmdText.getHeaderCmd()); //Initial
        cmdText.setChartsetName("UTF-8");
    }

    @Override
    public void writeText(String text) {
        try {
            cmdText.append(cmdText.getTextCmd(textSetting, text));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeLine(String text) {
        try {
            cmdText.append(cmdText.getTextCmd(textSetting, text));
            cmdText.append(cmdText.getLFCRCmd());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void writeTextMsgAsync() {
        rtPrinter.writeMsgAsync(cmdText.getAppendCmds());
    }


    public void cutPaper() {
        cmdText.append(cmdText.getAllCutCmd());
    }

    @Override
    public void addNewLine() {
        cmdText.append(cmdText.getLFCRCmd());
    }

    @Override
    public void addNewLines(int count) {
        for (int i = 0; i < count; i++) {
            cmdText.append(cmdText.getLFCRCmd());
        }
    }

    @Override
    public void setTextAlign(TextAlign textAlign) {
        switch (textAlign) {
            case ALIGN_LEFT:
                textSetting.setAlign(CommonEnum.ALIGN_LEFT);
                break;
            case ALIGN_CENTER:
                textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
                break;
            case ALIGN_RIGHT:
                textSetting.setAlign(CommonEnum.ALIGN_RIGHT);
                break;
            default:
                textSetting.setAlign(CommonEnum.ALIGN_LEFT);
                break;
        }
    }

    @Override
    public void boldOn() {
        textSetting.setBold(SettingEnum.Enable);
    }

    @Override
    public void boldOff() {
        textSetting.setBold(SettingEnum.Disable);
    }

    @Override
    public void italicOn() {
        textSetting.setItalic(SettingEnum.Enable);
    }

    @Override
    public void italicOff() {
        textSetting.setItalic(SettingEnum.Disable);
    }

    @Override
    public void underlineOn() {
        textSetting.setUnderline(SettingEnum.Enable);
    }

    @Override
    public void underlineOff() {
        textSetting.setUnderline(SettingEnum.Disable);
    }

    @Override
    public void doubleWidthOn() {
        textSetting.setDoubleWidth(SettingEnum.Enable);
    }

    @Override
    public void doubleWidthOff() {
        textSetting.setDoubleWidth(SettingEnum.Disable);
    }

    @Override
    public void doubleHeightOn() {
        textSetting.setDoubleHeight(SettingEnum.Enable);
    }

    @Override
    public void doubleHeightOff() {
        textSetting.setDoubleHeight(SettingEnum.Disable);
    }

    @Override
    public void smallCharacterOn() {
        textSetting.setIsEscSmallCharactor(SettingEnum.Enable);
    }

    @Override
    public void smallCharacterOff() {
        textSetting.setIsEscSmallCharactor(SettingEnum.Disable);
    }

    public void printImage(Bitmap bitmap) {
        try {
            BitmapSetting bitmapSetting = new BitmapSetting();
            cmdText.append(cmdText.getBitmapCmd(bitmapSetting, Bitmap.createBitmap(bitmap)));
            cmdText.append(cmdText.getLFCRCmd());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printBarcode(BarcodeType barcodeType, String text) {
        try {
            BarcodeSetting barcodeSetting = new BarcodeSetting();

            cmdText.append(cmdText.getBarcodeCmd(getBarcodeType(barcodeType), barcodeSetting, text));
            cmdText.append(cmdText.getLFCRCmd());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private com.rt.printerlibrary.enumerate.BarcodeType getBarcodeType(BarcodeType barcodeFormat) {
        com.rt.printerlibrary.enumerate.BarcodeType barcodeType = com.rt.printerlibrary.enumerate.BarcodeType.QR_CODE;

        if (barcodeFormat == BarcodeType.CODE39) {
            barcodeType = com.rt.printerlibrary.enumerate.BarcodeType.CODE39;
        } else if (barcodeFormat == BarcodeType.CODE128) {
            barcodeType = com.rt.printerlibrary.enumerate.BarcodeType.CODE128;
        } else if (barcodeFormat == BarcodeType.CODABAR) {
            barcodeType = com.rt.printerlibrary.enumerate.BarcodeType.CODABAR;
        }

        return barcodeType;
    }

    public void printerBeep() {
        Cmd cmd = new EscCmd();
        cmd.append(cmd.getBeepCmd());
        rtPrinter.writeMsgAsync(cmd.getAppendCmds());
    }

    public void printerSelfTest() {
        Cmd cmd = new EscCmd();
        cmd.append(cmd.getHeaderCmd());
        cmd.append(cmd.getLFCRCmd());
        cmd.append(cmd.getSelfTestCmd());
        cmd.append(cmd.getLFCRCmd());
        rtPrinter.writeMsgAsync(cmd.getAppendCmds());
    }

    public void printerPaperCut() {
        Cmd cmd = new EscCmd();
        cmd.append(cmd.getAllCutCmd());
        rtPrinter.writeMsgAsync(cmd.getAppendCmds());
    }

    public void openCashDrawer() {
        cmdText.append(cmdText.getOpenMoneyBoxCmd());
    }

}