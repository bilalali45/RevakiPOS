package com.revaki.revakipos;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.revaki.revakipos.beans.ShiftRecord;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.helper.UIHelper;
import com.revaki.revakipos.utils.SessionManager;

import java.util.Date;

public class EndShiftActivity extends AppCompatActivity implements View.OnClickListener {

    private ShiftRecord shiftRecord;
    private ApplicationDAL applicationDAL;
    private EditText etComments;
    private Button btnSubmit;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_shift);
        initViews();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("End Shift");

        if (Configuration.getShiftRecordId().equals("") == false) {
            applicationDAL = new ApplicationDAL(this);
            shiftRecord = applicationDAL.getShiftRecord(Configuration.getShiftRecordId());
        } else {
            UIHelper.showShortToast(this, "Shift must be started.");
            finish();
        }
    }

    private void initViews() {
        etComments = findViewById(R.id.etComments);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                finish();
                break;
            case R.id.btnSubmit:
                finishShift();
                break;
        }
    }

    private void finishShift() {
        int transactionCount = applicationDAL.getShiftActiveTransactionCount(Configuration.getShiftRecordId());
        if (transactionCount == 0) {
            shiftRecord.setFinishTime(new Date());
            shiftRecord.setComments(etComments.getText().toString());
            shiftRecord.setStatusId(2);
            applicationDAL.addUpdateShiftRecord(shiftRecord);
            Configuration.setShiftRecordId("");
            setResult(RESULT_OK);
            finish();
            //logout();
        } else {
            UIHelper.showErrorDialog(this, "", "Some transaction are active, shift will be finish after close all transactions.");
        }
    }

    private void logout() {
        SessionManager.getInstance().logout();
        Configuration.setLogin(false);
        Configuration.setOfflineLogin(false);
        Intent intent = new Intent(EndShiftActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

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
