package com.revaki.revakipos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.revaki.revakipos.adapter.PrintersAdapter;
import com.revaki.revakipos.beans.PrinterDetail;
import com.revaki.revakipos.db.ApplicationDAL;

import java.util.List;

public class PrinterListActivity extends AppCompatActivity {

    private ListView listPrinters;
    List<PrinterDetail> printerDetailList;
    private PrintersAdapter printersAdapter;
    private Button btnAddPrinter;
    private ApplicationDAL applicationDAL;
    private TextView tvNoRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Printers");
        initViews();
        applicationDAL = new ApplicationDAL(this);

        btnAddPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrinterListActivity.this, PrinterDetailActivity.class);
                startActivity(intent);
            }
        });

        listPrinters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PrinterDetail printerDetail = printerDetailList.get(i);

                Intent intent = new Intent(PrinterListActivity.this, PrinterDetailActivity.class);
                intent.putExtra("PrinterId",printerDetail.getPrinterId());
                startActivity(intent);
            }
        });

    }

    private void initViews() {
        tvNoRecord = findViewById(R.id.tvNoRecord);
        btnAddPrinter = findViewById(R.id.btnAddPrinter);
        listPrinters = findViewById(R.id.listPrinters);
    }

    private void loadPrinters() {
        printerDetailList = applicationDAL.getPrinterDetails("","");
        printersAdapter = new PrintersAdapter(PrinterListActivity.this,printerDetailList);
        listPrinters.setAdapter(printersAdapter);

        if(printerDetailList.size() == 0){
            tvNoRecord.setVisibility(View.VISIBLE);
        }else{
            tvNoRecord.setVisibility(View.GONE);
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

    @Override
    protected void onStart() {
        super.onStart();
        loadPrinters();
    }
}
