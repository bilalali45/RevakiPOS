package com.revaki.revakipos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.revaki.revakipos.helper.UIHelper;
import com.revaki.revakipos.widget.IconTextView;

public class EditCartQuantityPriceActivity extends AppCompatActivity {

    TextView tvTitle;
    EditText etValue;
    TextView tvDescription;
    private TextView lbl_0;
    private TextView lbl_1;
    private TextView lbl_2;
    private TextView lbl_3;
    private TextView lbl_4;
    private TextView lbl_5;
    private TextView lbl_6;
    private TextView lbl_7;
    private TextView lbl_8;
    private TextView lbl_9;
    private IconTextView tvDelete;
    private IconTextView tvDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cart_quantity_price);

        getSupportActionBar().setTitle("Edit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tvTitle);
        etValue = findViewById(R.id.etValue);
        tvDescription = findViewById(R.id.tvDescription);
        lbl_0 = findViewById(R.id.lbl_0);
        lbl_1 = findViewById(R.id.lbl_1);
        lbl_2 = findViewById(R.id.lbl_2);
        lbl_3 = findViewById(R.id.lbl_3);
        lbl_4 = findViewById(R.id.lbl_4);
        lbl_5 = findViewById(R.id.lbl_5);
        lbl_6 = findViewById(R.id.lbl_6);
        lbl_7 = findViewById(R.id.lbl_7);
        lbl_8 = findViewById(R.id.lbl_8);
        lbl_9 = findViewById(R.id.lbl_9);
        tvDelete = findViewById(R.id.tvDelete);
        tvDone = findViewById(R.id.tvDone);

        String Title = getIntent().getStringExtra("Title");
        String value = getIntent().getStringExtra("Value");

        tvTitle.setText(Title);
        etValue.setText(value);

        etValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        lbl_0.setOnClickListener(onKeypadClickListener);
        lbl_1.setOnClickListener(onKeypadClickListener);
        lbl_2.setOnClickListener(onKeypadClickListener);
        lbl_3.setOnClickListener(onKeypadClickListener);
        lbl_4.setOnClickListener(onKeypadClickListener);
        lbl_5.setOnClickListener(onKeypadClickListener);
        lbl_6.setOnClickListener(onKeypadClickListener);
        lbl_7.setOnClickListener(onKeypadClickListener);
        lbl_8.setOnClickListener(onKeypadClickListener);
        lbl_9.setOnClickListener(onKeypadClickListener);

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etValue.getText().length() > 0) {
                    etValue.setText(etValue.getText().toString().substring(0, etValue.getText().length() - 1));
                }
            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etValue.getText().length() > 0 && Float.valueOf(etValue.getText().toString()) > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("Value", etValue.getText().toString());
                    intent.putExtra("Position", getIntent().getIntExtra("Position", 0));
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    UIHelper.showShortToast(EditCartQuantityPriceActivity.this, "Invalid quantity.");
                }
            }
        });


    }

    private View.OnClickListener onKeypadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView textView = (TextView) view;
            etValue.setText(etValue.getText().toString() + textView.getText().toString());
        }
    };


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
