package com.revaki.revakipos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.revaki.revakipos.beans.SyncDetail;
import com.revaki.revakipos.connectivity.HttpWeb;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.db.DBHelper;
import com.revaki.revakipos.db.DataRow;
import com.revaki.revakipos.helper.CommonUtils;
import com.revaki.revakipos.helper.UIHelper;
import com.revaki.revakipos.job.SyncMasterData;
import com.revaki.revakipos.job.UploadDataService;
import com.revaki.revakipos.utils.LocalDataManager;
import com.revaki.revakipos.utils.SessionManager;
import com.revaki.revakipos.widget.IconTextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class SyncDataActivity extends AppCompatActivity {

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");

    private ApplicationDAL applicationDAL;

    private Button btnSyncAll;
    private ListView listMasterTable;

    private SyncMasterData syncMasterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_data);

        getSupportActionBar().setTitle("Sync Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSyncAll = findViewById(R.id.btnSyncAll);
        listMasterTable = findViewById(R.id.listMasterTable);

        applicationDAL = new ApplicationDAL(this);

        syncMasterData = new SyncMasterData(this);


        btnSyncAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sync(1, "");
            }
        });

        syncMasterData.setOnSyncListener(new SyncMasterData.OnSyncListener() {
            @Override
            public void onSyncStart() {
                lockScreenOrientation();
            }

            @Override
            public void onSyncCompleted(boolean isSuccess, String tableName) {
                unlockScreenOrientation();
                if (isSuccess) {
                    loadSyncData();
                }
                if (tableName.equals("")) {
                    Intent serviceIntent = new Intent(SyncDataActivity.this, UploadDataService.class);
                    serviceIntent.putExtra("startCode", UploadDataService.MANUAL_START);
                    startService(serviceIntent);
                }
            }
        });

        loadSyncData();
    }

    private void sync(int tableType, String tableName) {
        if (HttpWeb.isConnectingToInternet(this)) {
            if (tableType == 1) {
                syncMasterData.sync(Configuration.getPlaceId(), tableName);
            } else if (tableType == 2) {
                Intent serviceIntent = new Intent(SyncDataActivity.this, UploadDataService.class);
                serviceIntent.putExtra("startCode", UploadDataService.MANUAL_START);
                serviceIntent.putExtra("tableName", tableName);
                startService(serviceIntent);
            }
        } else {
            UIHelper.showShortToast(this, "No network connection.");
        }
    }

    private void loadSyncData() {
        List<SyncDetail> syncDetailList = applicationDAL.getSyncDetails("", DBHelper.COL_SYNC_DETAIL_TABLE_ORDER);

        SyncDetailAdapter syncDetailAdapter = new SyncDetailAdapter(this, syncDetailList);
        listMasterTable.setAdapter(syncDetailAdapter);
    }

    public class SyncDetailAdapter extends BaseAdapter {
        Context context;
        private List<SyncDetail> syncDetailList;

        public SyncDetailAdapter(Context context, List<SyncDetail> syncDetailList) {
            this.context = context;
            this.syncDetailList = syncDetailList;
        }

        @Override
        public int getCount() {
            return syncDetailList.size();
        }

        @Override
        public Object getItem(int position) {
            return syncDetailList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.sync_detail_row, null);

                TextView tvTableTitle = convertView.findViewById(R.id.tvTableTitle);
                TextView tvRecordCount = convertView.findViewById(R.id.tvRecordCount);
                TextView tvLastSyncedOn = convertView.findViewById(R.id.tvLastSyncedOn);
                IconTextView btnSync = convertView.findViewById(R.id.btnSync);

                viewHolder = new ViewHolder(tvTableTitle, tvRecordCount, tvLastSyncedOn, btnSync);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvTableTitle.setText(syncDetailList.get(position).getTableTitle());
            viewHolder.tvRecordCount.setText("No. Of Records : " + CommonUtils.formatTwoDecimal(syncDetailList.get(position).getRecordCount()));
            viewHolder.tvLastSyncedOn.setText("Last Synced On : " + sdf.format(syncDetailList.get(position).getLastSyncedOn()));

          viewHolder.btnSync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (syncDetailList.get(position).getTableType() == 1) {
                        sync(syncDetailList.get(position).getTableType(), syncDetailList.get(position).getTableName());
                    } else {
                        sync(syncDetailList.get(position).getTableType(), syncDetailList.get(position).getTableName());
                    }
                }
            });

            if (syncDetailList.get(position).getTableType() == 1) {
                viewHolder.tvRecordCount.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvRecordCount.setVisibility(View.GONE);
            }
            return convertView;
        }


        public class ViewHolder {
            TextView tvTableTitle;
            TextView tvRecordCount;
            TextView tvLastSyncedOn;
            IconTextView btnSync;

            public ViewHolder(TextView tvTableTitle, TextView tvRecordCount, TextView tvLastSyncedOn, IconTextView btnSync) {
                this.tvTableTitle = tvTableTitle;
                this.tvRecordCount = tvRecordCount;
                this.tvLastSyncedOn = tvLastSyncedOn;
                this.btnSync = btnSync;
            }
        }
    }


    private void lockScreenOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void unlockScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    private void clearData() {
        DataRow dataRow = applicationDAL.getSyncDataDetail();
        int allSyncDataCount = dataRow.getInt("order_master_count") + dataRow.getInt("shift_record_count");
        if (allSyncDataCount == 0) {
            UIHelper.showConfirmDialog(this, "Clear data", "Are you sure to clear data?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    applicationDAL.clearData();

                    SessionManager.getInstance().logout();
                    LocalDataManager.getInstance().clear();

                    Configuration.setLogin(false);
                    Configuration.setOfflineLogin(false);

                    setResult(RESULT_OK);
                    finish();
                }
            });
        } else {
            if (HttpWeb.isConnectingToInternet(this)) {
                UIHelper.showConfirmDialog(this, "Upload", "Some records are not uploaded, do you want to upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent serviceIntent = new Intent(SyncDataActivity.this, UploadDataService.class);
                        serviceIntent.putExtra("startCode", UploadDataService.MANUAL_START);
                        startService(serviceIntent);
                    }
                });
            } else {
                UIHelper.showShortToast(this, "No network connection.");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sync_data_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_clear_data) {
            clearData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
