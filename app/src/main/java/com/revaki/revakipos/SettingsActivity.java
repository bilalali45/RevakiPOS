package com.revaki.revakipos;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.revaki.revakipos.beans.PrinterDetail;
import com.revaki.revakipos.beans.SettingDetail;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.helper.ActivityRequest;
import com.revaki.revakipos.printer.Printer;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private ApplicationDAL applicationDAL;
    private SwitchCompat csShowPreBillPreview;
    private SwitchCompat csShowPostBillPreview;
    private SwitchCompat csShowKOTPreview;
    private SwitchCompat csAutoPrintAfterCheckout;
    private SwitchCompat csOpenCashDrawerAfterCheckout;
    private TextView tvDefaultPrinter;
    private TextView tvDefaultKitchenPrinter;
    private TextView tvKitchenPrintCopy;
    private List<PrinterDetail> printerDetails = null;
    private SettingDetail settingDetail = null;
    private int selectedPrinterIndex = 0;
    private int selectedKitchenPrinterIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        initViews();
        applicationDAL = new ApplicationDAL(this);

        loadSettings();
    }


    private void loadSettings() {
        printerDetails = applicationDAL.getPrinterDetails("", "");

        settingDetail = applicationDAL.getSettingDetail();

        selectedPrinterIndex = 0;
        selectedKitchenPrinterIndex = 0;

        PrinterDetail defaultPrinterDetail = applicationDAL.getPrinterDetail(settingDetail.getDefaultPrinterId());
        PrinterDetail kitchenPrinterDetail = applicationDAL.getPrinterDetail(settingDetail.getKitchenPrinterId());

        if (defaultPrinterDetail != null) {
            for (int i = 0; i < printerDetails.size(); i++) {
                if (printerDetails.get(i).getPrinterId().equals(defaultPrinterDetail.getPrinterId())) {
                    selectedPrinterIndex = i;
                    break;
                }
            }
        }

        if (kitchenPrinterDetail != null) {
            for (int i = 0; i < printerDetails.size(); i++) {
                if (printerDetails.get(i).getPrinterId().equals(kitchenPrinterDetail.getPrinterId())) {
                    selectedKitchenPrinterIndex = i;
                    break;
                }
            }
        }

        if (printerDetails.size() > 0) {
            tvDefaultPrinter.setText(printerDetails.get(selectedPrinterIndex).getTitle());
            tvDefaultKitchenPrinter.setText(printerDetails.get(selectedKitchenPrinterIndex).getTitle());
        } else {
            tvDefaultPrinter.setText("Select Printer");
            tvDefaultKitchenPrinter.setText("Select Printer");
        }

        tvKitchenPrintCopy.setText(String.valueOf(settingDetail.getKitchenPintCopy()));
        csShowPreBillPreview.setChecked(settingDetail.isShowPreBillPreview());
        csShowPostBillPreview.setChecked(settingDetail.isShowPostBillPreview());
        csShowKOTPreview.setChecked(settingDetail.isShowKitchenPrintPreview());
        csAutoPrintAfterCheckout.setChecked(settingDetail.isAutoPrintAfterCheckout());
        csOpenCashDrawerAfterCheckout.setChecked(settingDetail.isOpenCashDrawerAfterCheckout());
    }

    @Override
    public void onBackPressed() {
        if (printerDetails.size() > 0) {
            settingDetail.setDefaultPrinterId(printerDetails.get(selectedPrinterIndex).getPrinterId());
            settingDetail.setKitchenPrinterId(printerDetails.get(selectedKitchenPrinterIndex).getPrinterId());
        } else {
            settingDetail.setDefaultPrinterId("");
            settingDetail.setKitchenPrinterId("");
        }
        settingDetail.setShowPreBillPreview(csShowPreBillPreview.isChecked());
        settingDetail.setShowPostBillPreview(csShowPostBillPreview.isChecked());
        settingDetail.setShowKitchenPrintPreview(csShowKOTPreview.isChecked());
        settingDetail.setAutoPrintAfterCheckout(csAutoPrintAfterCheckout.isChecked());
        settingDetail.setOpenCashDrawerAfterCheckout(csOpenCashDrawerAfterCheckout.isChecked());
        applicationDAL.addUpdateSettingDetail(settingDetail);
        super.onBackPressed();
    }

    private void initViews() {
        tvDefaultPrinter = findViewById(R.id.tvDefaultPrinter);
        tvDefaultKitchenPrinter = findViewById(R.id.tvKitchenDefaultPrinter);
        tvKitchenPrintCopy = findViewById(R.id.tvKitchenPrintCopy);
        csShowPreBillPreview = findViewById(R.id.csShowPreBillPreview);
        csShowPostBillPreview = findViewById(R.id.csShowPostBillPreview);
        csShowKOTPreview = findViewById(R.id.csShowKOTPreview);
        csAutoPrintAfterCheckout = findViewById(R.id.csAutoPrintAfterCheckout);
        csOpenCashDrawerAfterCheckout = findViewById(R.id.csOpenCashDrawerAfterCheckout);
    }

    public void actionBar_onClick(View v) {
        if (v.getId() == R.id.lyPrinters) {
            Intent intent = new Intent(SettingsActivity.this, PrinterListActivity.class);
            startActivityForResult(intent, ActivityRequest.REQUEST_PRINTERS);
        } else if (v.getId() == R.id.lyDefaultPrinter) {

            ArrayAdapter<PrinterDetail> printerDetailArrayAdapter = new ArrayAdapter<PrinterDetail>(this, android.R.layout.simple_list_item_single_choice, printerDetails);

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setTitle("Select Printer");
            builder.setSingleChoiceItems(printerDetailArrayAdapter, selectedPrinterIndex, null);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (printerDetails.size() > 0) {
                        selectedPrinterIndex = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        tvDefaultPrinter.setText(printerDetails.get(selectedPrinterIndex).getTitle());
                    }
                }
            });

            builder.setNegativeButton("Cancel", null);

            builder.show();
        } else if (v.getId() == R.id.lyKitchenDefaultPrinter) {

            ArrayAdapter<PrinterDetail> printerDetailArrayAdapter = new ArrayAdapter<PrinterDetail>(this, android.R.layout.simple_list_item_single_choice, printerDetails);

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setTitle("Select Printer");
            builder.setSingleChoiceItems(printerDetailArrayAdapter, selectedKitchenPrinterIndex, null);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (printerDetails.size() > 0) {
                        selectedKitchenPrinterIndex = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        tvDefaultKitchenPrinter.setText(printerDetails.get(selectedKitchenPrinterIndex).getTitle());

                    }
                }
            });

            builder.setNegativeButton("Cancel", null);

            builder.show();
        } else if (v.getId() == R.id.lyKitchenPrintCopy) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter Kitchen Printer No. of Copies");

            final EditText input = new EditText(this);
            input.setPadding(30, 30, 30, 30);
            input.setText(String.valueOf(settingDetail.getKitchenPintCopy()));
            input.setSingleLine(true);
            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setSelection(input.length());
            builder.setView(input);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int kitchenPrintCopy = Integer.valueOf("0" + input.getText().toString());
                    if (kitchenPrintCopy == 0) {
                        input.setText("1");
                    }

                    tvKitchenPrintCopy.setText(String.valueOf(kitchenPrintCopy));
                    settingDetail.setKitchenPintCopy(kitchenPrintCopy);
                }
            });

            builder.setNegativeButton("Cancel", null);
            builder.show();

        } else if (v.getId() == R.id.lyKitchenPrinterCategories) {

            Intent intent = new Intent(SettingsActivity.this, KitchenPrinterCategoryActivity.class);
            startActivityForResult(intent, ActivityRequest.REQUEST_KITCHEN_PRINTER_CATEGORIES);

        } else if (v.getId() == R.id.rlShowPreBillPreview) {

            csShowPreBillPreview.setChecked(!csShowPreBillPreview.isChecked());

        } else if (v.getId() == R.id.rlShowPostBillPreview) {
            csShowPostBillPreview.setChecked(!csShowPostBillPreview.isChecked());

        } else if (v.getId() == R.id.rlShowKOTPreview) {
            csShowKOTPreview.setChecked(!csShowKOTPreview.isChecked());
        } else if (v.getId() == R.id.rlAutoPrintAfterCheckout) {
            csAutoPrintAfterCheckout.setChecked(!csAutoPrintAfterCheckout.isChecked());

        } else if (v.getId() == R.id.rlOpenCashDrawerAfterCheckout) {
            csOpenCashDrawerAfterCheckout.setChecked(!csOpenCashDrawerAfterCheckout.isChecked());
        }
     /*   if (v.getId() == R.id.lyFirstName) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setTitle("Select Currency");
            builder.setSingleChoiceItems(array, 1, null);

            builder.show();
        }
        else    if (v.getId() == R.id.rlRealTimeMode) {
            //csRealTimeMode.setChecked(!csRealTimeMode.isChecked());
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequest.REQUEST_PRINTERS || requestCode == ActivityRequest.REQUEST_KITCHEN_PRINTER_CATEGORIES) {
            loadSettings();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
