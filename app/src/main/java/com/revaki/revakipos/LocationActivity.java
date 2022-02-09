package com.revaki.revakipos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
    }

    public void   btnSubmit_onClick(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
