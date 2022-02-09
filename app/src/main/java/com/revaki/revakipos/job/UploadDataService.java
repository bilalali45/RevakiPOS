package com.revaki.revakipos.job;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.revaki.revakipos.Configuration;
import com.revaki.revakipos.beans.OrderMaster;
import com.revaki.revakipos.beans.ShiftRecord;
import com.revaki.revakipos.beans.SyncDetail;
import com.revaki.revakipos.beans.User;
import com.revaki.revakipos.beans.UserDetail;
import com.revaki.revakipos.connectivity.HttpWeb;
import com.revaki.revakipos.connectivity.ServiceManager;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.db.DBHelper;
import com.revaki.revakipos.db.DataRow;
import com.revaki.revakipos.helper.CommonUtils;
import com.revaki.revakipos.utils.LocalDataManager;
import com.revaki.revakipos.utils.Logger;
import com.revaki.revakipos.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UploadDataService extends IntentService {

    public static final String ACTION = "com.revaki.revakipos.job.UploadDataService";
    public static final int RESULT_STARTED = 101;
    public static final int RESULT_COMPLETED = 102;
    public static final int RESULT_FAILED = 103;
    public static final int AUTO_START = 201;
    public static final int MANUAL_START = 202;

    static int resultCode;
    static int startCode;
    static String tableName;
    static boolean isRunning = false;


    public UploadDataService() {
        super("UploadDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isRunning) {
            isRunning = true;
            try {
                resultCode = RESULT_STARTED;
                startCode = intent.getIntExtra("startCode", 0);
                tableName = intent.getStringExtra("tableName");
                String syncDetail = "";
                int errors = 0;

                ApplicationDAL applicationDAL = new ApplicationDAL(this);
                int uploadLimit = Configuration.getUploadingEntriesLimit();
                String uploadIds = "";

                boolean isLogin = Configuration.isLogin();
                LocalDataManager.createInstance(this);

                if (isLogin == false) {
                    SessionManager.createInstance(this);
                    if (SessionManager.getInstance().isLoggedIn()) {

                        UserDetail userDetail = applicationDAL.getUserDetailByUsername(SessionManager.getInstance().getUsername());
                        if (userDetail != null) {

                            User user = new User();
                            user.setUserId(userDetail.getId());
                            user.setFirstName(userDetail.getFirstName());
                            user.setLastName(userDetail.getLastName());
                            user.setUsername(userDetail.getUserName());
                            user.setSessionUsername(SessionManager.getInstance().getSessionUsername());
                            user.setPassword(userDetail.getPassword());

                            Configuration.setUser(user);

                            String PlaceId = LocalDataManager.getInstance().getString("PlaceId");
                            Configuration.setPlaceId(PlaceId);

                            isLogin = true;
                        }
                    }
                }
                if (Configuration.getUser().getUsername().equals(Configuration.getUser().getSessionUsername())) {
                    if (isLogin) {
                        String lastUploadedOn = LocalDataManager.getInstance().getString("LastUploadedOn");

                        Intent in = new Intent(ACTION);
                        in.putExtra("resultCode", resultCode);
                        in.putExtra("startCode", startCode);
                        in.putExtra("syncDetail", syncDetail);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(in);

                        try {
                            if (HttpWeb.isConnectingToInternet(this)) {

                                ServiceManager serviceManager = new ServiceManager();


                                DataRow dataRow = applicationDAL.getSyncDataDetail();

                                JSONObject jObject;
                                Type type;
                                Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                                String json = "";
                                boolean status = false;

                                ContentValues contentValues;

                                int allSyncDataCount = 0;

                                allSyncDataCount = dataRow.getInt("order_master_count") + dataRow.getInt("shift_record_count");

                                if (allSyncDataCount > 0) {

                                    //Sync order master
                                    if (dataRow.getInt("order_master_count") > 0 && (tableName == null || tableName.equals(DBHelper.TABLE_ORDER_MASTER))) {
                                        try {
                                            contentValues = new ContentValues();
                                            contentValues.put(DBHelper.COL_ORDER_MASTER_SEND_STATUS_ID, 1);
                                            applicationDAL.executeUpdate(DBHelper.TABLE_ORDER_MASTER, contentValues, DBHelper.COL_ORDER_MASTER_STATUS_ID + " = 2 and " + DBHelper.COL_ORDER_MASTER_SEND_STATUS_ID + " = 0");

                                            List<OrderMaster> orderMasters = applicationDAL.getOrderMasters(DBHelper.COL_ORDER_MASTER_SEND_STATUS_ID + " = 1", "");
                                            List<OrderMaster> sendOrderMasters = new ArrayList<OrderMaster>();

                                            type = new TypeToken<List<OrderMaster>>() {
                                            }.getType();

                                            for (int uploadIndex = 0; uploadIndex < orderMasters.size(); uploadIndex++) {
                                                sendOrderMasters.add(orderMasters.get(uploadIndex));
                                                uploadIds += "'" + orderMasters.get(uploadIndex).getOrderMasterId() + "',";

                                                if (sendOrderMasters.size() == uploadLimit || (uploadIndex == orderMasters.size() - 1)) {

                                                    json = gson.toJson(sendOrderMasters, type);

                                                    JSONObject jObj = new JSONObject();
                                                    jObj.put("OrderMasters", new JSONArray(json));
                                                    jObj.put("PlaceId", Configuration.getPlaceId());
                                                    jObj.put("UserId", Configuration.getUserId());

                                                    jObject = serviceManager.PostOrderMaster(jObj.toString());

                                                    status = jObject.getBoolean("Status");
                                                    if (status) {
                                                        uploadIds += "''";
                                                        applicationDAL.updateOrderMasterUploaded(uploadIds);

                                                        applicationDAL.addUpdateSyncDetail(new SyncDetail(DBHelper.TABLE_ORDER_MASTER, "Orders", 2, 10, 0, new Date()));
                                                    } else {
                                                        throw new Exception("Service not responding");
                                                    }
                                                    sendOrderMasters = new ArrayList<OrderMaster>();
                                                    uploadIds = "";
                                                }
                                            }
                                        } catch (Exception e) {
                                            errors++;
                                            e.printStackTrace();
                                        }
                                    }

                                    //Sync Shift Record
                                    if (dataRow.getInt("shift_record_count") > 0 && (tableName == null || tableName.equals(DBHelper.TABLE_SHIFT_RECORD))) {
                                        try {
                                            contentValues = new ContentValues();
                                            contentValues.put(DBHelper.COL_SHIFT_RECORD_SEND_STATUS_ID, 1);
                                            applicationDAL.executeUpdate(DBHelper.TABLE_SHIFT_RECORD, contentValues, DBHelper.COL_SHIFT_RECORD_STATUS_ID + " = 2 and " + DBHelper.COL_SHIFT_RECORD_SEND_STATUS_ID + " = 0");

                                            List<ShiftRecord> shiftRecords = applicationDAL.getShiftRecords(DBHelper.COL_SHIFT_RECORD_SEND_STATUS_ID + " = 1", "");

                                            type = new TypeToken<List<ShiftRecord>>() {
                                            }.getType();

                                            json = gson.toJson(shiftRecords, type);

                                            JSONObject jObj = new JSONObject();
                                            jObj.put("ShiftRecords", new JSONArray(json));
                                            jObj.put("PlaceId", Configuration.getPlaceId());
                                            jObj.put("UserId", Configuration.getUserId());

                                            jObject = serviceManager.PostShiftRecord(jObj.toString());

                                            status = jObject.getBoolean("Status");
                                            if (status) {
                                                contentValues = new ContentValues();
                                                contentValues.put(DBHelper.COL_SHIFT_RECORD_SEND_STATUS_ID, 2);
                                                applicationDAL.executeUpdate(DBHelper.TABLE_SHIFT_RECORD, contentValues, DBHelper.COL_SHIFT_RECORD_SEND_STATUS_ID + " = 1");
                                                applicationDAL.addUpdateSyncDetail(new SyncDetail(DBHelper.TABLE_SHIFT_RECORD, "Shift Records", 2, 11, 0, new Date()));
                                            } else {
                                                throw new Exception("Service not responding");
                                            }
                                        } catch (Exception e) {
                                            errors++;
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                try {
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(Configuration.getCurrentDate());
                                    cal.add(Calendar.MONTH, -3);

                                    applicationDAL.deleteSyncData(cal.getTime());
                                } catch (Exception e) {
                                    Logger.writeError(e);
                                    e.printStackTrace();
                                }

                                jObject = serviceManager.HeartBeat(Configuration.getPlaceId(), Configuration.getUserId(), CommonUtils.extractDate(new Date(), "yyyy-MM-dd HH:mm:ss"));

                            } else {
                                errors++;
                                syncDetail = "No network connection.";
                            }
                            if (errors == 0) {
                                Date syncedOn = new Date();
                                LocalDataManager.getInstance().putString("LastUploadedOn", CommonUtils.extractDate(syncedOn, "dd/MM/yyyy hh:mm aa"));
                                syncDetail = "Data has been synced.";
                                resultCode = RESULT_COMPLETED;
                            } else {
                                syncDetail = "Data sync failed.";
                                resultCode = RESULT_FAILED;
                            }

                        } catch (Exception e) {
                            Logger.writeError(e);
                            e.printStackTrace();
                            syncDetail = "Data sync failed.";
                            resultCode = RESULT_FAILED;
                        }

                        in.putExtra("resultCode", resultCode);
                        in.putExtra("startCode", startCode);
                        in.putExtra("syncDetail", syncDetail);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(in);

                    }
                }
                isRunning = false;
                stopSelf();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
