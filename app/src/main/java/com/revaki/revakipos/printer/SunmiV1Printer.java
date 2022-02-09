package com.revaki.revakipos.printer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;

import com.revaki.revakipos.printer.sunmiv1.ESCUtil;

import java.util.Date;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class SunmiV1Printer extends Printer {
    private Context context;

    private static final String SERVICE＿PACKAGE = "woyou.aidlservice.jiuiv5";
    private static final String SERVICE＿ACTION = "woyou.aidlservice.jiuiv5.IWoyouService";

    private IWoyouService woyouService;

    private long connectionCheckingInterval = 300;
    private long timeoutCheckingInterval = 10000;

    public SunmiV1Printer(Context context) {
        this.context = context;
    }

    public boolean connect() {
        boolean isConnected = false;
        boolean stopChecking = false;
        long lastConnectionCheck = new Date().getTime();
        long lastTimeoutCheck = new Date().getTime();

        try {
            printerState = PrinterState.CONNECTING;

            Intent intent = new Intent();
            intent.setPackage(SERVICE＿PACKAGE);
            intent.setAction(SERVICE＿ACTION);
            context.getApplicationContext().startService(intent);
            context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);

            while (!stopChecking) {
                if ((new Date().getTime() - lastConnectionCheck) > connectionCheckingInterval) {
                    lastConnectionCheck = new Date().getTime();
                    if (woyouService != null) {
                        isConnected = true;
                        stopChecking = true;
                    }
                }
                if ((new Date().getTime() - lastTimeoutCheck) > timeoutCheckingInterval) {
                    stopChecking = true;
                }
            }

            if (isConnected) {
                printerState = PrinterState.CONNECTED;
            } else {
                printerState = PrinterState.FAILED;
            }

        } catch (Exception e) {
            printerState = PrinterState.FAILED;
            e.printStackTrace();
        }


        return isConnected;
    }

    public void connectAsync() {
        ConnectTask connectTask = new ConnectTask();
        connectTask.execute();
    }

    private class ConnectTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean isConnected;
            isConnected = connect();
            return isConnected;
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            if (printerStateChangeListener != null) {
                printerStateChangeListener.onStateChange(printerState);
            }
        }

    }

    public boolean disconnect() {
        boolean status = false;
        try {
            if (woyouService != null) {
                Thread.sleep(5000);
                context.getApplicationContext().unbindService(connService);
                woyouService = null;
            }
            status = true;

            printerState = PrinterState.DISCONNECTED;

            if (printerStateChangeListener != null) {
                printerStateChangeListener.onStateChange(printerState);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };


    public void writeText(String text) {
        try {
            woyouService.printText(text, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeLine(String text) {
        try {
            woyouService.printText(text + "\n", null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void addNewLine() {
        try {
            woyouService.sendRAWData(ESCUtil.nextLine(1), null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void addNewLines(int count) {
        try {
            woyouService.sendRAWData(ESCUtil.nextLine(count), null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void setTextAlign(TextAlign textAlign) {
        try {
            switch (textAlign) {
                case ALIGN_LEFT:
                    woyouService.sendRAWData(ESCUtil.alignLeft(), null);
                    break;
                case ALIGN_CENTER:
                    woyouService.sendRAWData(ESCUtil.alignCenter(), null);
                    break;
                case ALIGN_RIGHT:
                    woyouService.sendRAWData(ESCUtil.alignRight(), null);
                    break;
                default:
                    woyouService.sendRAWData(ESCUtil.alignLeft(), null);
                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void boldOn() {
        try {
            woyouService.sendRAWData(ESCUtil.boldOn(), null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void boldOff() {
        try {
            woyouService.sendRAWData(ESCUtil.boldOff(), null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void underlineOn() {
        try {
            woyouService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void underlineOff() {
        try {
            woyouService.sendRAWData(ESCUtil.underlineOff(), null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setFontSize(float fontSize) {
        try {
            woyouService.setFontSize(fontSize, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


}

