package com.revaki.revakipos;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.revaki.revakipos.beans.ShiftRecord;
import com.revaki.revakipos.beans.User;
import com.revaki.revakipos.beans.UserDetail;
import com.revaki.revakipos.connectivity.HttpWeb;
import com.revaki.revakipos.connectivity.ServiceManager;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.helper.ActivityRequest;
import com.revaki.revakipos.helper.CommonUtils;
import com.revaki.revakipos.helper.UIHelper;
import com.revaki.revakipos.utils.BackgroundRequest;
import com.revaki.revakipos.utils.LocalDataManager;
import com.revaki.revakipos.utils.SessionManager;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    private LinearLayout lyLoading;
    private ConstraintLayout clMainWindow;

    ServiceManager serviceManager = new ServiceManager();
    private ApplicationDAL applicationDAL;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lyLoading = findViewById(R.id.lyLoading);
        clMainWindow = findViewById(R.id.clMainWindow);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        applicationDAL = new ApplicationDAL(this);
        user = new User();

        if (SessionManager.getInstance().isLoggedIn()) {
            etUsername.setText(SessionManager.getInstance().getSessionUsername());
            etPassword.setText(SessionManager.getInstance().getPassword());
            login();
        }
    }

    public void btnLogin_onClick(View view) {
        login();
    }

    private void login() {
        if (etUsername.getText().length() != 0 && etPassword.getText().length() != 0) {

            String username = etUsername.getText().toString();
            if (username.endsWith("@revakiadmin")) {
                username = username.replace("@revakiadmin", "");
            }
            user.setUsername(username);
            user.setPassword(etPassword.getText().toString());
            user.setSessionUsername(etUsername.getText().toString());

            if (HttpWeb.isConnectingToInternet(this)) {
                UserLoginTask();
            //} else if (LocalDataManager.getInstance().getBoolean("IsSynced")) {
            //    OfflineLogin();
            } else {
                Snackbar
                        .make(etUsername, "No network connection.", Snackbar.LENGTH_LONG)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                login();
                            }
                        }).show();
            }
        } else {
            UIHelper.showErrorDialog(this, "", "please enter username and password");
        }
    }


    private void UserLoginTask() {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                clMainWindow.setVisibility(View.GONE);
                lyLoading.setVisibility(View.VISIBLE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.Login(user.getUsername(), user.getPassword());

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    final boolean status = jsonObject.getBoolean("Status");
                    final int statusCode = jsonObject.getInt("StatusCode");
                    if (status) {

                        JSONObject joUser = jsonObject.getJSONObject("UserData");
                        String userPlaceId = joUser.getString("PlaceId");

                        if (Configuration.getPlaceId() == null || userPlaceId.equals(Configuration.getPlaceId())) {

                            user.setFirstName(joUser.getString("FirstName"));
                            user.setLastName(joUser.getString("LastName"));

                            String token = joUser.getString("Token");

                            ServiceManager.setToken(token);
                            LocalDataManager.getInstance().putString("APIToken", token);

                            Configuration.setCurrentDate(CommonUtils.parseDate(jsonObject.getString("CurrentDate"),"dd-MMM-yyyy"));
                            Configuration.setUser(user);
                            Configuration.setLogin(true);
                            Configuration.setOfflineLogin(false);

                            Intent intent = new Intent(LoginActivity.this, VerifyPINActivity.class);
                            startActivityForResult(intent, ActivityRequest.REQUEST_VERIFY_PIN);

                        } else {
                            clMainWindow.setVisibility(View.VISIBLE);
                            UIHelper.showErrorDialog(LoginActivity.this, "", "Other place data found you cannot login with this user.");
                        }

                    } else {
                        clMainWindow.setVisibility(View.VISIBLE);
                        UIHelper.showErrorDialog(LoginActivity.this, "", jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    clMainWindow.setVisibility(View.VISIBLE);
                    OfflineLogin();
                    e.printStackTrace();
                }

                lyLoading.setVisibility(View.GONE);
            }
        }.execute();
    }

    private void OfflineLogin() {
        if (LocalDataManager.getInstance().getBoolean("IsSynced")) {

            UserDetail userDetail = applicationDAL.getUserDetailByUsername(user.getUsername());
            if (userDetail != null) {
                if (userDetail.getPassword().equals(user.getPassword())) {
                    user.setFirstName(userDetail.getUserName());
                    user.setLastName(userDetail.getLastName());

                    Configuration.setUser(user);
                    Configuration.setLogin(true);
                    Configuration.setOfflineLogin(true);

                    Intent intent = new Intent(LoginActivity.this, VerifyPINActivity.class);
                    startActivityForResult(intent, ActivityRequest.REQUEST_VERIFY_PIN);
                } else {
                    UIHelper.showErrorDialog(LoginActivity.this, "", "Invalid username or password");
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        clMainWindow.setVisibility(View.VISIBLE);

        if (requestCode == ActivityRequest.REQUEST_LOGIN && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        } else if (requestCode == ActivityRequest.REQUEST_VERIFY_PIN && resultCode == RESULT_OK) {
            ShiftRecord shiftRecord = applicationDAL.getLastShiftRecordByUserId(Configuration.getUserId());
            if (shiftRecord != null) {
                Configuration.setShiftRecordId(shiftRecord.getShiftRecordId());
            }
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivityForResult(intent, ActivityRequest.REQUEST_LOGIN);
        } else if (requestCode == ActivityRequest.REQUEST_SHIFT_START && resultCode == RESULT_OK) {
            String shiftRecordId = data.getExtras().getString("ShiftRecordId");
            if (shiftRecordId.equals("")) {
                UIHelper.showShortToast(LoginActivity.this, "Shift must be started.");
            } else {
                Configuration.setShiftRecordId(shiftRecordId);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivityForResult(intent, ActivityRequest.REQUEST_LOGIN);
            }
        }
    }
}
