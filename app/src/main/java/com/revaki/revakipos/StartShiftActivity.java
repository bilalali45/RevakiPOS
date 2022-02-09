package com.revaki.revakipos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.revaki.revakipos.beans.ShiftRecord;
import com.revaki.revakipos.beans.ShiftTypeDetail;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.helper.CommonUtils;
import com.revaki.revakipos.helper.UIHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class StartShiftActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etBalanceAmount;
    private TextView tvStartedBy;
    private TextView tvTime;
    private LinearLayout lyShiftType;
    private Button btnSubmit;
    private ApplicationDAL applicationDAL;
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy hh:mm aa");

    private TextView selectedShiftType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_shift);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Start Shift");

        initViews();

        if (Configuration.getShiftRecordId().equals("")) {
            if (Configuration.getPlaceDetail().getStartShiftDefaultAmount().length() > 0 && Configuration.getPlaceDetail().getStartShiftDefaultAmount().equals("0") == false) {
                etBalanceAmount.setText(Configuration.getPlaceDetail().getStartShiftDefaultAmount());
            }
            List<ShiftTypeDetail> shiftTypeDetails = applicationDAL.getShiftTypes("", "");

            for (ShiftTypeDetail shiftTypeDetail : shiftTypeDetails) {

                TextView tvShiftType = new TextView(this);
                tvShiftType.setBackgroundResource(R.drawable.bg_gray_corner);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 10, 10, 10);
                tvShiftType.setLayoutParams(params);
                tvShiftType.setText(shiftTypeDetail.getShiftType());
                tvShiftType.setTag(shiftTypeDetail.getId());

                tvShiftType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView textView = (TextView) view;

                        if (selectedShiftType != null) {
                            selectedShiftType.setBackgroundResource(R.drawable.bg_gray_corner);
                        }
                        selectedShiftType = textView;
                        selectedShiftType.setBackgroundResource(R.drawable.bg_gray_corner_filled);
                    }
                });

                lyShiftType.addView(tvShiftType);
            }

            if (lyShiftType.getChildCount() > 0) {
                selectedShiftType = (TextView) lyShiftType.getChildAt(0);
                selectedShiftType.setBackgroundResource(R.drawable.bg_gray_corner_filled);
            }
        } else {
            UIHelper.showShortToast(this, "Shift already started.");
            finish();
        }

    }

    private void addShiftRecord() {
        if (Configuration.getShiftRecordId().equals("")) {
            ShiftRecord shiftRecord = new ShiftRecord();
            shiftRecord.setOpeningCash(CommonUtils.parseTwoDecimal(etBalanceAmount.getText().toString()));
            shiftRecord.setShiftDate(CommonUtils.getDate());
            shiftRecord.setStartTime(new Date());
            shiftRecord.setUserId(Configuration.getUserId());
            shiftRecord.setStatusId(1);

            if (selectedShiftType != null) {
                shiftRecord.setShiftTypeId(selectedShiftType.getTag().toString());
            }

            applicationDAL.addUpdateShiftRecord(shiftRecord);

            Configuration.setShiftRecordId(shiftRecord.getShiftRecordId());

            Intent intent = new Intent();
            intent.putExtra("ShiftRecordId", shiftRecord.getShiftRecordId());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            UIHelper.showShortToast(this, "Shift already started.");
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("ShiftRecordId", "");
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initViews() {
        applicationDAL = new ApplicationDAL(this);
        etBalanceAmount = findViewById(R.id.etBalanceAmount);
        tvStartedBy = findViewById(R.id.tvStartedBy);
        tvTime = findViewById(R.id.tvTime);
        lyShiftType = findViewById(R.id.lyShiftType);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        tvStartedBy.setText(Configuration.getUser().getUsername());
        tvTime.setText(sdf.format(new Date()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                if (!etBalanceAmount.getText().equals("")) {
                    addShiftRecord();
                } else {
                    UIHelper.showErrorDialog(StartShiftActivity.this, "", "Balance cant be empty. ");
                }
                break;
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
