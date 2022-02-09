package com.revaki.revakipos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.revaki.revakipos.adapter.ShiftRegisterAdapter;
import com.revaki.revakipos.beans.ShiftRecord;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.db.DBHelper;
import com.revaki.revakipos.helper.ActivityRequest;

import java.util.List;

public class ShiftRegisterActivity extends AppCompatActivity {

    private ApplicationDAL applicationDAL;

    List<ShiftRecord> shiftRecordList = null;

    private ListView listShiftRegister;
    private RelativeLayout rlEmptyData;

    private int reportType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shift Register");

        reportType = getIntent().getIntExtra("ReportType", 1);

        if (reportType == 2) {
            getSupportActionBar().setTitle("Shift Summary");
        }else if (reportType == 3) {
            getSupportActionBar().setTitle("Void Report");
        }

        listShiftRegister = findViewById(R.id.listShiftRegister);
        rlEmptyData = findViewById(R.id.rlEmptyData);

        applicationDAL = new ApplicationDAL(this);

        listShiftRegister.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShiftRegisterActivity.this, PrintViewActivity.class);
                if (reportType == 1) {
                    intent.putExtra("PrintType", "ShiftRegister");
                }
                else if (reportType == 2)  {
                    intent.putExtra("PrintType", "ShiftSummary");
                }
                else if (reportType == 3)  {
                    intent.putExtra("PrintType", "VoidReport");
                }
                intent.putExtra("ShiftRecordId", shiftRecordList.get(position).getShiftRecordId());
                intent.putExtra("ShiftType", shiftRecordList.get(position).getShiftType());
                startActivityForResult(intent, ActivityRequest.REQUEST_PRINT_SHIFT_REGISTER);
            }
        });

        loadShiftRegister();
    }

    private void loadShiftRegister() {

        shiftRecordList = applicationDAL.getShiftRecords("", DBHelper.COL_SHIFT_RECORD_START_TIME + " desc");

        ShiftRegisterAdapter shiftRegisterAdapter = new ShiftRegisterAdapter(this, shiftRecordList);
        listShiftRegister.setAdapter(shiftRegisterAdapter);

        if (shiftRecordList.size() == 0) {
            rlEmptyData.setVisibility(View.VISIBLE);
        } else {
            rlEmptyData.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shift, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_end_shift) {
            Intent intent = new Intent(this, EndShiftActivity.class);
            startActivityForResult(intent, ActivityRequest.REQUEST_SHIFT_END);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityRequest.REQUEST_SHIFT_END && resultCode == RESULT_OK) {
            loadShiftRegister();
        }
    }

}
