package com.revaki.revakipos.job;

import android.content.Context;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.revaki.revakipos.beans.BankPromotion;
import com.revaki.revakipos.beans.CategoryDetail;
import com.revaki.revakipos.beans.Customer;
import com.revaki.revakipos.beans.DeliveryCompanyDetail;
import com.revaki.revakipos.beans.DishDetail;
import com.revaki.revakipos.beans.FloorDetail;
import com.revaki.revakipos.beans.ShiftTypeDetail;
import com.revaki.revakipos.beans.SyncDetail;
import com.revaki.revakipos.beans.TableDetail;
import com.revaki.revakipos.beans.UserDetail;
import com.revaki.revakipos.beans.WaiterDetail;
import com.revaki.revakipos.connectivity.ServiceManager;
import com.revaki.revakipos.db.ApplicationDAL;
import com.revaki.revakipos.db.DBHelper;
import com.revaki.revakipos.utils.BackgroundRequest;
import com.revaki.revakipos.widget.DataProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SyncMasterData {

    private DataProgressDialog dataProgressDialog;

    private Context context;
    private ServiceManager serviceManager = new ServiceManager();
    private ApplicationDAL applicationDAL;
    private OnSyncListener onSyncListener;

    public SyncMasterData(Context context) {
        this.context = context;

        applicationDAL = new ApplicationDAL(context);
    }

    public void sync(String placeId, String tableName) {
        SyncMasterDataTask(placeId, tableName);
    }

    public void setOnSyncListener(OnSyncListener listener) {
        this.onSyncListener = listener;
    }

    private void SyncMasterDataTask(final String PlaceId, final String TableName) {

        new BackgroundRequest<String, String, HashMap<String, Object>>() {

            protected void onPreExecute() {
                if (onSyncListener != null) {
                    onSyncListener.onSyncStart();
                }
                dataProgressDialog = new DataProgressDialog(context);
                dataProgressDialog.setTitle("Please wait...");
                dataProgressDialog.setCancelable(false);
                dataProgressDialog.show();
            }

            @Override
            protected HashMap<String, Object> doInBackground(String... params) {
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                boolean isSuccess = false;
                int errors = 0;

                JSONObject jObject;
                JSONArray content;
                Type type;
                Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
                boolean status = false;
                String message;

                List<UserDetail> userDetails = null;
                List<FloorDetail> floorDetails = null;
                List<TableDetail> tableDetails = null;
                List<CategoryDetail> categoryDetails = null;
                List<DishDetail> dishDetails = null;
                List<BankPromotion> bankPromotions = null;
                List<WaiterDetail> waiterDetails = null;
                List<ShiftTypeDetail> shiftTypeDetails = null;
                List<DeliveryCompanyDetail> deliveryCompanyDetails = null;
                List<Customer> customers = null;

                //Get and sync Users
                if (errors == 0 && (TableName.equals("") || TableName.equals(DBHelper.TABLE_USER_DETAIL))) {
                    try {
                        publishProgress("Users", "Running", "Getting Users...", "");
                        jObject = serviceManager.UserList(PlaceId);
                        publishProgress("Users", "Running", "Push Users...", "");

                        status = jObject.getBoolean("Status");
                        message = jObject.getString("Message");
                        if (status) {
                            content = jObject.getJSONArray("UserList");
                            type = new TypeToken<List<UserDetail>>() {
                            }.getType();

                            userDetails = gson.fromJson(content.toString(), type);
                            applicationDAL.pushUserDetail(userDetails);
                            applicationDAL.addUpdateSyncDetail(new SyncDetail(DBHelper.TABLE_USER_DETAIL, "Users", 1, 1, userDetails.size(), new Date()));

                            publishProgress("Users", "Completed", "Users Synced", "");

                        } else {
                            throw new Exception(message);
                        }
                    } catch (Exception e) {
                        errors++;
                        publishProgress("Users", "Failed", "Users Failed", e.toString());
                        e.printStackTrace();
                    }
                }

                //Get and sync Floors
                if (errors == 0 && (TableName.equals("") || TableName.equals(DBHelper.TABLE_FLOOR_DETAIL))) {
                    try {
                        publishProgress("Floors", "Running", "Getting Floors...", "");
                        jObject = serviceManager.FloorList(PlaceId);
                        publishProgress("Floors", "Running", "Push Floors...", "");

                        status = jObject.getBoolean("Status");
                        message = jObject.getString("Message");
                        if (status) {
                            content = jObject.getJSONArray("FloorList");
                            type = new TypeToken<List<FloorDetail>>() {
                            }.getType();

                            floorDetails = gson.fromJson(content.toString(), type);
                            applicationDAL.pushFloorDetail(floorDetails);
                            applicationDAL.addUpdateSyncDetail(new SyncDetail(DBHelper.TABLE_FLOOR_DETAIL, "Floors", 1, 2, floorDetails.size(), new Date()));

                            publishProgress("Floors", "Completed", "Floors Synced", "");

                        } else {
                            throw new Exception(message);
                        }
                    } catch (Exception e) {
                        errors++;
                        publishProgress("Floors", "Failed", "Floors Failed", e.toString());
                        e.printStackTrace();
                    }
                }

                //Get and sync Tables
                if (errors == 0 && (TableName.equals("") || TableName.equals(DBHelper.TABLE_TABLE_DETAIL))) {
                    try {
                        publishProgress("Tables", "Running", "Getting Tables...", "");
                        jObject = serviceManager.TableList(PlaceId);
                        publishProgress("Tables", "Running", "Push Tables...", "");

                        status = jObject.getBoolean("Status");
                        message = jObject.getString("Message");
                        if (status) {
                            content = jObject.getJSONArray("TableList");
                            type = new TypeToken<List<TableDetail>>() {
                            }.getType();

                            tableDetails = gson.fromJson(content.toString(), type);
                            applicationDAL.pushTableDetail(tableDetails);
                            applicationDAL.addUpdateSyncDetail(new SyncDetail(DBHelper.TABLE_TABLE_DETAIL, "Tables", 1, 3, tableDetails.size(), new Date()));

                            publishProgress("Tables", "Completed", "Tables Synced", "");

                        } else {
                            throw new Exception(message);
                        }
                    } catch (Exception e) {
                        errors++;
                        publishProgress("Tables", "Failed", "Tables Failed", e.toString());
                        e.printStackTrace();
                    }
                }

                //Get and sync Categories
                if (errors == 0 && (TableName.equals("") || TableName.equals(DBHelper.TABLE_CATEGORY_DETAIL))) {
                    try {
                        publishProgress("Categories", "Running", "Getting Categories...", "");
                        jObject = serviceManager.FoodCategoriesList(PlaceId);
                        publishProgress("Categories", "Running", "Push Categories...", "");

                        status = jObject.getBoolean("Status");
                        message = jObject.getString("Message");
                        if (status) {
                            content = jObject.getJSONArray("FoodCategories");
                            type = new TypeToken<List<CategoryDetail>>() {
                            }.getType();

                            categoryDetails = gson.fromJson(content.toString(), type);
                            applicationDAL.pushCategoryDetail(categoryDetails);
                            applicationDAL.addUpdateSyncDetail(new SyncDetail(DBHelper.TABLE_CATEGORY_DETAIL, "Categories", 1, 4, categoryDetails.size(), new Date()));

                            publishProgress("Categories", "Completed", "Categories Synced", "");

                        } else {
                            throw new Exception(message);
                        }
                    } catch (Exception e) {
                        errors++;
                        publishProgress("Categories", "Failed", "Categories Failed", e.toString());
                        e.printStackTrace();
                    }
                }


                //Get and sync Dishes
                if (errors == 0 && (TableName.equals("") || TableName.equals(DBHelper.TABLE_DISH_DETAIL))) {
                    try {
                        publishProgress("Dishes", "Running", "Getting Dishes...", "");
                        jObject = serviceManager.DishList(PlaceId);
                        publishProgress("Dishes", "Running", "Push Dishes...", "");

                        status = jObject.getBoolean("Status");
                        message = jObject.getString("Message");
                        if (status) {
                            content = jObject.getJSONArray("DishList");
                            type = new TypeToken<List<DishDetail>>() {
                            }.getType();

                            dishDetails = gson.fromJson(content.toString(), type);
                            applicationDAL.pushDishDetail(dishDetails);
                            applicationDAL.addUpdateSyncDetail(new SyncDetail(DBHelper.TABLE_DISH_DETAIL, "Dishes", 1, 5, dishDetails.size(), new Date()));

                            publishProgress("Dishes", "Completed", "Dishes Synced", "");

                        } else {
                            throw new Exception(message);
                        }
                    } catch (Exception e) {
                        errors++;
                        publishProgress("Dishes", "Failed", "Dishes Failed", e.toString());
                        e.printStackTrace();
                    }
                }

                //Get and sync Bank Promotions
                if (errors == 0 && (TableName.equals("") || TableName.equals(DBHelper.TABLE_BANK_PROMOTION))) {
                    try {
                        publishProgress("Promotions", "Running", "Getting Promotions...", "");
                        jObject = serviceManager.PromotionList(PlaceId);
                        publishProgress("Promotions", "Running", "Push Promotions...", "");

                        status = jObject.getBoolean("Status");
                        message = jObject.getString("Message");
                        if (status) {
                            content = jObject.getJSONArray("PromotionData");
                            type = new TypeToken<List<BankPromotion>>() {
                            }.getType();

                            bankPromotions = gson.fromJson(content.toString(), type);
                            applicationDAL.pushBankPromotion(bankPromotions);
                            applicationDAL.addUpdateSyncDetail(new SyncDetail(DBHelper.TABLE_BANK_PROMOTION, "Bank Promotions", 1, 6, bankPromotions.size(), new Date()));

                            publishProgress("Promotions", "Completed", "Promotions Synced", "");

                        } else {
                            throw new Exception(message);
                        }
                    } catch (Exception e) {
                        errors++;
                        publishProgress("Promotions", "Failed", "Promotions Failed", e.toString());
                        e.printStackTrace();
                    }
                }

                //Get and sync Waiter
                if (errors == 0 && (TableName.equals("") || TableName.equals(DBHelper.TABLE_WAITER))) {
                    try {
                        publishProgress("Waiters", "Running", "Getting Waiters...", "");
                        jObject = serviceManager.WaiterList(PlaceId);
                        publishProgress("Waiters", "Running", "Push Waiters...", "");

                        status = jObject.getBoolean("Status");
                        message = jObject.getString("Message");
                        if (status) {
                            content = jObject.getJSONArray("WaiterList");
                            type = new TypeToken<List<WaiterDetail>>() {
                            }.getType();

                            waiterDetails = gson.fromJson(content.toString(), type);
                            applicationDAL.pushWaiter(waiterDetails);
                            applicationDAL.addUpdateSyncDetail(new SyncDetail(DBHelper.TABLE_WAITER, "Waiters", 1, 7, waiterDetails.size(), new Date()));

                            publishProgress("Waiters", "Completed", "Waiters Synced", "");

                        } else {
                            throw new Exception(message);
                        }
                    } catch (Exception e) {
                        errors++;
                        publishProgress("Waiters", "Failed", "Waiters Failed", e.toString());
                        e.printStackTrace();
                    }
                }


                //Get and sync Customer
                if (errors == 0 && (TableName.equals("") || TableName.equals(DBHelper.TABLE_CUSTOMER))) {
                    try {
                        publishProgress("Customers", "Running", "Getting Customers...", "");
                        jObject = serviceManager.CustomerList(PlaceId);
                        publishProgress("Customers", "Running", "Push Customers...", "");

                        status = jObject.getBoolean("Status");
                        message = jObject.getString("Message");
                        if (status) {
                            content = jObject.getJSONArray("CustomerList");
                            type = new TypeToken<List<Customer>>() {
                            }.getType();

                            customers = gson.fromJson(content.toString(), type);
                            applicationDAL.pushCustomer(customers);
                            applicationDAL.addUpdateSyncDetail(new SyncDetail(DBHelper.TABLE_CUSTOMER, "Customers", 1, 8, customers.size(), new Date()));

                            publishProgress("Customers", "Completed", "Customers Synced", "");

                        } else {
                            throw new Exception(message);
                        }
                    } catch (Exception e) {
                        errors++;
                        publishProgress("Customers", "Failed", "Customers Failed", e.toString());
                        e.printStackTrace();
                    }
                }

                //Get and sync Master
                if (errors == 0 && (TableName.equals("") || TableName.equals(DBHelper.TABLE_SHIFT_TYPE) || TableName.equals(DBHelper.TABLE_DELIVERY_COMPANY))) {
                    try {
                        publishProgress("Masters", "Running", "Getting Masters...", "");
                        jObject = serviceManager.MasterList(PlaceId);
                        publishProgress("Masters", "Running", "Push Masters...", "");

                        status = jObject.getBoolean("Status");
                        message = jObject.getString("Message");
                        if (status) {
                            content = jObject.getJSONArray("ShiftTypeList");
                            type = new TypeToken<List<ShiftTypeDetail>>() {
                            }.getType();

                            shiftTypeDetails = gson.fromJson(content.toString(), type);
                            applicationDAL.pushShiftType(shiftTypeDetails);

                            content = jObject.getJSONArray("DeliveryCompanyList");
                            type = new TypeToken<List<DeliveryCompanyDetail>>() {
                            }.getType();

                            deliveryCompanyDetails = gson.fromJson(content.toString(), type);
                            applicationDAL.pushDeliveryCompany(deliveryCompanyDetails);
                            applicationDAL.addUpdateSyncDetail(new SyncDetail("Masters", "Masters", 1, 9, shiftTypeDetails.size() + deliveryCompanyDetails.size(), new Date()));

                            publishProgress("Masters", "Completed", "Masters Synced", "");

                        } else {
                            throw new Exception(message);
                        }
                    } catch (Exception e) {
                        errors++;
                        publishProgress("Masters", "Failed", "Masters Failed", e.toString());
                        e.printStackTrace();
                    }
                }


                isSuccess = (errors == 0);
                hashMap.put("IsSuccess", isSuccess);

                return hashMap;
            }

            @Override
            protected void onPostExecute(HashMap<String, Object> hashMap) {

                try {
                    dataProgressDialog.setCancelable(true);
                    dataProgressDialog.showPositiveButton();

                    boolean isSuccess = (Boolean) (hashMap.get("IsSuccess"));

                    if (isSuccess) {
                        dataProgressDialog.setTitle("Data Sync");
                    } else {
                        dataProgressDialog.setTitle("Data Sync Failed");
                    }

                    if (onSyncListener != null) {
                        onSyncListener.onSyncCompleted(isSuccess, TableName);
                    }
                } catch (Exception e) {
                    dataProgressDialog.showPositiveButton();
                    e.printStackTrace();
                }

            }

            @Override
            protected void onProgressUpdate(String... values) {
                String key = values[0];
                String progress = values[1];
                String message = values[2];
                String detail = values[3];

                dataProgressDialog.addDataProgressItem(key).setProgress(progress, message, detail);
            }
        }.execute();
    }


    public interface OnSyncListener {
        public void onSyncStart();

        public void onSyncCompleted(boolean isSuccess, String tableName);
    }
}
