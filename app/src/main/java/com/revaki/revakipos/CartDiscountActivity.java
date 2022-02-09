package com.revaki.revakipos;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.revaki.revakipos.helper.CommonUtils;

public class CartDiscountActivity extends AppCompatActivity {

    private EditText etDiscountAmount;
    private EditText etDiscountPercentage;
    private TextView tvTitle;
    private TextView tvAmount;
    private TextView tvNetAmount;
    private Button btnApplyDiscount;
    private RadioButton rbAmount;
    private RadioButton rbPercentage;
    private int discountTypeId = 1;

    float amount = 0;
    int Position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_discount);

        getSupportActionBar().setTitle("Discount");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etDiscountAmount = findViewById(R.id.etDiscountAmount);
        etDiscountPercentage = findViewById(R.id.etDiscountPercentage);
        tvTitle = findViewById(R.id.tvTitle);
        tvAmount = findViewById(R.id.tvAmount);
        tvNetAmount = findViewById(R.id.tvNetAmount);
        btnApplyDiscount = findViewById(R.id.btnApplyDiscount);
        rbAmount = findViewById(R.id.rbAmount);
        rbAmount.setChecked(true);
        rbPercentage = findViewById(R.id.rbPercentage);

        tvNetAmount.setPaintFlags(tvNetAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        String Title = getIntent().getStringExtra("Title");
        String DiscountAmount = getIntent().getStringExtra("DiscountAmount");
        String DiscountPercentage = getIntent().getStringExtra("DiscountPercentage");
        int DiscountType = getIntent().getIntExtra("DiscountTypeId", 1);
        amount = Float.valueOf(getIntent().getStringExtra("Amount"));

        if (getIntent().hasExtra("Position")) {
            Position = getIntent().getIntExtra("Position", 0);
        }


        tvTitle.setText(Title);
        etDiscountAmount.setText(DiscountAmount);
        etDiscountPercentage.setText(DiscountPercentage);
        tvAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(amount));
        tvNetAmount.setText("");

        if (DiscountType == 2) {
            rbPercentage.setChecked(true);
        }

        if (amount > 0) {
            calculateDiscount(1);
        } else {
            etDiscountAmount.setEnabled(false);
            etDiscountPercentage.setEnabled(false);
        }

        etDiscountAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                if (etDiscountAmount.hasFocus()) {
                    calculateDiscount(1);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etDiscountPercentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                if (etDiscountPercentage.hasFocus()) {
                    calculateDiscount(2);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnApplyDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("DiscountAmount", CommonUtils.parseTwoDecimal(etDiscountAmount.getText().toString()));
                intent.putExtra("DiscountPercentage", CommonUtils.parseTwoDecimal(etDiscountPercentage.getText().toString()));
                intent.putExtra("DiscountTypeId", discountTypeId);
                if (getIntent().hasExtra("Position")) {
                    intent.putExtra("Position", Position);
                }
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        rbAmount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rbAmount.isChecked()) {
                    discountTypeId = 1;
                }
            }
        });

        rbPercentage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rbPercentage.isChecked()) {
                    discountTypeId = 2;
                }
            }
        });

    }

    private void calculateDiscount(int mode) {
        try {
            if (mode == 1) {
                float discountAmount = Float.valueOf("0" + etDiscountAmount.getText().toString());
                if (discountAmount == 0) {
                    etDiscountPercentage.setText("");
                    tvAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(amount));
                    tvNetAmount.setText("");
                } else {

                    if (discountAmount > amount) {
                        discountAmount = amount;
                        etDiscountAmount.setText(String.valueOf(amount));
                    }

                    float percentage = (discountAmount / amount) * 100;

                    etDiscountPercentage.setText(String.format("%.2f", percentage));
                    tvAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(amount - discountAmount));
                    tvNetAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(amount));

                }

            } else if (mode == 2) {
                float percentage = Float.valueOf("0" + etDiscountPercentage.getText().toString());

                if (percentage == 0) {
                    etDiscountAmount.setText("");
                    tvAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(amount));
                    tvNetAmount.setText("");
                } else {

                    if (percentage > 100) {
                        percentage = 100;
                        etDiscountPercentage.setText("100");
                    }
                    float discountAmount = (amount / 100) * percentage;

                    etDiscountAmount.setText(String.format("%.2f", discountAmount));
                    tvAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(amount - discountAmount));
                    tvNetAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(amount));
                }
            }

        } catch (Exception e) {

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
