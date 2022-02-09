package com.revaki.revakipos;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revaki.revakipos.beans.PlaceDetail;
import com.revaki.revakipos.beans.UserDetail;
import com.revaki.revakipos.connectivity.HttpWeb;
import com.revaki.revakipos.connectivity.ServiceManager;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.helper.UIHelper;
import com.revaki.revakipos.utils.BackgroundRequest;
import com.revaki.revakipos.utils.LocalDataManager;
import com.revaki.revakipos.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VerifyPINActivity extends AppCompatActivity {

    ProgressDialog dialog;

    ServiceManager serviceManager = new ServiceManager();
    private ApplicationDAL applicationDAL;

    private EditText etPin1;
    private EditText etPin2;
    private EditText etPin3;
    private EditText etPin4;

    private EditText etPrevious;
    private EditText etNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_pin);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etPin1 = (EditText) findViewById(R.id.etPin1);
        etPin2 = (EditText) findViewById(R.id.etPin2);
        etPin3 = (EditText) findViewById(R.id.etPin3);
        etPin4 = (EditText) findViewById(R.id.etPin4);

        etPrevious = etPin1;
        etNext = etPin1;


        applicationDAL = new ApplicationDAL(this);

        etPin1.setOnKeyListener(onKeyListener);
        etPin2.setOnKeyListener(onKeyListener);
        etPin3.setOnKeyListener(onKeyListener);
        etPin4.setOnKeyListener(onKeyListener);


        etPin1.setOnFocusChangeListener(onFocusChangeListener);
        etPin2.setOnFocusChangeListener(onFocusChangeListener);
        etPin3.setOnFocusChangeListener(onFocusChangeListener);
        etPin4.setOnFocusChangeListener(onFocusChangeListener);


        etPin1.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(VerifyPINActivity.this.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(etPin1, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 500);

        //VerifyPINTask("1234");
    }


    View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                etNext.requestFocus();
            } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL && ((EditText) view).getText().length() == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                etPrevious.setText("");
                etPrevious.requestFocus();
            } else if (view.equals(etPin4) && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                verifyPIN();
            }
            return false;
        }
    };

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {

                if (view.getId() == R.id.etPin1) {
                    etPrevious = etPin1;
                    etNext = etPin2;
                } else if (view.getId() == R.id.etPin2) {
                    etPrevious = etPin1;
                    etNext = etPin3;
                } else if (view.getId() == R.id.etPin3) {
                    etPrevious = etPin2;
                    etNext = etPin4;
                } else if (view.getId() == R.id.etPin4) {
                    etPrevious = etPin3;
                    etNext = etPin4;
                }
            }
        }
    };

    private void verifyPIN() {
        String PINCode = etPin1.getText().toString() + etPin2.getText().toString() + etPin3.getText().toString() + etPin4.getText().toString();
        if (PINCode.length() == 4) {
            if (HttpWeb.isConnectingToInternet(this)) {
                VerifyPINTask(PINCode);
            } else if (LocalDataManager.getInstance().getBoolean("IsSynced")) {
                offlineVerifyPIN(Integer.valueOf(PINCode));
            }
        }
    }

    private void offlineVerifyPIN(int PINCode) {
        if (LocalDataManager.getInstance().getBoolean("IsSynced")) {

            UserDetail userDetail = applicationDAL.getUserDetailByUsername(Configuration.getUser().getUsername());
            if (userDetail != null) {
                if (userDetail.getPosKey() == PINCode) {

                    Configuration.getUser().setUserId(userDetail.getId());

                    PlaceDetail savePlaceDetail = applicationDAL.getPlaceDetail(Configuration.getPlaceId());
                    Configuration.setPlaceDetail(savePlaceDetail);

                    SessionManager.getInstance().login(Configuration.getUser().getUsername(), Configuration.getUser().getPassword(), Configuration.getUser().getSessionUsername());

                    setResult(RESULT_OK);
                    finish();
                } else {
                    UIHelper.showErrorDialog(VerifyPINActivity.this, "", "Invalid pin code");
                }
            }
        }
    }

    public void btnSubmit_onClick(View view) {
        verifyPIN();
    }


    private void VerifyPINTask(final String PINCode) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(VerifyPINActivity.this);
                dialog.setTitle("Please wait");
                dialog.setMessage("Details Checking...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.VerifyPin(PINCode);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                dialog.dismiss();
                try {
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        JSONObject joUser = jsonObject.getJSONObject("UserData");
                        PlaceDetail placeDetail = gson.fromJson(jsonObject.getJSONObject("PlaceData").toString(), PlaceDetail.class);

                        List<PlaceDetail> placeDetailList = new ArrayList<PlaceDetail>();
                        placeDetailList.add(placeDetail);

                        applicationDAL.pushPlaceDetail(placeDetailList);

                        Configuration.getUser().setUserId(joUser.getString("Id"));
                        Configuration.setPlaceId(jsonObject.getString("PlaceId"));
                        PlaceDetail savePlaceDetail = applicationDAL.getPlaceDetail(Configuration.getPlaceId());
                        Configuration.setPlaceDetail(savePlaceDetail);

                        LocalDataManager.getInstance().putString("PlaceId", Configuration.getPlaceId());
                        SessionManager.getInstance().login(Configuration.getUser().getUsername(), Configuration.getUser().getPassword(),Configuration.getUser().getSessionUsername());

                        setResult(RESULT_OK);
                        finish();
                    } else {
                        UIHelper.showErrorDialog(VerifyPINActivity.this, "", jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    offlineVerifyPIN(Integer.valueOf(PINCode));
                    e.printStackTrace();
                }

            }
        }.execute();
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
