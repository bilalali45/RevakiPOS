package com.revaki.revakipos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.revaki.revakipos.connectivity.ServiceManager;
import com.revaki.revakipos.helper.ActivityRequest;
import com.revaki.revakipos.utils.LocalDataManager;
import com.revaki.revakipos.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadApp();
    }

    private void loadApp() {
        if (checkPermission()) {
            LocalDataManager.createInstance(this);
            SessionManager.createInstance(this);

            Configuration.setAppLoaded(LocalDataManager.getInstance().getBoolean("AppLoaded"));

            if (Configuration.isAppLoaded() == false) {
                //MyFirebaseMessagingService.createNotificationChannel(this);
                //FirebaseMessaging.getInstance().subscribeToTopic("revaki-all");
                //FirebaseMessaging.getInstance().subscribeToTopic("revaki-android");
                LocalDataManager.getInstance().putBoolean("AppLoaded", true);
            }

            String APIToken = LocalDataManager.getInstance().getString("APIToken");
            if (APIToken != null) {
                ServiceManager.setToken(APIToken);
            }
            String PlaceId = LocalDataManager.getInstance().getString("PlaceId");
            Configuration.setPlaceId(PlaceId);

            tvTitle = (TextView) findViewById(R.id.tvTitle);

            Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            fadeIn.setDuration(1500);
            fadeIn.setFillAfter(true);

            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    gotoNextScreen();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            tvTitle.startAnimation(fadeIn);
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, ActivityRequest.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadApp();
        } else {
            finish();
        }
    }

    private void gotoNextScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
