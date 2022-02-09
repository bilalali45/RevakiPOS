package com.revaki.revakipos;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.revaki.revakipos.adapter.CartDetailAdapter;
import com.revaki.revakipos.beans.OrderChild;
import com.revaki.revakipos.beans.OrderMaster;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.helper.ActivityRequest;
import com.revaki.revakipos.helper.CommonUtils;
import com.revaki.revakipos.helper.UIHelper;
import com.revaki.revakipos.widget.IconTextView;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    String orderMasterId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderMasterId = getIntent().getStringExtra("OrderMasterId");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequest.REQUEST_CHECKOUT && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            intent.putExtra("OrderMasterId", data.getExtras().getString("OrderMasterId"));
            setResult(RESULT_OK, intent);
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("OrderMasterId", orderMasterId);
        setResult(RESULT_OK, intent);

        super.onBackPressed();
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
