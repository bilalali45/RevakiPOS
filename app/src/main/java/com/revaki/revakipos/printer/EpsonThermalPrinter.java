package com.revaki.revakipos.printer;

import android.content.Context;
import android.graphics.Bitmap;

import com.epson.epos2.Epos2Exception;
import com.epson.eposprint.Print;
import com.epson.epos2.printer.Printer;


public class EpsonThermalPrinter extends com.revaki.revakipos.printer.Printer {

    private Context context;

    private Printer mPrinter = null;

    private int textWidth = 1;
    private int textHeight = 1;
    private String lastErrorMsg;

    private int printerModel;
    private String printerIp;
    private int port;


    public EpsonThermalPrinter(Context context, int printerModel) {
        this.context = context;
        this.printerModel = printerModel;
    }

    public String getLastErrorMsg() {
        return lastErrorMsg;
    }

    public void setLastErrorMsg(String lastErrorMsg) {
        this.lastErrorMsg = lastErrorMsg;
    }

    public int getPrinterModel() {
        return printerModel;
    }

    public void setPrinterModel(int printerModel) {
        this.printerModel = printerModel;
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

        try {
            lastErrorMsg = "";
            mPrinter = new Printer(printerModel, 0, context);
            mPrinter.connect("TCP:" + printerIp, Printer.PARAM_DEFAULT);

            mPrinter.clearCommandBuffer();
            isConnected = true;
        } catch (Exception e) {
            if (e instanceof Epos2Exception) {
                lastErrorMsg = getEpsonExceptionText(((Epos2Exception) e).getErrorStatus());
            }
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
            if (mPrinter != null) {
                Thread.sleep(1000);
                mPrinter.disconnect();

                mPrinter.clearCommandBuffer();

                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }


    @Override
    public void writeText(String text) {
        try {
            mPrinter.addText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeLine(String text) {
        try {
            mPrinter.addText(text + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void senData() {
        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            mPrinter.clearCommandBuffer();
        }
    }


    public void cutPaper() {
        try {
            mPrinter.addCut(Printer.CUT_FEED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addNewLine() {
        try {
            mPrinter.addFeedLine(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addNewLines(int count) {
        try {

            mPrinter.addFeedLine(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTextAlign(TextAlign textAlign) {
        try {
            switch (textAlign) {
                case ALIGN_LEFT:
                    mPrinter.addTextAlign(Printer.ALIGN_LEFT);
                    break;
                case ALIGN_CENTER:
                    mPrinter.addTextAlign(Printer.ALIGN_CENTER);
                    break;
                case ALIGN_RIGHT:
                    mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
                    break;
                default:
                    mPrinter.addTextAlign(Printer.ALIGN_LEFT);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void boldOn() {
        try {
            mPrinter.addTextStyle(Printer.FALSE, Print.FALSE, Printer.TRUE, Print.PARAM_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void boldOff() {
        try {
            mPrinter.addTextStyle(Printer.FALSE, Print.FALSE, Printer.FALSE, Print.PARAM_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void italicOn() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void italicOff() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void underlineOn() {
        try {
            mPrinter.addTextStyle(Printer.FALSE, Print.TRUE, Printer.FALSE, Print.PARAM_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void underlineOff() {
        try {
            mPrinter.addTextStyle(Printer.FALSE, Print.FALSE, Printer.FALSE, Print.PARAM_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doubleWidthOn() {
        try {
            textWidth = 2;
            mPrinter.addTextSize(textWidth, textHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doubleWidthOff() {
        try {
            textWidth = 1;
            mPrinter.addTextSize(textWidth, textHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doubleHeightOn() {
        try {
            textHeight = 2;
            mPrinter.addTextSize(textWidth, textHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doubleHeightOff() {
        try {
            textHeight = 1;
            mPrinter.addTextSize(textWidth, textHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void smallCharacterOn() {
        try {
            mPrinter.addTextFont(Printer.FONT_B);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void smallCharacterOff() {
        try {
            mPrinter.addTextFont(Printer.FONT_A);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getEpsonExceptionText(int state) {
        String return_text = "";
        switch (state) {
            case Epos2Exception.ERR_PARAM:
                return_text = "ERR_PARAM";
                break;
            case Epos2Exception.ERR_CONNECT:
                return_text = "ERR_CONNECT";
                break;
            case Epos2Exception.ERR_TIMEOUT:
                return_text = "ERR_TIMEOUT";
                break;
            case Epos2Exception.ERR_MEMORY:
                return_text = "ERR_MEMORY";
                break;
            case Epos2Exception.ERR_ILLEGAL:
                return_text = "ERR_ILLEGAL";
                break;
            case Epos2Exception.ERR_PROCESSING:
                return_text = "ERR_PROCESSING";
                break;
            case Epos2Exception.ERR_NOT_FOUND:
                return_text = "ERR_NOT_FOUND";
                break;
            case Epos2Exception.ERR_IN_USE:
                return_text = "ERR_IN_USE";
                break;
            case Epos2Exception.ERR_TYPE_INVALID:
                return_text = "ERR_TYPE_INVALID";
                break;
            case Epos2Exception.ERR_DISCONNECT:
                return_text = "ERR_DISCONNECT";
                break;
            case Epos2Exception.ERR_ALREADY_OPENED:
                return_text = "ERR_ALREADY_OPENED";
                break;
            case Epos2Exception.ERR_ALREADY_USED:
                return_text = "ERR_ALREADY_USED";
                break;
            case Epos2Exception.ERR_BOX_COUNT_OVER:
                return_text = "ERR_BOX_COUNT_OVER";
                break;
            case Epos2Exception.ERR_BOX_CLIENT_OVER:
                return_text = "ERR_BOX_CLIENT_OVER";
                break;
            case Epos2Exception.ERR_UNSUPPORTED:
                return_text = "ERR_UNSUPPORTED";
                break;
            case Epos2Exception.ERR_FAILURE:
                return_text = "ERR_FAILURE";
                break;
            default:
                return_text = String.format("%d", state);
                break;
        }
        return return_text;
    }

    public void printImage(Bitmap bitmap) {
        try {
            mPrinter.addImage(bitmap, 0, 0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    Printer.COLOR_1,
                    Printer.MODE_MONO,
                    Printer.HALFTONE_DITHER,
                    Printer.PARAM_DEFAULT,
                    Printer.COMPRESS_AUTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}