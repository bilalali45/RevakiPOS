package com.revaki.revakipos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.revaki.revakipos.adapter.TransactionDetailAdapter;
import com.revaki.revakipos.beans.OrderMaster;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.helper.ActivityRequest;
import com.revaki.revakipos.helper.CommonUtils;

import java.text.SimpleDateFormat;

public class TransactionDetailActivity extends AppCompatActivity {

    private ApplicationDAL applicationDAL;

    String orderMasterId = null;
    OrderMaster orderMaster = null;

    private TextView tvReceiptNo;
    private TextView tvTableName;
    private TextView tvOrderDate;
    private TextView tvNetAmount;
    private TextView tvCustomerName;
    private ListView listTransactionDetail;
    private Button btnPrint;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        getSupportActionBar().setTitle("Transaction");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvReceiptNo = findViewById(R.id.tvReceiptNo);
        tvTableName = findViewById(R.id.tvTableName);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvNetAmount = findViewById(R.id.tvNetAmount);
        tvCustomerName = findViewById(R.id.tvCustomerName);

        listTransactionDetail = findViewById(R.id.listTransactionDetail);
        btnPrint = findViewById(R.id.btnPrint);

        applicationDAL = new ApplicationDAL(this);

        orderMasterId = getIntent().getStringExtra("OrderMasterId");

        orderMaster = applicationDAL.getOrderMaster(orderMasterId);

        tvReceiptNo.setText("Receipt # " + orderMaster.getDeviceReceiptNo());
        tvTableName.setText("Table : " + orderMaster.getTableName());
        tvOrderDate.setText(sdf.format(orderMaster.getOrderDeviceDate()));
        tvNetAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));
        tvCustomerName.setText(orderMaster.getCustomerName());

        TransactionDetailAdapter transactionDetailAdapter = new TransactionDetailAdapter(this, orderMaster.getOrderChilds());
        listTransactionDetail.setAdapter(transactionDetailAdapter);

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionDetailActivity.this, PrintViewActivity.class);
                intent.putExtra("PrintType", "PostBill");
                intent.putExtra("PrintMode", 3);
                intent.putExtra("OrderMasterId", orderMaster.getOrderMasterId());
                startActivityForResult(intent, ActivityRequest.REQUEST_PRINT_POST_BILL);
            }
        });

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
