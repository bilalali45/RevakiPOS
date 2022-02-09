package com.revaki.revakipos;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.revaki.revakipos.beans.PrinterDetail;
import com.revaki.revakipos.beans.PrinterModel;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.helper.UIHelper;
import com.revaki.revakipos.printer.Printer;
import com.revaki.revakipos.printer.WifiThermalPrinter;

import java.util.ArrayList;
import java.util.List;

public class PrinterDetailActivity extends AppCompatActivity {

    private LinearLayout lyNetworkPrinter;
    private LinearLayout lyPrintServer;
    private EditText etTitle;
    private Spinner spPrinterType;
    private Spinner spPrinterModel;
    private EditText etPrinterIP;
    private EditText etPort;
    private Button btnTest;
    private EditText etPrintServerURL;
    private EditText etPrinterName;
    private Button btnSubmit;
    private Button btnDelete;
    private ApplicationDAL applicationDAL;
    private String PrinterId = "";
    private boolean isEdit = false;
    private List<PrinterModel> printerModels;
    PrinterDetail printerDetail = new PrinterDetail();
    PrinterModel selectedPrinterModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_detail);

        getSupportActionBar().setTitle("Add Printer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();


        ArrayAdapter<String> printerTypeArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Printer.getPrinterTypes());
        printerTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPrinterType.setAdapter(printerTypeArrayAdapter);


        applicationDAL = new ApplicationDAL(this);
        PrinterId = getIntent().getStringExtra("PrinterId");


        spPrinterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                String printerType = spPrinterType.getItemAtPosition(position).toString();
                printerModels = new ArrayList<PrinterModel>();

                for (PrinterModel printerModel : Printer.getPrinterModels()) {
                    if (printerModel.getPrinterType().equals(printerType)) {
                        printerModels.add(printerModel);
                    }
                }

                if (printerType.equals("Network Printer")) {
                    lyNetworkPrinter.setVisibility(View.VISIBLE);
                    lyPrintServer.setVisibility(View.GONE);
                } else if (printerType.equals("Print Server")) {
                    lyNetworkPrinter.setVisibility(View.GONE);
                    lyPrintServer.setVisibility(View.VISIBLE);
                } else if (printerType.equals("Embedded")) {
                    lyNetworkPrinter.setVisibility(View.GONE);
                    lyPrintServer.setVisibility(View.GONE);
                }

                ArrayAdapter<PrinterModel> printerModelArrayAdapter = new ArrayAdapter<PrinterModel>(PrinterDetailActivity.this, android.R.layout.simple_spinner_item, printerModels);
                printerModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spPrinterModel.setAdapter(printerModelArrayAdapter);

                if (selectedPrinterModel != null) {
                    spPrinterModel.setSelection(printerModels.indexOf(selectedPrinterModel), true);
                    selectedPrinterModel = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (PrinterId != null) {
            getSupportActionBar().setTitle("Edit Printer");
            btnDelete.setVisibility(View.VISIBLE);

            printerDetail = applicationDAL.getPrinterDetail(PrinterId);

            selectedPrinterModel = Printer.getPrinterModel(printerDetail.getPrinterBrand(), printerDetail.getPrinterModel(), printerDetail.getPrinterType());

            etTitle.setText(printerDetail.getTitle());
            spPrinterType.setSelection(Printer.getPrinterTypes().indexOf(printerDetail.getPrinterType()), true);
            etPrinterIP.setText(String.valueOf(printerDetail.getPrinterIp()));
            etPort.setText(String.valueOf(printerDetail.getPort()));
            etPrintServerURL.setText(String.valueOf(printerDetail.getPrintServerURL()));
            etPrinterName.setText(String.valueOf(printerDetail.getPrinterName()));
        } else {
            btnDelete.setVisibility(View.GONE);
        }


        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (printerModels.get(spPrinterModel.getSelectedItemPosition()).getBrandName().equals("BayLan") || printerModels.get(spPrinterModel.getSelectedItemPosition()).getBrandName().equals("BlackCopper")) {
                        WifiThermalPrinter printer = new WifiThermalPrinter();

                        boolean isConnected = printer.connect(etPrinterIP.getText().toString(), Integer.valueOf(etPort.getText().toString()));
                        if (isConnected) {
                            printer.printerBeep();

                            printer.disconnect();
                        }
                    }
                } catch (Exception e) {
                    UIHelper.showShortToast(PrinterDetailActivity.this, e.getMessage());
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    if (spPrinterType.getSelectedItem().toString().equals("Print Server") == false) {
                        etPrinterName.setText(printerModels.get(spPrinterModel.getSelectedItemPosition()).getModelName());
                    }
                    printerDetail.setTitle(etTitle.getText().toString());
                    printerDetail.setPrinterType(printerModels.get(spPrinterModel.getSelectedItemPosition()).getPrinterType());
                    printerDetail.setPrinterBrand(printerModels.get(spPrinterModel.getSelectedItemPosition()).getBrandName());
                    printerDetail.setPrinterModel(printerModels.get(spPrinterModel.getSelectedItemPosition()).getModelName());
                    printerDetail.setPrinterIp(etPrinterIP.getText().toString());
                    printerDetail.setPort(Integer.parseInt("0" + etPort.getText().toString()));
                    printerDetail.setPrintServerURL(etPrintServerURL.getText().toString());
                    printerDetail.setPrinterName(etPrinterName.getText().toString());
                    applicationDAL.addUpdatePrinterDetail(printerDetail);
                    finish();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showConfirmDialog(PrinterDetailActivity.this, "", "Are you sure to delete printer?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        applicationDAL.deletePrinterDetail(printerDetail.getPrinterId());
                        finish();
                    }
                });
            }
        });


    }


    private void initViews() {
        lyNetworkPrinter = findViewById(R.id.lyNetworkPrinter);
        lyPrintServer = findViewById(R.id.lyPrintServer);
        etTitle = findViewById(R.id.etTitle);
        spPrinterType = findViewById(R.id.spPrinterType);
        spPrinterModel = findViewById(R.id.spPrinterModel);
        etPrinterIP = findViewById(R.id.etPrinterIP);
        etPort = findViewById(R.id.etPort);
        btnTest = findViewById(R.id.btnTest);
        etPrintServerURL = findViewById(R.id.etPrintServerURL);
        etPrinterName = findViewById(R.id.etPrinterName);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnDelete = findViewById(R.id.btnDelete);
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

    private boolean validateFields() {
        if (etTitle.getText().toString().isEmpty()) {
            UIHelper.showErrorDialog(this, "Error", "Title Required");
            return false;
        }
        if (spPrinterType.getSelectedItem().equals("Network Printer")) {
            if (etTitle.getText().toString().isEmpty()) {
                UIHelper.showErrorDialog(this, "Error", "Ip Required");
                return false;
            } else if (etTitle.getText().toString().isEmpty()) {
                UIHelper.showErrorDialog(this, "Error", "Port Required");
                return false;
            }
        } else if (spPrinterType.getSelectedItem().equals("Print Server")) {
            if (etPrintServerURL.getText().toString().isEmpty()) {
                UIHelper.showErrorDialog(this, "Error", "Print Server URL Required");
                return false;
            } else if (etPrinterName.getText().toString().isEmpty()) {
                UIHelper.showErrorDialog(this, "Error", "Printer Name Required");
                return false;
            }
        }
        return true;
    }
}
