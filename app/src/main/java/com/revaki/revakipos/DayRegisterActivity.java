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

import com.revaki.revakipos.adapter.DayRegisterAdapter;
import com.revaki.revakipos.adapter.ShiftRegisterAdapter;
import com.revaki.revakipos.beans.DayRecord;
import com.revaki.revakipos.beans.ShiftRecord;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.db.DBHelper;
import com.revaki.revakipos.helper.ActivityRequest;
import com.revaki.revakipos.helper.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.List;

public class DayRegisterActivity extends AppCompatActivity {


    private ApplicationDAL applicationDAL;

    List<DayRecord> dayRecordList = null;

    private ListView listDayRegister;
    private RelativeLayout rlEmptyData;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_register);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Day Register");

        listDayRegister = findViewById(R.id.listDayRegister);
        rlEmptyData = findViewById(R.id.rlEmptyData);

        applicationDAL = new ApplicationDAL(this);

        listDayRegister.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DayRegisterActivity.this, PrintViewActivity.class);
                intent.putExtra("PrintType", "DayRegister");
                intent.putExtra("ShiftDate", sdf.format(dayRecordList.get(position).getShiftDate()));
                startActivityForResult(intent, ActivityRequest.REQUEST_PRINT_DAY_REGISTER);
            }
        });

        loadDayRegister();
    }

    private void loadDayRegister() {

        dayRecordList = applicationDAL.getDayRecords();

        DayRegisterAdapter dayRegisterAdapter = new DayRegisterAdapter(this, dayRecordList);
        listDayRegister.setAdapter(dayRegisterAdapter);

        if (dayRecordList.size() == 0) {
            rlEmptyData.setVisibility(View.VISIBLE);
        } else {
            rlEmptyData.setVisibility(View.GONE);
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
