package com.revaki.revakipos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.revaki.revakipos.adapter.TransactionAdapter;
import com.revaki.revakipos.beans.OrderMaster;
import com.revaki.revakipos.beans.WaiterDetail;
import com.revaki.revakipos.connectivity.ServiceManager;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.db.DBHelper;
import com.revaki.revakipos.helper.CommonUtils;
import com.revaki.revakipos.widget.DataProgressDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TransactionListActivity extends AppCompatActivity {

    private ApplicationDAL applicationDAL;


    List<OrderMaster> orderMasterCloseList = null;
    List<OrderMaster> orderMasterActiveList = null;

    private TextView tvTransactionDate;
    private Spinner spWaiter;
    private TabLayout tabLayout;
    private LinearLayout tabCloseTransaction;
    private LinearLayout tabActiveTransaction;
    private ListView listCloseTransaction;
    private ListView listActiveTransaction;
    private RelativeLayout rlEmptyData;

    private List<WaiterDetail> waiterDetails = null;

    private Calendar transactionDateCalendar;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        getSupportActionBar().setTitle("Transactions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTransactionDate = findViewById(R.id.tvTransactionDate);
        spWaiter = findViewById(R.id.spWaiter);
        tabLayout = findViewById(R.id.tabLayout);
        tabCloseTransaction = findViewById(R.id.tabCloseTransaction);
        tabActiveTransaction = findViewById(R.id.tabActiveTransaction);
        listCloseTransaction = findViewById(R.id.listCloseTransaction);
        listActiveTransaction = findViewById(R.id.listActiveTransaction);
        rlEmptyData = findViewById(R.id.rlEmptyData);

        transactionDateCalendar = Calendar.getInstance();
        transactionDateCalendar.setTime(CommonUtils.getDate());

        applicationDAL = new ApplicationDAL(this);

        tvTransactionDate.setText(sdf.format(transactionDateCalendar.getTime()));

        waiterDetails = applicationDAL.getWaiterDetail("", "");
        waiterDetails.add(0, new WaiterDetail("", "All"));

        ArrayAdapter<WaiterDetail> waiterDetailArrayAdapter = new ArrayAdapter<WaiterDetail>(this, android.R.layout.simple_spinner_item, waiterDetails);
        waiterDetailArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWaiter.setAdapter(waiterDetailArrayAdapter);

        spWaiter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadTransaction();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Close Transactions"));
        tabLayout.addTab(tabLayout.newTab().setText("Active Transactions"));

        tabCloseTransaction.setVisibility(View.VISIBLE);
        tabActiveTransaction.setVisibility(View.GONE);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    tabActiveTransaction.setVisibility(View.GONE);
                    tabCloseTransaction.setVisibility(View.VISIBLE);

                    if (orderMasterCloseList.size() == 0) {
                        rlEmptyData.setVisibility(View.VISIBLE);
                    } else {
                        rlEmptyData.setVisibility(View.GONE);
                    }

                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    tabCloseTransaction.setVisibility(View.GONE);
                    tabActiveTransaction.setVisibility(View.VISIBLE);

                    if (orderMasterActiveList.size() == 0) {
                        rlEmptyData.setVisibility(View.VISIBLE);
                    } else {
                        rlEmptyData.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        listCloseTransaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TransactionListActivity.this, TransactionDetailActivity.class);
                intent.putExtra("OrderMasterId", orderMasterCloseList.get(position).getOrderMasterId());
                startActivity(intent);
            }
        });

        listActiveTransaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("OrderMasterId", orderMasterActiveList.get(position).getOrderMasterId());
                intent.putExtra("TableId", orderMasterActiveList.get(position).getTableId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        loadTransaction();

        tabLayout.getTabAt(1).select();
        tabLayout.getTabAt(0).select();
    }

    private void loadTransaction() {

        String waiterId = waiterDetails.get(spWaiter.getSelectedItemPosition()).getWaiterId();
        orderMasterCloseList = applicationDAL.getOrderMasters(DBHelper.COL_ORDER_MASTER_STATUS_ID + " = 2 and " + DBHelper.COL_ORDER_MASTER_ORDER_DEVICE_DATE + " = " + transactionDateCalendar.getTime().getTime() + " and (" + DBHelper.COL_ORDER_MASTER_WAITER_ID + " = '" + waiterId + "' or '" + waiterId + "' = '')", DBHelper.COL_ORDER_MASTER_DEVICE_RECEIPT_NO + " desc");

        TransactionAdapter closeTransactionAdapter = new TransactionAdapter(this, orderMasterCloseList);
        listCloseTransaction.setAdapter(closeTransactionAdapter);

        orderMasterActiveList = applicationDAL.getOrderMasters(DBHelper.COL_ORDER_MASTER_STATUS_ID + " = 1 and " + DBHelper.COL_ORDER_MASTER_ORDER_DEVICE_DATE + " = " + transactionDateCalendar.getTime().getTime() + " and (" + DBHelper.COL_ORDER_MASTER_WAITER_ID + " = '" + waiterId + "' or '" + waiterId + "' = '')", DBHelper.COL_ORDER_MASTER_DEVICE_RECEIPT_NO + " desc");

        TransactionAdapter activeTransactionAdapter = new TransactionAdapter(this, orderMasterActiveList);
        listActiveTransaction.setAdapter(activeTransactionAdapter);
    }

    public void btnChangeTransactionDate_onClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        transactionDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        transactionDateCalendar.set(Calendar.MONTH, month);
                        transactionDateCalendar.set(Calendar.YEAR, year);

                        tvTransactionDate.setText(sdf.format(transactionDateCalendar.getTime()));
                        loadTransaction();
                    }
                }, transactionDateCalendar.get(Calendar.YEAR), transactionDateCalendar.get(Calendar.MONTH), transactionDateCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

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
