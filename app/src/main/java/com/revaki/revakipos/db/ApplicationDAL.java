package com.revaki.revakipos.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.revaki.revakipos.beans.BankPromotion;
import com.revaki.revakipos.beans.CategoryDetail;
import com.revaki.revakipos.beans.Customer;
import com.revaki.revakipos.beans.DayRecord;
import com.revaki.revakipos.beans.DeliveryCompanyDetail;
import com.revaki.revakipos.beans.DishDetail;
import com.revaki.revakipos.beans.DishPrintDetail;
import com.revaki.revakipos.beans.FloorDetail;
import com.revaki.revakipos.beans.OrderCardDetail;
import com.revaki.revakipos.beans.OrderChild;
import com.revaki.revakipos.beans.OrderMaster;
import com.revaki.revakipos.beans.ShiftTypeDetail;
import com.revaki.revakipos.beans.SyncDetail;
import com.revaki.revakipos.beans.PlaceDetail;
import com.revaki.revakipos.beans.PrinterDetail;
import com.revaki.revakipos.beans.SettingDetail;
import com.revaki.revakipos.beans.ShiftRecord;
import com.revaki.revakipos.beans.TableDetail;
import com.revaki.revakipos.beans.UserDetail;
import com.revaki.revakipos.beans.WaiterDetail;
import com.revaki.revakipos.helper.SpinnerItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ApplicationDAL {

    private SQLiteDatabase database;
    private DBHelper dBHelper;
    private static int pushChunkSize = 500;

    public ApplicationDAL(Context context) {
        this.dBHelper = new DBHelper(context);
    }

    public static int getPushChunkSize() {
        return pushChunkSize;
    }

    public static void setPushChunkSize(int pushChunkSize) {
        if (pushChunkSize > 0) {
            ApplicationDAL.pushChunkSize = pushChunkSize;
        }
    }

    public void openDBConnection() throws SQLException {
        database = dBHelper.getWritableDatabase();
    }

    public void closeDBConnection() throws SQLException {
        database.close();
    }


    public boolean pushPlaceDetail(List<PlaceDetail> placeDetailList) {

        openDBConnection();

        int rows = 0;
        String strqry = "insert or replace into " + DBHelper.TABLE_PLACE_DETAIL + " (" +
                DBHelper.COL_PLACE_DETAIL_PLACE_ID + ", " +
                DBHelper.COL_PLACE_DETAIL_TITLE + ", " +
                DBHelper.COL_PLACE_DETAIL_CONTACT_INFO + ", " +
                DBHelper.COL_PLACE_DETAIL_ADDRESS + ", " +
                DBHelper.COL_PLACE_DETAIL_EMAIL + ", " +
                DBHelper.COL_PLACE_DETAIL_PHONE + ", " +
                DBHelper.COL_PLACE_DETAIL_WEBSITE + ", " +
                DBHelper.COL_PLACE_DETAIL_PRINT_LOGO_NAME + ", " +
                DBHelper.COL_PLACE_DETAIL_PRINT_LOGO_IMAGE + ", " +
                DBHelper.COL_PLACE_DETAIL_STRN + ", " +
                DBHelper.COL_PLACE_DETAIL_NTN + ", " +
                DBHelper.COL_PLACE_DETAIL_GST_PERCENTAGE + ", " +
                DBHelper.COL_PLACE_DETAIL_CARD_GST_PERCENTAGE + ", " +
                DBHelper.COL_PLACE_DETAIL_BOTTOM_TEXT + ", " +
                DBHelper.COL_PLACE_DETAIL_UAN_NUMBER + ", " +
                DBHelper.COL_PLACE_DETAIL_DELIVERY_CHARGES + ", " +
                DBHelper.COL_PLACE_DETAIL_GST_TITLE + ", " +
                DBHelper.COL_PLACE_DETAIL_GST_DEDUCTION_TYPE + ", " +
                DBHelper.COL_PLACE_DETAIL_GST_DEDUCTION_ON_FULL_DISCOUNT + ", " +
                DBHelper.COL_PLACE_DETAIL_START_SHIFT_DEFAULT_AMOUNT + ", " +
                DBHelper.COL_PLACE_DETAIL_ALLOW_TABLE_MULTIPLE_RECEIPTS + ", " +
                DBHelper.COL_PLACE_DETAIL_FREE_DELIVERY_AFTER_AMOUNT + ", " +
                DBHelper.COL_PLACE_DETAIL_PRINT_KITCHEN_SUMMARY + ") values";

        StringBuilder sb = new StringBuilder();
        sb.append(strqry);
        for (PlaceDetail placeDetail : placeDetailList) {
            sb.append("('");
            sb.append(placeDetail.getPlaceId() + "','");
            sb.append(placeDetail.getTitle().replace("'", "''") + "','");
            sb.append(placeDetail.getContactInfo() + "','");
            sb.append(placeDetail.getAddress().replace("'", "''") + "','");
            sb.append(placeDetail.getEmail() + "','");
            sb.append(placeDetail.getPhone() + "','");
            sb.append(placeDetail.getWebsite() + "','");
            sb.append(placeDetail.getPrintLogoName().replace("'", "''") + "','");
            sb.append(placeDetail.getPrintLogoImage().replace("'", "''") + "','");
            sb.append(placeDetail.getSTRN() + "','");
            sb.append(placeDetail.getNTN() + "','");
            sb.append(placeDetail.getGSTPercentage() + "','");
            sb.append(placeDetail.getCardGSTPercentage() + "','");
            sb.append(placeDetail.getBottomText().replace("'", "''") + "','");
            sb.append(placeDetail.getUANNumber() + "','");
            sb.append(placeDetail.getDeliveryCharges() + "','");
            sb.append(placeDetail.getGSTTitle() + "',");
            sb.append(placeDetail.getGSTDeductionType() + ",");
            sb.append(placeDetail.getGSTDeductionOnFullDiscount() + ",'");
            sb.append(placeDetail.getStartShiftDefaultAmount() + "',");
            sb.append((placeDetail.isAllowTableMultipleReceipts() == true ? 1 : 0) + ",");
            sb.append(placeDetail.getFreeDeliveryAfterAmount() + ",");
            sb.append((placeDetail.isPrintKitchenSummary() == true ? 1 : 0) + "),");
            rows++;
            if (rows == getPushChunkSize()) {
                rows = 0;
                sb.deleteCharAt(sb.length() - 1);
                database.execSQL(sb.toString());
                sb = new StringBuilder();
                sb.append(strqry);
            }
        }
        if (rows > 0) {
            sb.deleteCharAt(sb.length() - 1);
            database.execSQL(sb.toString());
        }
        closeDBConnection();
        return true;
    }

    public boolean pushUserDetail(List<UserDetail> userDetailList) {
        openDBConnection();

        database.execSQL(DBHelper.DROP_TABLE_USER_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_USER_DETAIL);

        int rows = 0;
        String strqry = "insert or replace into " + DBHelper.TABLE_USER_DETAIL + " (" +
                DBHelper.COL_USER_DETAIL_USER_ID + ", " +
                DBHelper.COL_USER_DETAIL_FIRST_NAME + ", " +
                DBHelper.COL_USER_DETAIL_LAST_NAME + ", " +
                DBHelper.COL_USER_DETAIL_USERNAME + ", " +
                DBHelper.COL_USER_DETAIL_PASSWORD + ", " +
                DBHelper.COL_USER_DETAIL_HASH_PASSWORD + ", " +
                DBHelper.COL_USER_DETAIL_HASH_POS_KEY + ", " +
                DBHelper.COL_USER_DETAIL_HASH_MODIFICATION_KEY + ") values ";
        StringBuilder sb = new StringBuilder();
        sb.append(strqry);
        for (UserDetail userDetail : userDetailList) {
            sb.append("('");
            sb.append(userDetail.getId() + "','");
            sb.append(userDetail.getFirstName() + "','");
            sb.append(userDetail.getLastName() + "','");
            sb.append(userDetail.getUserName() + "','");
            sb.append(userDetail.getPassword() + "','");
            sb.append(userDetail.getHashPassword() + "',");
            sb.append(userDetail.getPosKey() + ",");
            sb.append(userDetail.getModificationKey() + "),");
            rows++;
            if (rows == getPushChunkSize()) {
                rows = 0;
                sb.deleteCharAt(sb.length() - 1);
                database.execSQL(sb.toString());
                sb = new StringBuilder();
                sb.append(strqry);
            }
        }
        if (rows > 0) {
            sb.deleteCharAt(sb.length() - 1);
            database.execSQL(sb.toString());
        }
        closeDBConnection();
        return true;
    }


    public boolean pushFloorDetail(List<FloorDetail> floorDetailList) {
        openDBConnection();

        database.execSQL(DBHelper.DROP_TABLE_FLOOR_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_FLOOR_DETAIL);

        int rows = 0;
        String strqry = "insert or replace into " + DBHelper.TABLE_FLOOR_DETAIL + " (" +
                DBHelper.COL_FLOOR_DETAIL_FLOOR_ID + ", " +
                DBHelper.COL_FLOOR_DETAIL_FLOOR_NAME + ", " +
                DBHelper.COL_FLOOR_DETAIL_DESCRIPTION + ", " +
                DBHelper.COL_FLOOR_DETAIL_ORDER_TYPE_ID + ", " +
                DBHelper.COL_FLOOR_DETAIL_IS_SHOW_TABLE + ") values ";
        StringBuilder sb = new StringBuilder();
        sb.append(strqry);
        for (FloorDetail floorDetail : floorDetailList) {
            sb.append("('");
            sb.append(floorDetail.getFloorId() + "','");
            sb.append(floorDetail.getFloorName() + "','");
            sb.append(floorDetail.getDiscription() + "',");
            sb.append(floorDetail.getOrderTypeId() + ",");
            sb.append((floorDetail.isShowTable() == true ? 1 : 0) + "),");
            rows++;
            if (rows == getPushChunkSize()) {
                rows = 0;
                sb.deleteCharAt(sb.length() - 1);
                database.execSQL(sb.toString());
                sb = new StringBuilder();
                sb.append(strqry);
            }
        }
        if (rows > 0) {
            sb.deleteCharAt(sb.length() - 1);
            database.execSQL(sb.toString());
        }
        closeDBConnection();
        return true;
    }

    public boolean pushTableDetail(List<TableDetail> tableDetailList) {
        openDBConnection();

        database.execSQL(DBHelper.DROP_TABLE_TABLE_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_TABLE_DETAIL);

        int rows = 0;
        String strqry = "insert or replace into " + DBHelper.TABLE_TABLE_DETAIL + " (" +
                DBHelper.COL_TABLE_DETAIL_TABLE_ID + ", " +
                DBHelper.COL_TABLE_DETAIL_TABLE_NAME + ", " +
                DBHelper.COL_TABLE_DETAIL_CAPACITY + ", " +
                DBHelper.COL_TABLE_DETAIL_FLOOR_ID + ", " +
                DBHelper.COL_TABLE_DETAIL_DESCRIPTION + ") values ";
        StringBuilder sb = new StringBuilder();
        sb.append(strqry);
        for (TableDetail tableDetail : tableDetailList) {
            sb.append("('");
            sb.append(tableDetail.getTableId() + "','");
            sb.append(tableDetail.getTableName() + "',");
            sb.append(tableDetail.getCapacity() + ",'");
            sb.append(tableDetail.getFloorId() + "','");
            sb.append(tableDetail.getDiscription() + "'),");
            rows++;
            if (rows == getPushChunkSize()) {
                rows = 0;
                sb.deleteCharAt(sb.length() - 1);
                database.execSQL(sb.toString());
                sb = new StringBuilder();
                sb.append(strqry);
            }
        }
        if (rows > 0) {
            sb.deleteCharAt(sb.length() - 1);
            database.execSQL(sb.toString());
        }
        closeDBConnection();
        return true;
    }

    public boolean pushCategoryDetail(List<CategoryDetail> categoryDetailList) {
        openDBConnection();

        database.execSQL(DBHelper.DROP_TABLE_CATEGORY_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_CATEGORY_DETAIL);

        int rows = 0;
        String strqry = "insert or replace into " + DBHelper.TABLE_CATEGORY_DETAIL + " (" +
                DBHelper.COL_CATEGORY_DETAIL_CATEGORY_ID + ", " +
                DBHelper.COL_CATEGORY_DETAIL_CATEGORY_NAME + ", " +
                DBHelper.COL_CATEGORY_DETAIL_IMAGE_URL + ", " +
                DBHelper.COL_CATEGORY_DETAIL_TYPE + ", " +
                DBHelper.COL_CATEGORY_DETAIL_DESCRIPTION + ") values ";
        StringBuilder sb = new StringBuilder();
        sb.append(strqry);
        for (CategoryDetail categoryDetail : categoryDetailList) {
            sb.append("('");
            sb.append(categoryDetail.getCategoryId() + "','");
            sb.append(categoryDetail.getCategoryName().replace("'", "''") + "','");
            sb.append(categoryDetail.getImageURL() + "','");
            sb.append(categoryDetail.getType() + "','");
            sb.append(categoryDetail.getDiscription() + "'),");
            rows++;
            if (rows == getPushChunkSize()) {
                rows = 0;
                sb.deleteCharAt(sb.length() - 1);
                database.execSQL(sb.toString());
                sb = new StringBuilder();
                sb.append(strqry);
            }
        }
        if (rows > 0) {
            sb.deleteCharAt(sb.length() - 1);
            database.execSQL(sb.toString());
        }
        closeDBConnection();
        return true;
    }


    public boolean pushDishDetail(List<DishDetail> dishDetailList) {
        openDBConnection();

        database.execSQL(DBHelper.DROP_TABLE_DISH_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_DISH_DETAIL);

        int rows = 0;
        String strqry = "insert or replace into " + DBHelper.TABLE_DISH_DETAIL + " (" +
                DBHelper.COL_DISH_DETAIL_DISH_ID + ", " +
                DBHelper.COL_DISH_DETAIL_DISH_NAME + ", " +
                DBHelper.COL_DISH_DETAIL_CATEGORY_ID + ", " +
                DBHelper.COL_DISH_DETAIL_IMAGE_URL + ", " +
                DBHelper.COL_DISH_DETAIL_BARCODE + ", " +
                DBHelper.COL_DISH_DETAIL_IN_MINUTE + ", " +
                DBHelper.COL_DISH_DETAIL_IN_SECOND + ", " +
                DBHelper.COL_DISH_DETAIL_WEIGHT_IN_GRAM + ", " +
                DBHelper.COL_DISH_DETAIL_TOTAL_PRICE + ", " +
                DBHelper.COL_DISH_DETAIL_PRICE_START_FROM + ", " +
                DBHelper.COL_DISH_DETAIL_APPLY_GST + ", " +
                DBHelper.COL_DISH_DETAIL_APPLY_DISCOUNT + ", " +
                DBHelper.COL_DISH_DETAIL_DESCRIPTION + ", " +
                DBHelper.COL_DISH_DETAIL_VARIANT_DATA + ") values ";
        StringBuilder sb = new StringBuilder();
        sb.append(strqry);
        for (DishDetail dishDetail : dishDetailList) {
            sb.append("('");
            sb.append(dishDetail.getDishId() + "','");
            sb.append(dishDetail.getDishName().replace("'", "''") + "','");
            sb.append(dishDetail.getCategoryId() + "','");
            sb.append(dishDetail.getImageURL() + "','");
            sb.append(dishDetail.getBarCode() + "',");
            sb.append(dishDetail.getInMinut() + ",");
            sb.append(dishDetail.getInSec() + ",'");
            sb.append(dishDetail.getWeightInGram() + "',");
            sb.append(dishDetail.getTotalPrice() + ",'");
            sb.append(dishDetail.getPriceStartFrom() + "',");
            sb.append((dishDetail.isApplyGST() == true ? 1 : 0) + ",");
            sb.append((dishDetail.isApplyDiscount() == true ? 1 : 0) + ",'");
            sb.append(dishDetail.getDiscription() + "','");
            sb.append(dishDetail.getVariantData() + "'),");
            rows++;
            if (rows == getPushChunkSize()) {
                rows = 0;
                sb.deleteCharAt(sb.length() - 1);
                database.execSQL(sb.toString());
                sb = new StringBuilder();
                sb.append(strqry);
            }
        }
        if (rows > 0) {
            sb.deleteCharAt(sb.length() - 1);
            database.execSQL(sb.toString());
        }
        closeDBConnection();
        return true;
    }


    public boolean pushBankPromotion(List<BankPromotion> bankPromotionList) {
        openDBConnection();

        database.execSQL(DBHelper.DROP_TABLE_BANK_PROMOTION);
        database.execSQL(DBHelper.CREATE_TABLE_BANK_PROMOTION);

        int rows = 0;
        String strqry = "insert or replace into " + DBHelper.TABLE_BANK_PROMOTION + " (" +
                DBHelper.COL_BANK_PROMOTION_BANK_DETAIL_ID + ", " +
                DBHelper.COL_BANK_PROMOTION_BANK_NAME + ", " +
                DBHelper.COL_BANK_DISCOUNT_PERCENTAGE + ", " +
                DBHelper.COL_BANK_DISCOUNT_AMOUNT + ", " +
                DBHelper.COL_BANK_START_DATE + ", " +
                DBHelper.COL_BANK_END_DATE + ") values ";
        StringBuilder sb = new StringBuilder();
        sb.append(strqry);
        for (BankPromotion bankPromotion : bankPromotionList) {
            sb.append("('");
            sb.append(bankPromotion.getBankDetailId() + "','");
            sb.append(bankPromotion.getBankName() + "',");
            sb.append(bankPromotion.getDiscountPercentage() + ",");
            sb.append(bankPromotion.getDiscountAmount() + ",'");
            sb.append(bankPromotion.getStartDate() + "','");
            sb.append(bankPromotion.getEndDate() + "'),");
            rows++;
            if (rows == getPushChunkSize()) {
                rows = 0;
                sb.deleteCharAt(sb.length() - 1);
                database.execSQL(sb.toString());
                sb = new StringBuilder();
                sb.append(strqry);
            }
        }
        if (rows > 0) {
            sb.deleteCharAt(sb.length() - 1);
            database.execSQL(sb.toString());
        }
        closeDBConnection();
        return true;
    }

    public boolean pushWaiter(List<WaiterDetail> waiterDetailList) {
        openDBConnection();

        database.execSQL(DBHelper.DROP_TABLE_WAITER);
        database.execSQL(DBHelper.CREATE_TABLE_WAITER);

        int rows = 0;
        String strqry = "insert or replace into " + DBHelper.TABLE_WAITER + " (" +
                DBHelper.COL_WAITER_WAITER_ID + ", " +
                DBHelper.COL_WAITER_WAITER_NAME + ") values ";
        StringBuilder sb = new StringBuilder();
        sb.append(strqry);
        for (WaiterDetail waiterDetail : waiterDetailList) {
            sb.append("('");
            sb.append(waiterDetail.getWaiterId() + "','");
            sb.append(waiterDetail.getWaiterName() + "'),");
            rows++;
            if (rows == getPushChunkSize()) {
                rows = 0;
                sb.deleteCharAt(sb.length() - 1);
                database.execSQL(sb.toString());
                sb = new StringBuilder();
                sb.append(strqry);
            }
        }
        if (rows > 0) {
            sb.deleteCharAt(sb.length() - 1);
            database.execSQL(sb.toString());
        }
        closeDBConnection();
        return true;
    }


    public boolean pushShiftType(List<ShiftTypeDetail> shiftTypeDetailList) {
        openDBConnection();

        database.execSQL(DBHelper.DROP_TABLE_SHIFT_TYPE);
        database.execSQL(DBHelper.CREATE_TABLE_SHIFT_TYPE);

        int rows = 0;
        String strqry = "insert or replace into " + DBHelper.TABLE_SHIFT_TYPE + " (" +
                DBHelper.COL_SHIFT_TYPE_SHIFT_TYPE_ID + ", " +
                DBHelper.COL_SHIFT_TYPE_SHIFT_TYPE_NAME + ") values ";
        StringBuilder sb = new StringBuilder();
        sb.append(strqry);
        for (ShiftTypeDetail shiftTypeDetail : shiftTypeDetailList) {
            sb.append("('");
            sb.append(shiftTypeDetail.getId() + "','");
            sb.append(shiftTypeDetail.getShiftType() + "'),");
            rows++;
            if (rows == getPushChunkSize()) {
                rows = 0;
                sb.deleteCharAt(sb.length() - 1);
                database.execSQL(sb.toString());
                sb = new StringBuilder();
                sb.append(strqry);
            }
        }
        if (rows > 0) {
            sb.deleteCharAt(sb.length() - 1);
            database.execSQL(sb.toString());
        }
        closeDBConnection();
        return true;
    }

    public boolean pushDeliveryCompany(List<DeliveryCompanyDetail> deliveryCompanyList) {
        openDBConnection();

        database.execSQL(DBHelper.DROP_TABLE_DELIVERY_COMPANY);
        database.execSQL(DBHelper.CREATE_TABLE_DELIVERY_COMPANY);

        int rows = 0;
        String strqry = "insert or replace into " + DBHelper.TABLE_DELIVERY_COMPANY + " (" +
                DBHelper.COL_DELIVERY_COMPANY_DELIVERY_COMPANY_ID + ", " +
                DBHelper.COL_DELIVERY_COMPANY_DELIVERY_COMPANY_NAME + ") values ";
        StringBuilder sb = new StringBuilder();
        sb.append(strqry);
        for (DeliveryCompanyDetail deliveryCompanyDetail : deliveryCompanyList) {
            sb.append("('");
            sb.append(deliveryCompanyDetail.getId() + "','");
            sb.append(deliveryCompanyDetail.getDeliveryCompany() + "'),");
            rows++;
            if (rows == getPushChunkSize()) {
                rows = 0;
                sb.deleteCharAt(sb.length() - 1);
                database.execSQL(sb.toString());
                sb = new StringBuilder();
                sb.append(strqry);
            }
        }
        if (rows > 0) {
            sb.deleteCharAt(sb.length() - 1);
            database.execSQL(sb.toString());
        }
        closeDBConnection();
        return true;
    }


    public boolean pushCustomer(List<Customer> customerList) {
        openDBConnection();

        database.execSQL(DBHelper.DROP_TABLE_CUSTOMER);
        database.execSQL(DBHelper.CREATE_TABLE_CUSTOMER);

        int rows = 0;
        String strqry = "insert or replace into " + DBHelper.TABLE_CUSTOMER + " (" +
                DBHelper.COL_CUSTOMER_CUSTOMER_ID + ", " +
                DBHelper.COL_CUSTOMER_FULL_NAME + ", " +
                DBHelper.COL_CUSTOMER_CONTACT_NO + ", " +
                DBHelper.COL_CUSTOMER_ADDRESS + ", " +
                DBHelper.COL_CUSTOMER_REWARD_BALANCE + ") values ";
        StringBuilder sb = new StringBuilder();
        sb.append(strqry);
        for (Customer customer : customerList) {
            sb.append("('");
            sb.append(customer.getId() + "','");
            sb.append(customer.getFullName() + "','");
            sb.append(customer.getContactNo() + "','");
            sb.append(customer.getAddress() + "','");
            sb.append(customer.getRewardBalance() + "'),");
            rows++;
            if (rows == getPushChunkSize()) {
                rows = 0;
                sb.deleteCharAt(sb.length() - 1);
                database.execSQL(sb.toString());
                sb = new StringBuilder();
                sb.append(strqry);
            }
        }
        if (rows > 0) {
            sb.deleteCharAt(sb.length() - 1);
            database.execSQL(sb.toString());
        }
        closeDBConnection();
        return true;
    }


    public boolean pushDishPrintDetail(List<DishPrintDetail> dishPrintDetails) {
        openDBConnection();

        database.execSQL(DBHelper.DROP_TABLE_DISH_PRINT_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_DISH_PRINT_DETAIL);

        int rows = 0;
        String strqry = "insert or replace into " + DBHelper.TABLE_DISH_PRINT_DETAIL + " (" +
                DBHelper.COL_DISH_PRINT_DETAIL_DISH_ID + ", " +
                DBHelper.COL_DISH_PRINT_DETAIL_CATEGORY_ID + ", " +
                DBHelper.COL_DISH_PRINT_DETAIL_PRINTER_IP + ", " +
                DBHelper.COL_DISH_PRINT_DETAIL_PORT + ") values";
        StringBuilder sb = new StringBuilder();
        sb.append(strqry);
        for (DishPrintDetail dishPrintDetail : dishPrintDetails) {
            sb.append("('");
            sb.append(dishPrintDetail.getDishId() + "','");
            sb.append(dishPrintDetail.getCategoryId() + "','");
            sb.append(dishPrintDetail.getPrinterIp() + "','");
            sb.append(dishPrintDetail.getPort() + "),");
            rows++;
            if (rows == getPushChunkSize()) {
                rows = 0;
                sb.deleteCharAt(sb.length() - 1);
                database.execSQL(sb.toString());
                sb = new StringBuilder();
                sb.append(strqry);
            }
        }
        if (rows > 0) {
            sb.deleteCharAt(sb.length() - 1);
            database.execSQL(sb.toString());
        }
        closeDBConnection();
        return true;
    }

    public SettingDetail getSettingDetail() {

        SettingDetail settingDetail = null;

        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_SETTING_DETAIL;

        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {

            settingDetail = new SettingDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_SETTING_DETAIL_SETTING_DETAIL_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SETTING_DETAIL_DEFAULT_PRINTER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SETTING_DETAIL_KITCHEN_PRINTER_ID)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SETTING_DETAIL_KITCHEN_PRINT_COPY)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SETTING_DETAIL_KITCHEN_PRINTER_CATEGORIES)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SETTING_DETAIL_SHOW_PRE_BILL_PREVIEW)) == 1,
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SETTING_DETAIL_SHOW_POST_BILL_PREVIEW)) == 1,
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SETTING_DETAIL_SHOW_KITCHEN_PRINT_PREVIEW)) == 1,
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SETTING_DETAIL_AUTO_PRINT_AFTER_CHECKOUT)) == 1,
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SETTING_DETAIL_OPEN_CASH_DRAWER_AFTER_CHECKOUT)) == 1);
        } else {
            settingDetail = new SettingDetail(null, "", "", 1, "{}", true, true, true, false, false);
            addUpdateSettingDetail(settingDetail);
        }
        closeDBConnection();
        return settingDetail;
    }

    public List<SyncDetail> getSyncDetails(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<SyncDetail> syncDetails = new ArrayList<SyncDetail>();
        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_SYNC_DETAIL + criteria + orderBy;
        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            syncDetails.add(new SyncDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_SYNC_DETAIL_TABLE_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SYNC_DETAIL_TABLE_TITLE)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SYNC_DETAIL_TABLE_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SYNC_DETAIL_TABLE_ORDER)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SYNC_DETAIL_RECORD_COUNT)),
                    new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_SYNC_DETAIL_LAST_SYNCED_ON)))));
            cursor.moveToNext();
        }
        closeDBConnection();
        return syncDetails;
    }


    public PlaceDetail getPlaceDetail(String PlaceDetailId) {

        PlaceDetail placeDetail = null;

        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_PLACE_DETAIL +
                " where " + DBHelper.COL_PLACE_DETAIL_PLACE_ID + " = '" + PlaceDetailId + "'";

        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {

            placeDetail = new PlaceDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_PLACE_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_TITLE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_CONTACT_INFO)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_PHONE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_WEBSITE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_PRINT_LOGO_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_PRINT_LOGO_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_STRN)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_NTN)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_GST_PERCENTAGE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_CARD_GST_PERCENTAGE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_BOTTOM_TEXT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_UAN_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_DELIVERY_CHARGES)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_GST_TITLE)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_GST_DEDUCTION_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_GST_DEDUCTION_ON_FULL_DISCOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_START_SHIFT_DEFAULT_AMOUNT)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_ALLOW_TABLE_MULTIPLE_RECEIPTS)) == 1,
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_FREE_DELIVERY_AFTER_AMOUNT)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_PLACE_DETAIL_PRINT_KITCHEN_SUMMARY)) == 1);
        }
        closeDBConnection();
        return placeDetail;
    }


    public List<FloorDetail> getFloorDetails(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<FloorDetail> floorDetails = new ArrayList<FloorDetail>();
        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_FLOOR_DETAIL + criteria + orderBy;
        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            floorDetails.add(new FloorDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_FLOOR_DETAIL_FLOOR_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_FLOOR_DETAIL_FLOOR_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_FLOOR_DETAIL_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_FLOOR_DETAIL_ORDER_TYPE_ID)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_FLOOR_DETAIL_IS_SHOW_TABLE)) == 1));
            cursor.moveToNext();
        }
        closeDBConnection();
        return floorDetails;
    }

    public List<TableDetail> getTableDetails(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<TableDetail> tableDetails = new ArrayList<TableDetail>();
        openDBConnection();
        String strqry = "select *,(select count(*) from order_master where order_master.status_id=1 and order_master.table_id=table_detail.table_id) active_bill_count from " + DBHelper.TABLE_TABLE_DETAIL + criteria + orderBy;
        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            tableDetails.add(new TableDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_TABLE_DETAIL_TABLE_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_TABLE_DETAIL_TABLE_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_TABLE_DETAIL_CAPACITY)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_TABLE_DETAIL_FLOOR_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_TABLE_DETAIL_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndex("active_bill_count"))));
            cursor.moveToNext();
        }
        closeDBConnection();
        return tableDetails;
    }

    public TableDetail getTableDetail(String TableId) {
        TableDetail tableDetail = null;

        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_TABLE_DETAIL +
                " where " + DBHelper.COL_TABLE_DETAIL_TABLE_ID + " = '" + TableId + "'";

        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {

            tableDetail = new TableDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_TABLE_DETAIL_TABLE_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_TABLE_DETAIL_TABLE_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_TABLE_DETAIL_CAPACITY)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_TABLE_DETAIL_FLOOR_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_TABLE_DETAIL_DESCRIPTION)),
                    0);
        }
        closeDBConnection();
        return tableDetail;
    }


    public List<CategoryDetail> getCategoryDetails(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<CategoryDetail> categoryDetails = new ArrayList<CategoryDetail>();
        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_CATEGORY_DETAIL + criteria + orderBy;
        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            categoryDetails.add(new CategoryDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_CATEGORY_DETAIL_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_CATEGORY_DETAIL_CATEGORY_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_CATEGORY_DETAIL_TYPE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_CATEGORY_DETAIL_IMAGE_URL)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_CATEGORY_DETAIL_DESCRIPTION))));
            cursor.moveToNext();
        }
        closeDBConnection();
        return categoryDetails;
    }

    public List<DishDetail> getDishDetails(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<DishDetail> dishDetails = new ArrayList<DishDetail>();
        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_DISH_DETAIL + criteria + orderBy;
        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            dishDetails.add(new DishDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_DISH_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_DISH_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_IMAGE_URL)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_BARCODE)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_IN_MINUTE)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_IN_SECOND)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_WEIGHT_IN_GRAM)),
                    cursor.getDouble(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_TOTAL_PRICE)),
                    cursor.getDouble(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_PRICE_START_FROM)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_APPLY_GST)) == 1,
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_APPLY_DISCOUNT)) == 1,
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_VARIANT_DATA))));
            cursor.moveToNext();
        }
        closeDBConnection();
        return dishDetails;
    }

    public List<DishDetail> getDishDetailsWithCategory(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<DishDetail> dishDetails = new ArrayList<DishDetail>();
        DishDetail dishDetail = null;
        openDBConnection();
        String strqry = "select dish_id,dish_name,dish_detail.category_id,category_name,dish_detail.image_url,barcode,minute,second,weight_in_gram,total_price,price_start_from,apply_gst,apply_discount,dish_detail.discription,dish_detail.variant_data from dish_detail  INNER JOIN category_detail  on dish_detail.category_id = category_detail.category_id COLLATE NOCASE" + criteria + orderBy;
        //String strqry = "select *, (select category_name from category_detail where category_detail.category_id = dish_detail.category_id COLLATE NOCASE) as category_name from " + DBHelper.TABLE_DISH_DETAIL + criteria + orderBy;

        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            dishDetail = new DishDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_DISH_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_DISH_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_IMAGE_URL)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_BARCODE)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_IN_MINUTE)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_IN_SECOND)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_WEIGHT_IN_GRAM)),
                    cursor.getDouble(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_TOTAL_PRICE)),
                    cursor.getDouble(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_PRICE_START_FROM)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_APPLY_GST)) == 1,
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_APPLY_DISCOUNT)) == 1,
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DISH_DETAIL_VARIANT_DATA)));

            dishDetail.setCategoryName(cursor.getString(cursor.getColumnIndex(DBHelper.COL_CATEGORY_DETAIL_CATEGORY_NAME)));
            dishDetails.add(dishDetail);
            cursor.moveToNext();
        }
        closeDBConnection();
        return dishDetails;
    }


    public List<BankPromotion> getBankPromotions(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<BankPromotion> bankPromotions = new ArrayList<BankPromotion>();
        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_BANK_PROMOTION + criteria + orderBy;
        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            bankPromotions.add(new BankPromotion(cursor.getString(cursor.getColumnIndex(DBHelper.COL_BANK_PROMOTION_BANK_DETAIL_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_BANK_PROMOTION_BANK_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_BANK_DISCOUNT_PERCENTAGE)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_BANK_DISCOUNT_AMOUNT)),
                    new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_BANK_START_DATE))),
                    new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_BANK_END_DATE)))));
            cursor.moveToNext();
        }
        closeDBConnection();
        return bankPromotions;
    }

    public List<WaiterDetail> getWaiterDetail(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<WaiterDetail> waiterDetails = new ArrayList<WaiterDetail>();
        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_WAITER + criteria + orderBy;
        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            waiterDetails.add(new WaiterDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_WAITER_WAITER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_WAITER_WAITER_NAME))));
            cursor.moveToNext();
        }
        closeDBConnection();
        return waiterDetails;
    }


    public List<ShiftTypeDetail> getShiftTypes(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<ShiftTypeDetail> shiftTypeDetails = new ArrayList<ShiftTypeDetail>();
        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_SHIFT_TYPE + criteria + orderBy;
        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            shiftTypeDetails.add(new ShiftTypeDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_TYPE_SHIFT_TYPE_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_TYPE_SHIFT_TYPE_NAME))));
            cursor.moveToNext();
        }
        closeDBConnection();
        return shiftTypeDetails;
    }

    public List<DeliveryCompanyDetail> getDeliveryCompanies(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<DeliveryCompanyDetail> deliveryCompanyDetails = new ArrayList<DeliveryCompanyDetail>();
        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_DELIVERY_COMPANY + criteria + orderBy;
        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            deliveryCompanyDetails.add(new DeliveryCompanyDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_DELIVERY_COMPANY_DELIVERY_COMPANY_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DELIVERY_COMPANY_DELIVERY_COMPANY_NAME))));
            cursor.moveToNext();
        }
        closeDBConnection();
        return deliveryCompanyDetails;
    }


    public DeliveryCompanyDetail getDeliveryCompany(String deliveryCompanyId) {
        DeliveryCompanyDetail deliveryCompanyDetail = null;

        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_DELIVERY_COMPANY +
                " where " + DBHelper.COL_DELIVERY_COMPANY_DELIVERY_COMPANY_ID + " = '" + deliveryCompanyId + "'";

        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {

            deliveryCompanyDetail = new DeliveryCompanyDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_DELIVERY_COMPANY_DELIVERY_COMPANY_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_DELIVERY_COMPANY_DELIVERY_COMPANY_NAME)));
            ;
        }
        closeDBConnection();
        return deliveryCompanyDetail;
    }

    public List<Customer> getCustomers(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<Customer> customers = new ArrayList<Customer>();
        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_CUSTOMER + criteria + orderBy;
        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            customers.add(new Customer(cursor.getString(cursor.getColumnIndex(DBHelper.COL_CUSTOMER_CUSTOMER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_CUSTOMER_FULL_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_CUSTOMER_CONTACT_NO)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_CUSTOMER_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_CUSTOMER_REWARD_BALANCE))));
            cursor.moveToNext();
        }
        closeDBConnection();
        return customers;
    }


    public List<OrderMaster> getOrderMasters(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<OrderMaster> orderMasters = new ArrayList<OrderMaster>();
        OrderMaster orderMaster = null;
        List<OrderChild> orderChilds = null;
        List<OrderCardDetail> orderCardDetails = null;
        OrderChild orderChild = null;
        OrderCardDetail orderCardDetail = null;
        String orderMasterId = null;

        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_ORDER_MASTER + criteria + orderBy;
        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            orderMasterId = cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_ORDER_MASTER_ID));

            orderMaster = new OrderMaster(new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_ORDER_DEVICE_DATE))),
                    new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CREATION_DEVICE_DATETIME))),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DEVICE_RECEIPT_NO)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_ORDER_TYPE_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_TABLE_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_TABLE_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_WAITER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_WAITER_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_NO_OF_PERSON)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CUSTOMER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CUSTOMER_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CUSTOMER_CONTACT_NO)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CUSTOMER_ADDRESS)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DISCOUNT_TYPE_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DISCOUNT_PERCENTAGE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DISCOUNT_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_PRODUCT_DISCOUNT_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_COMPLEMENTARY_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_BANK_PROMOTION_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_BANK_PROMOTION_DISCOUNT_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_SALES_TAX_PERCENT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_SALES_TAX_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_TIP)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_SUB_TOTAL_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_TOTAL_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CASH_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CARD_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CHANGE_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CARD_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CARD_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CARD_NO_OF_SPLIT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DELIVERY_FEE_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DELIVERY_COMPANY)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DELIVERY_TYPE)),
                    ((cursor.getLong(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DELIVERY_DATE)) != 0) ? new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DELIVERY_DATE))) : null),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_RIDER_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_RIDER_MOBILE_NO)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_RIDER_BIKE_NO)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_SHIFT_RECORD_ID)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_PAYMENT_TYPE_ID)));

            orderMaster.setOrderMasterId(orderMasterId);
            orderMaster.setUserId(cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_USER_ID)));
            orderMaster.setUsername(cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_USERNAME)));
            orderMaster.setCheckoutDeviceDatetime(new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CHECKOUT_DEVICE_DATETIME))));
            orderMaster.setStatusId(cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_STATUS_ID)));
            orderMaster.setSendStatusId(cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_SEND_STATUS_ID)));

            orderChilds = new ArrayList<OrderChild>();

            strqry = "select * from " + DBHelper.TABLE_ORDER_CHILD +
                    " where " + DBHelper.COL_ORDER_CHILD_ORDER_MASTER_ID + " = '" + orderMasterId + "'";

            Cursor cursorChild = database.rawQuery(strqry, null);

            cursorChild.moveToFirst();

            while (!cursorChild.isAfterLast()) {
                orderChild = new OrderChild(cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_DISH_ID)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_DISH_NAME)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_QUANTITY)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_PRICE)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_AMOUNT)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_VARIANT_AMOUNT)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_PRINT_QUANTITY)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_SPECIAL_INSTRUCTION)),
                        cursorChild.getInt(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_DISCOUNT_TYPE_ID)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_DISCOUNT_PERCENTAGE)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_DISCOUNT_AMOUNT)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_COMPLEMENTARY_QUANTITY)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_COMPLEMENTARY_AMOUNT)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_NET_AMOUNT)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_TOTAL_AMOUNT)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_VARIANT_DATA)),
                        ((cursorChild.getLong(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_DATETIME_STAMP)) != 0) ? new Date(cursorChild.getLong(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_DATETIME_STAMP))) : null),
                        cursorChild.getInt(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_APPLY_GST)) == 1,
                        cursorChild.getInt(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_APPLY_DISCOUNT)) == 1,
                        cursorChild.getInt(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_STATUS_ID)));

                orderChild.setOrderChildId(cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_ORDER_CHILD_ID)));
                orderChild.setOrderMasterId(cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_ORDER_MASTER_ID)));

                orderChilds.add(orderChild);
                cursorChild.moveToNext();
            }

            orderMaster.setOrderChilds(orderChilds);

            orderCardDetails = new ArrayList<OrderCardDetail>();

            strqry = "select * from " + DBHelper.TABLE_ORDER_CARD_DETAIL +
                    " where " + DBHelper.COL_ORDER_CARD_DETAIL_ORDER_MASTER_ID + " = '" + orderMasterId + "'";

            Cursor cursorCardChild = database.rawQuery(strqry, null);

            cursorCardChild.moveToFirst();

            while (!cursorCardChild.isAfterLast()) {
                orderCardDetail = new OrderCardDetail(cursorCardChild.getInt(cursorCardChild.getColumnIndex(DBHelper.COL_ORDER_CARD_DETAIL_GUEST_SPLIT_NO)),
                        cursorCardChild.getString(cursorCardChild.getColumnIndex(DBHelper.COL_ORDER_CARD_DETAIL_CARD_AMOUNT)),
                        cursorCardChild.getString(cursorCardChild.getColumnIndex(DBHelper.COL_ORDER_CARD_DETAIL_CARD_NUMBER)),
                        cursorCardChild.getString(cursorCardChild.getColumnIndex(DBHelper.COL_ORDER_CARD_DETAIL_CARD_TYPE)));

                orderCardDetail.setCardDetailId(cursorCardChild.getString(cursorCardChild.getColumnIndex(DBHelper.COL_ORDER_CARD_DETAIL_CARD_DETAIL_ID)));
                orderCardDetail.setOrderMasterId(cursorCardChild.getString(cursorCardChild.getColumnIndex(DBHelper.COL_ORDER_CARD_DETAIL_ORDER_MASTER_ID)));

                orderCardDetails.add(orderCardDetail);
                cursorCardChild.moveToNext();
            }

            orderMaster.setOrderCardDetails(orderCardDetails);


            orderMasters.add(orderMaster);

            cursor.moveToNext();
        }
        closeDBConnection();
        return orderMasters;
    }


    public List<ShiftRecord> getShiftRecords(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<ShiftRecord> shiftRecords = new ArrayList<ShiftRecord>();
        ShiftRecord shiftRecord = null;
        Date finishTime = null;
        openDBConnection();
        String strqry = "select shift_record.*, ifnull(shift_type.shift_type_name,'') shift_type_name from shift_record left outer join shift_type on shift_record.shift_type_id = shift_type.shift_type_id" + criteria + orderBy;
        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            finishTime = null;

            if (cursor.getLong(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_FINISH_TIME)) != 0) {
                finishTime = new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_FINISH_TIME)));
            }
            shiftRecord = new ShiftRecord(cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_SHIFT_RECORD_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_SHIFT_TYPE_ID)),
                    new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_SHIFT_DATE))),
                    new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_START_TIME))),
                    finishTime,
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_OPENING_CASH)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_CLOSING_CASH)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_COMMENTS)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_STATUS_ID)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_SEND_STATUS_ID)));

            shiftRecord.setShiftType(cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_TYPE_SHIFT_TYPE_NAME)));

            shiftRecords.add(shiftRecord);

            cursor.moveToNext();
        }
        closeDBConnection();
        return shiftRecords;
    }


    public List<DayRecord> getDayRecords() {

        List<DayRecord> dayRecords = new ArrayList<DayRecord>();
        DayRecord dayRecord = null;

        openDBConnection();

        String strqry = "select shift_date, count(*) no_of_shift " +
                "from shift_record " +
                "group by shift_date " +
                "order by shift_date desc";

        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            dayRecord = new DayRecord(new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_SHIFT_DATE))),
                    cursor.getInt(cursor.getColumnIndex("no_of_shift")));

            dayRecords.add(dayRecord);

            cursor.moveToNext();
        }
        closeDBConnection();
        return dayRecords;
    }

    public List<PrinterDetail> getPrinterDetails(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<PrinterDetail> printerDetails = new ArrayList<PrinterDetail>();
        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_PRINTER_DETAIL + criteria + orderBy;
        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            printerDetails.add(new PrinterDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PRINTER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_TITLE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PRINTER_TYPE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PRINTER_BRAND)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PRINTER_MODEL)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PRINTER_IP)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PORT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PRINT_SERVER_URL)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PRINTER_NAME))));
            cursor.moveToNext();
        }
        closeDBConnection();
        return printerDetails;
    }

    public OrderMaster getOrderMaster(String orderMasterId) {

        OrderMaster orderMaster = null;
        List<OrderChild> orderChilds = null;
        List<OrderCardDetail> orderCardDetails = null;
        OrderChild orderChild = null;
        OrderCardDetail orderCardDetail = null;

        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_ORDER_MASTER +
                " where " + DBHelper.COL_ORDER_MASTER_ORDER_MASTER_ID + " = '" + orderMasterId + "'";

        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {

            orderMasterId = cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_ORDER_MASTER_ID));

            orderMaster = new OrderMaster(new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_ORDER_DEVICE_DATE))),
                    new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CREATION_DEVICE_DATETIME))),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DEVICE_RECEIPT_NO)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_ORDER_TYPE_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_TABLE_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_TABLE_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_WAITER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_WAITER_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_NO_OF_PERSON)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CUSTOMER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CUSTOMER_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CUSTOMER_CONTACT_NO)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CUSTOMER_ADDRESS)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DISCOUNT_TYPE_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DISCOUNT_PERCENTAGE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DISCOUNT_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_PRODUCT_DISCOUNT_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_COMPLEMENTARY_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_BANK_PROMOTION_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_BANK_PROMOTION_DISCOUNT_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_SALES_TAX_PERCENT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_SALES_TAX_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_TIP)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_SUB_TOTAL_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_TOTAL_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CASH_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CARD_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CHANGE_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CARD_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CARD_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CARD_NO_OF_SPLIT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DELIVERY_FEE_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DELIVERY_COMPANY)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DELIVERY_TYPE)),
                    ((cursor.getLong(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DELIVERY_DATE)) != 0) ? new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_DELIVERY_DATE))) : null),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_RIDER_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_RIDER_MOBILE_NO)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_RIDER_BIKE_NO)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_SHIFT_RECORD_ID)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_PAYMENT_TYPE_ID)));

            orderMaster.setOrderMasterId(orderMasterId);
            orderMaster.setUserId(cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_USER_ID)));
            orderMaster.setUsername(cursor.getString(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_USERNAME)));
            orderMaster.setCheckoutDeviceDatetime(new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_CHECKOUT_DEVICE_DATETIME))));
            orderMaster.setStatusId(cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_STATUS_ID)));
            orderMaster.setSendStatusId(cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ORDER_MASTER_SEND_STATUS_ID)));

            orderChilds = new ArrayList<OrderChild>();

            strqry = "select * from " + DBHelper.TABLE_ORDER_CHILD +
                    " where " + DBHelper.COL_ORDER_CHILD_ORDER_MASTER_ID + " = '" + orderMasterId + "'";

            Cursor cursorChild = database.rawQuery(strqry, null);

            cursorChild.moveToFirst();

            while (!cursorChild.isAfterLast()) {
                orderChild = new OrderChild(cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_DISH_ID)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_DISH_NAME)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_QUANTITY)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_PRICE)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_AMOUNT)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_VARIANT_AMOUNT)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_PRINT_QUANTITY)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_SPECIAL_INSTRUCTION)),
                        cursorChild.getInt(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_DISCOUNT_TYPE_ID)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_DISCOUNT_PERCENTAGE)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_DISCOUNT_AMOUNT)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_COMPLEMENTARY_QUANTITY)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_COMPLEMENTARY_AMOUNT)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_NET_AMOUNT)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_TOTAL_AMOUNT)),
                        cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_VARIANT_DATA)),
                        ((cursorChild.getLong(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_DATETIME_STAMP)) != 0) ? new Date(cursorChild.getLong(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_DATETIME_STAMP))) : null),
                        cursorChild.getInt(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_APPLY_GST)) == 1,
                        cursorChild.getInt(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_APPLY_DISCOUNT)) == 1,
                        cursorChild.getInt(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_STATUS_ID)));

                orderChild.setOrderChildId(cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_ORDER_CHILD_ID)));
                orderChild.setOrderMasterId(cursorChild.getString(cursorChild.getColumnIndex(DBHelper.COL_ORDER_CHILD_ORDER_MASTER_ID)));

                orderChilds.add(orderChild);
                cursorChild.moveToNext();
            }

            orderMaster.setOrderChilds(orderChilds);

            orderCardDetails = new ArrayList<OrderCardDetail>();

            strqry = "select * from " + DBHelper.TABLE_ORDER_CARD_DETAIL +
                    " where " + DBHelper.COL_ORDER_CARD_DETAIL_ORDER_MASTER_ID + " = '" + orderMasterId + "'";

            Cursor cursorCardChild = database.rawQuery(strqry, null);

            cursorCardChild.moveToFirst();

            while (!cursorCardChild.isAfterLast()) {
                orderCardDetail = new OrderCardDetail(cursorCardChild.getInt(cursorCardChild.getColumnIndex(DBHelper.COL_ORDER_CARD_DETAIL_GUEST_SPLIT_NO)),
                        cursorCardChild.getString(cursorCardChild.getColumnIndex(DBHelper.COL_ORDER_CARD_DETAIL_CARD_AMOUNT)),
                        cursorCardChild.getString(cursorCardChild.getColumnIndex(DBHelper.COL_ORDER_CARD_DETAIL_CARD_NUMBER)),
                        cursorCardChild.getString(cursorCardChild.getColumnIndex(DBHelper.COL_ORDER_CARD_DETAIL_CARD_TYPE)));

                orderCardDetail.setCardDetailId(cursorCardChild.getString(cursorCardChild.getColumnIndex(DBHelper.COL_ORDER_CARD_DETAIL_CARD_DETAIL_ID)));
                orderCardDetail.setOrderMasterId(cursorCardChild.getString(cursorCardChild.getColumnIndex(DBHelper.COL_ORDER_CARD_DETAIL_ORDER_MASTER_ID)));

                orderCardDetails.add(orderCardDetail);
                cursorCardChild.moveToNext();
            }

            orderMaster.setOrderCardDetails(orderCardDetails);
        }
        closeDBConnection();
        return orderMaster;
    }

    public PrinterDetail getPrinterDetail(String PrinterId) {

        PrinterDetail printerDetail = null;

        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_PRINTER_DETAIL +
                " where " + DBHelper.COL_PRINTER_DETAIL_PRINTER_ID + " = '" + PrinterId + "'";

        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {

            printerDetail = new PrinterDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PRINTER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_TITLE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PRINTER_TYPE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PRINTER_BRAND)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PRINTER_MODEL)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PRINTER_IP)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PORT)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PRINT_SERVER_URL)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_PRINTER_DETAIL_PRINTER_NAME)));
        }
        closeDBConnection();
        return printerDetail;
    }

    public UserDetail getUserDetailByUsername(String Username) {

        UserDetail userDetail = null;

        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_USER_DETAIL +
                " where " + DBHelper.COL_USER_DETAIL_USERNAME + " = '" + Username + "' COLLATE NOCASE";

        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {

            userDetail = new UserDetail(cursor.getString(cursor.getColumnIndex(DBHelper.COL_USER_DETAIL_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_USER_DETAIL_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_USER_DETAIL_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_USER_DETAIL_USERNAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_USER_DETAIL_PASSWORD)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_USER_DETAIL_HASH_PASSWORD)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_USER_DETAIL_HASH_POS_KEY)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_USER_DETAIL_HASH_MODIFICATION_KEY)));
        }
        closeDBConnection();
        return userDetail;
    }


    public ShiftRecord getShiftRecord(String ShiftRecordId) {
        ShiftRecord shiftRecord = null;

        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_SHIFT_RECORD +
                " where " + DBHelper.COL_SHIFT_RECORD_SHIFT_RECORD_ID + " = '" + ShiftRecordId + "'";

        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {

            shiftRecord = new ShiftRecord(cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_SHIFT_RECORD_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_SHIFT_TYPE_ID)),
                    new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_SHIFT_DATE))),
                    new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_START_TIME))),
                    new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_FINISH_TIME))),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_OPENING_CASH)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_CLOSING_CASH)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_COMMENTS)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_STATUS_ID)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_SEND_STATUS_ID))

            );
        }
        closeDBConnection();
        return shiftRecord;
    }

    public ShiftRecord getLastShiftRecordByUserId(String UserId) {
        ShiftRecord shiftRecord = null;

        openDBConnection();
        String strqry = "select * from " + DBHelper.TABLE_SHIFT_RECORD +
                " where " + DBHelper.COL_SHIFT_RECORD_USER_ID + " = '" + UserId + "' AND " + dBHelper.COL_SHIFT_RECORD_STATUS_ID + " = 1";

        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {

            shiftRecord = new ShiftRecord(cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_SHIFT_RECORD_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_SHIFT_TYPE_ID)),
                    new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_SHIFT_DATE))),
                    new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_START_TIME))),
                    new Date(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_FINISH_TIME))),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_OPENING_CASH)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_CLOSING_CASH)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_COMMENTS)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_STATUS_ID)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.COL_SHIFT_RECORD_SEND_STATUS_ID))

            );
        }
        closeDBConnection();

        return shiftRecord;
    }


    public long addUpdateSettingDetail(SettingDetail settingDetail) {
        openDBConnection();
        long rowId = 0;
        ContentValues contentValues = new ContentValues();

        String settingDetailId = UUID.randomUUID().toString();
        boolean isUpdate = false;
        if (settingDetail.getSettingDetailId() != null && settingDetail.getSettingDetailId().length() > 0) {
            settingDetailId = settingDetail.getSettingDetailId();
            isUpdate = true;
        }

        contentValues.put(DBHelper.COL_SETTING_DETAIL_SETTING_DETAIL_ID, settingDetailId);
        contentValues.put(DBHelper.COL_SETTING_DETAIL_DEFAULT_PRINTER_ID, settingDetail.getDefaultPrinterId());
        contentValues.put(DBHelper.COL_SETTING_DETAIL_KITCHEN_PRINTER_ID, settingDetail.getKitchenPrinterId());
        contentValues.put(DBHelper.COL_SETTING_DETAIL_KITCHEN_PRINT_COPY, settingDetail.getKitchenPintCopy());
        contentValues.put(DBHelper.COL_SETTING_DETAIL_KITCHEN_PRINTER_CATEGORIES, settingDetail.getKitchenPrinterCategories());
        contentValues.put(DBHelper.COL_SETTING_DETAIL_SHOW_PRE_BILL_PREVIEW, (settingDetail.isShowPreBillPreview() ? 1 : 0));
        contentValues.put(DBHelper.COL_SETTING_DETAIL_SHOW_POST_BILL_PREVIEW, (settingDetail.isShowPostBillPreview() ? 1 : 0));
        contentValues.put(DBHelper.COL_SETTING_DETAIL_SHOW_KITCHEN_PRINT_PREVIEW, (settingDetail.isShowKitchenPrintPreview() ? 1 : 0));
        contentValues.put(DBHelper.COL_SETTING_DETAIL_AUTO_PRINT_AFTER_CHECKOUT, (settingDetail.isAutoPrintAfterCheckout() ? 1 : 0));
        contentValues.put(DBHelper.COL_SETTING_DETAIL_OPEN_CASH_DRAWER_AFTER_CHECKOUT, (settingDetail.isOpenCashDrawerAfterCheckout() ? 1 : 0));

        if (!isUpdate) {
            rowId = database.insertOrThrow(DBHelper.TABLE_SETTING_DETAIL, null, contentValues);
            settingDetail.setSettingDetailId(settingDetailId);
        } else {
            rowId = database.update(DBHelper.TABLE_SETTING_DETAIL, contentValues, DBHelper.COL_SETTING_DETAIL_SETTING_DETAIL_ID + " = :primaryKey", new String[]{String.valueOf(settingDetailId)});
        }
        closeDBConnection();
        return rowId;
    }

    public boolean addUpdateSyncDetail(SyncDetail syncDetail) {
        openDBConnection();

        String strqry = "insert or replace into " + DBHelper.TABLE_SYNC_DETAIL + " (" +
                DBHelper.COL_SYNC_DETAIL_TABLE_NAME + ", " +
                DBHelper.COL_SYNC_DETAIL_TABLE_TITLE + ", " +
                DBHelper.COL_SYNC_DETAIL_TABLE_TYPE + ", " +
                DBHelper.COL_SYNC_DETAIL_TABLE_ORDER + ", " +
                DBHelper.COL_SYNC_DETAIL_RECORD_COUNT + ", " +
                DBHelper.COL_SYNC_DETAIL_LAST_SYNCED_ON + ") values " +
                "('" +
                syncDetail.getTableName() + "','" +
                syncDetail.getTableTitle() + "'," +
                syncDetail.getTableType() + "," +
                syncDetail.getTableOrder() + "," +
                syncDetail.getRecordCount() + "," +
                syncDetail.getLastSyncedOn().getTime() +
                ")";
        database.execSQL(strqry);

        closeDBConnection();
        return true;
    }

    public long addOrder(OrderMaster orderMaster) {

        openDBConnection();
        long rowId = 0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.COL_ORDER_MASTER_ORDER_MASTER_ID, orderMaster.getOrderMasterId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_ORDER_DEVICE_DATE, orderMaster.getOrderDeviceDate().getTime());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CREATION_DEVICE_DATETIME, orderMaster.getCreationDeviceDatetime().getTime());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DEVICE_RECEIPT_NO, orderMaster.getDeviceReceiptNo());
        contentValues.put(DBHelper.COL_ORDER_MASTER_ORDER_TYPE_ID, orderMaster.getOrderTypeId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_TABLE_ID, orderMaster.getTableId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_TABLE_NAME, orderMaster.getTableName());
        contentValues.put(DBHelper.COL_ORDER_MASTER_WAITER_ID, orderMaster.getWaiterId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_WAITER_NAME, orderMaster.getWaiterName());
        contentValues.put(DBHelper.COL_ORDER_MASTER_NO_OF_PERSON, orderMaster.getNoOfPerson());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CUSTOMER_ID, orderMaster.getCustomerId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CUSTOMER_NAME, orderMaster.getCustomerName());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CUSTOMER_CONTACT_NO, orderMaster.getCustomerContactNo());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CUSTOMER_ADDRESS, orderMaster.getCustomerAddress());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DISCOUNT_TYPE_ID, orderMaster.getDiscountTypeId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DISCOUNT_PERCENTAGE, orderMaster.getDiscountPercentage());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DISCOUNT_AMOUNT, orderMaster.getDiscountAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_PRODUCT_DISCOUNT_AMOUNT, orderMaster.getProductDiscountAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_COMPLEMENTARY_AMOUNT, orderMaster.getComplementaryAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_BANK_PROMOTION_ID, orderMaster.getBankPromotionId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_BANK_PROMOTION_DISCOUNT_AMOUNT, orderMaster.getBankPromotionDiscountAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_SALES_TAX_PERCENT, orderMaster.getSalesTaxPercent());
        contentValues.put(DBHelper.COL_ORDER_MASTER_SALES_TAX_AMOUNT, orderMaster.getSalesTaxAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_TIP, orderMaster.getTip());
        contentValues.put(DBHelper.COL_ORDER_MASTER_SUB_TOTAL_AMOUNT, orderMaster.getSubTotalAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_TOTAL_AMOUNT, orderMaster.getTotalAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CASH_AMOUNT, orderMaster.getCashAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CARD_AMOUNT, orderMaster.getCardAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CHANGE_AMOUNT, orderMaster.getChangeAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CARD_NUMBER, orderMaster.getCardNumber());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CARD_TYPE, orderMaster.getCardType());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CARD_NO_OF_SPLIT, orderMaster.getCardNoOfSplit());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DELIVERY_FEE_AMOUNT, orderMaster.getDeliveryFeeAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DELIVERY_COMPANY, orderMaster.getDeliveryCompany());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DELIVERY_TYPE, orderMaster.getDeliveryType());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DELIVERY_DATE, (orderMaster.getDeliveryDate() != null ? orderMaster.getDeliveryDate().getTime() : null));
        contentValues.put(DBHelper.COL_ORDER_MASTER_RIDER_NAME, orderMaster.getRiderName());
        contentValues.put(DBHelper.COL_ORDER_MASTER_RIDER_MOBILE_NO, orderMaster.getRiderMobileNo());
        contentValues.put(DBHelper.COL_ORDER_MASTER_RIDER_BIKE_NO, orderMaster.getRiderBikeNo());
        contentValues.put(DBHelper.COL_ORDER_MASTER_SHIFT_RECORD_ID, orderMaster.getShiftRecordId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_PAYMENT_TYPE_ID, orderMaster.getPaymentTypeId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_USER_ID, orderMaster.getUserId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_USERNAME, orderMaster.getUsername());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CHECKOUT_DEVICE_DATETIME, orderMaster.getCheckoutDeviceDatetime().getTime());
        contentValues.put(DBHelper.COL_ORDER_MASTER_STATUS_ID, orderMaster.getStatusId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_SEND_STATUS_ID, orderMaster.getSendStatusId());

        rowId = database.insert(DBHelper.TABLE_ORDER_MASTER, null, contentValues);

        for (OrderChild orderChild : orderMaster.getOrderChilds()) {
            contentValues = new ContentValues();

            contentValues.put(DBHelper.COL_ORDER_CHILD_ORDER_CHILD_ID, orderChild.getOrderChildId());
            contentValues.put(DBHelper.COL_ORDER_CHILD_ORDER_MASTER_ID, orderChild.getOrderMasterId());
            contentValues.put(DBHelper.COL_ORDER_CHILD_DISH_ID, orderChild.getDishId());
            contentValues.put(DBHelper.COL_ORDER_CHILD_DISH_NAME, orderChild.getDishName());
            contentValues.put(DBHelper.COL_ORDER_CHILD_QUANTITY, orderChild.getQuantity());
            contentValues.put(DBHelper.COL_ORDER_CHILD_PRICE, orderChild.getPrice());
            contentValues.put(DBHelper.COL_ORDER_CHILD_AMOUNT, orderChild.getAmount());
            contentValues.put(DBHelper.COL_ORDER_CHILD_VARIANT_AMOUNT, orderChild.getVariantAmount());
            contentValues.put(DBHelper.COL_ORDER_CHILD_PRINT_QUANTITY, orderChild.getPrintQuantity());
            contentValues.put(DBHelper.COL_ORDER_CHILD_SPECIAL_INSTRUCTION, orderChild.getSpecialInstruction());
            contentValues.put(DBHelper.COL_ORDER_CHILD_DISCOUNT_TYPE_ID, orderChild.getDiscountTypeId());
            contentValues.put(DBHelper.COL_ORDER_CHILD_DISCOUNT_PERCENTAGE, orderChild.getDiscountPercentage());
            contentValues.put(DBHelper.COL_ORDER_CHILD_DISCOUNT_AMOUNT, orderChild.getDiscountAmount());
            contentValues.put(DBHelper.COL_ORDER_CHILD_COMPLEMENTARY_QUANTITY, orderChild.getComplementaryQuantity());
            contentValues.put(DBHelper.COL_ORDER_CHILD_COMPLEMENTARY_AMOUNT, orderChild.getComplementaryAmount());
            contentValues.put(DBHelper.COL_ORDER_CHILD_NET_AMOUNT, orderChild.getNetAmount());
            contentValues.put(DBHelper.COL_ORDER_CHILD_TOTAL_AMOUNT, orderChild.getTotalAmount());
            contentValues.put(DBHelper.COL_ORDER_CHILD_VARIANT_DATA, orderChild.getVariantData());
            contentValues.put(DBHelper.COL_ORDER_CHILD_DATETIME_STAMP, (orderChild.getDatetimeStamp() != null ? orderChild.getDatetimeStamp().getTime() : null));
            contentValues.put(DBHelper.COL_DISH_DETAIL_APPLY_GST, orderChild.isApplyGST() == true ? 1 : 0);
            contentValues.put(DBHelper.COL_DISH_DETAIL_APPLY_DISCOUNT, orderChild.isApplyDiscount() == true ? 1 : 0);
            contentValues.put(DBHelper.COL_ORDER_CHILD_STATUS_ID, orderChild.getStatusId());


            rowId = database.insert(DBHelper.TABLE_ORDER_CHILD, null, contentValues);
        }

        for (OrderCardDetail orderCardDetail : orderMaster.getOrderCardDetails()) {
            contentValues = new ContentValues();

            contentValues.put(DBHelper.COL_ORDER_CARD_DETAIL_CARD_DETAIL_ID, orderCardDetail.getCardDetailId());
            contentValues.put(DBHelper.COL_ORDER_CARD_DETAIL_ORDER_MASTER_ID, orderCardDetail.getOrderMasterId());
            contentValues.put(DBHelper.COL_ORDER_CARD_DETAIL_GUEST_SPLIT_NO, orderCardDetail.getGuestSplitNo());
            contentValues.put(DBHelper.COL_ORDER_CARD_DETAIL_CARD_AMOUNT, orderCardDetail.getCardAmount());
            contentValues.put(DBHelper.COL_ORDER_CARD_DETAIL_CARD_NUMBER, orderCardDetail.getCardNumber());
            contentValues.put(DBHelper.COL_ORDER_CARD_DETAIL_CARD_TYPE, orderCardDetail.getCardType());

            rowId = database.insert(DBHelper.TABLE_ORDER_CARD_DETAIL, null, contentValues);
        }

        closeDBConnection();
        return rowId;
    }


    public long addUpdateOrderMaster(OrderMaster orderMaster) {

        openDBConnection();
        long rowId = 0;
        ContentValues contentValues = new ContentValues();

        String orderMasterId = UUID.randomUUID().toString();
        boolean isUpdate = false;
        if (orderMaster.getOrderMasterId() != null && orderMaster.getOrderMasterId().length() > 0) {
            orderMasterId = orderMaster.getOrderMasterId();
            isUpdate = true;
        }

        contentValues.put(DBHelper.COL_ORDER_MASTER_ORDER_MASTER_ID, orderMasterId);
        contentValues.put(DBHelper.COL_ORDER_MASTER_ORDER_DEVICE_DATE, orderMaster.getOrderDeviceDate().getTime());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CREATION_DEVICE_DATETIME, orderMaster.getCreationDeviceDatetime().getTime());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DEVICE_RECEIPT_NO, orderMaster.getDeviceReceiptNo());
        contentValues.put(DBHelper.COL_ORDER_MASTER_ORDER_TYPE_ID, orderMaster.getOrderTypeId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_TABLE_ID, orderMaster.getTableId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_TABLE_NAME, orderMaster.getTableName());
        contentValues.put(DBHelper.COL_ORDER_MASTER_WAITER_ID, orderMaster.getWaiterId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_WAITER_NAME, orderMaster.getWaiterName());
        contentValues.put(DBHelper.COL_ORDER_MASTER_NO_OF_PERSON, orderMaster.getNoOfPerson());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CUSTOMER_ID, orderMaster.getCustomerId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CUSTOMER_NAME, orderMaster.getCustomerName());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CUSTOMER_CONTACT_NO, orderMaster.getCustomerContactNo());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CUSTOMER_ADDRESS, orderMaster.getCustomerAddress());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DISCOUNT_TYPE_ID, orderMaster.getDiscountTypeId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DISCOUNT_PERCENTAGE, orderMaster.getDiscountPercentage());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DISCOUNT_AMOUNT, orderMaster.getDiscountAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_PRODUCT_DISCOUNT_AMOUNT, orderMaster.getProductDiscountAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_COMPLEMENTARY_AMOUNT, orderMaster.getComplementaryAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_BANK_PROMOTION_ID, orderMaster.getBankPromotionId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_BANK_PROMOTION_DISCOUNT_AMOUNT, orderMaster.getBankPromotionDiscountAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_SALES_TAX_PERCENT, orderMaster.getSalesTaxPercent());
        contentValues.put(DBHelper.COL_ORDER_MASTER_SALES_TAX_AMOUNT, orderMaster.getSalesTaxAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_TIP, orderMaster.getTip());
        contentValues.put(DBHelper.COL_ORDER_MASTER_SUB_TOTAL_AMOUNT, orderMaster.getSubTotalAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_TOTAL_AMOUNT, orderMaster.getTotalAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CASH_AMOUNT, orderMaster.getCashAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CARD_AMOUNT, orderMaster.getCardAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CHANGE_AMOUNT, orderMaster.getChangeAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CARD_NUMBER, orderMaster.getCardNumber());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CARD_TYPE, orderMaster.getCardType());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CARD_NO_OF_SPLIT, orderMaster.getCardNoOfSplit());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DELIVERY_FEE_AMOUNT, orderMaster.getDeliveryFeeAmount());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DELIVERY_COMPANY, orderMaster.getDeliveryCompany());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DELIVERY_TYPE, orderMaster.getDeliveryType());
        contentValues.put(DBHelper.COL_ORDER_MASTER_DELIVERY_DATE, (orderMaster.getDeliveryDate() != null ? orderMaster.getDeliveryDate().getTime() : null));
        contentValues.put(DBHelper.COL_ORDER_MASTER_RIDER_NAME, orderMaster.getRiderName());
        contentValues.put(DBHelper.COL_ORDER_MASTER_RIDER_MOBILE_NO, orderMaster.getRiderMobileNo());
        contentValues.put(DBHelper.COL_ORDER_MASTER_RIDER_BIKE_NO, orderMaster.getRiderBikeNo());
        contentValues.put(DBHelper.COL_ORDER_MASTER_SHIFT_RECORD_ID, orderMaster.getShiftRecordId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_PAYMENT_TYPE_ID, orderMaster.getPaymentTypeId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_USER_ID, orderMaster.getUserId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_USERNAME, orderMaster.getUsername());
        contentValues.put(DBHelper.COL_ORDER_MASTER_CHECKOUT_DEVICE_DATETIME, orderMaster.getCheckoutDeviceDatetime().getTime());
        contentValues.put(DBHelper.COL_ORDER_MASTER_STATUS_ID, orderMaster.getStatusId());
        contentValues.put(DBHelper.COL_ORDER_MASTER_SEND_STATUS_ID, orderMaster.getSendStatusId());


        if (!isUpdate) {
            rowId = database.insertOrThrow(DBHelper.TABLE_ORDER_MASTER, null, contentValues);
            orderMaster.setOrderMasterId(orderMasterId);

        } else {
            rowId = database.update(DBHelper.TABLE_ORDER_MASTER, contentValues, DBHelper.COL_ORDER_MASTER_ORDER_MASTER_ID + " = :primaryKey", new String[]{String.valueOf(orderMasterId)});
        }
        closeDBConnection();
        return rowId;
    }

    public long addUpdateOrderChild(OrderChild orderChild) {
        openDBConnection();
        long rowId = 0;
        ContentValues contentValues = new ContentValues();

        String orderChildId = UUID.randomUUID().toString();
        boolean isUpdate = false;
        if (orderChild.getOrderChildId() != null && orderChild.getOrderChildId().length() > 0) {
            orderChildId = orderChild.getOrderChildId();
            isUpdate = true;
        }

        contentValues.put(DBHelper.COL_ORDER_CHILD_ORDER_CHILD_ID, orderChildId);
        contentValues.put(DBHelper.COL_ORDER_CHILD_ORDER_MASTER_ID, orderChild.getOrderMasterId());
        contentValues.put(DBHelper.COL_ORDER_CHILD_DISH_ID, orderChild.getDishId());
        contentValues.put(DBHelper.COL_ORDER_CHILD_DISH_NAME, orderChild.getDishName());
        contentValues.put(DBHelper.COL_ORDER_CHILD_QUANTITY, orderChild.getQuantity());
        contentValues.put(DBHelper.COL_ORDER_CHILD_PRICE, orderChild.getPrice());
        contentValues.put(DBHelper.COL_ORDER_CHILD_AMOUNT, orderChild.getAmount());
        contentValues.put(DBHelper.COL_ORDER_CHILD_VARIANT_AMOUNT, orderChild.getVariantAmount());
        contentValues.put(DBHelper.COL_ORDER_CHILD_PRINT_QUANTITY, orderChild.getPrintQuantity());
        contentValues.put(DBHelper.COL_ORDER_CHILD_SPECIAL_INSTRUCTION, orderChild.getSpecialInstruction());
        contentValues.put(DBHelper.COL_ORDER_CHILD_DISCOUNT_TYPE_ID, orderChild.getDiscountTypeId());
        contentValues.put(DBHelper.COL_ORDER_CHILD_DISCOUNT_PERCENTAGE, orderChild.getDiscountPercentage());
        contentValues.put(DBHelper.COL_ORDER_CHILD_DISCOUNT_AMOUNT, orderChild.getDiscountAmount());
        contentValues.put(DBHelper.COL_ORDER_CHILD_COMPLEMENTARY_QUANTITY, orderChild.getComplementaryQuantity());
        contentValues.put(DBHelper.COL_ORDER_CHILD_COMPLEMENTARY_AMOUNT, orderChild.getComplementaryAmount());
        contentValues.put(DBHelper.COL_ORDER_CHILD_NET_AMOUNT, orderChild.getNetAmount());
        contentValues.put(DBHelper.COL_ORDER_CHILD_TOTAL_AMOUNT, orderChild.getTotalAmount());
        contentValues.put(DBHelper.COL_ORDER_CHILD_VARIANT_DATA, orderChild.getVariantData());
        contentValues.put(DBHelper.COL_ORDER_CHILD_DATETIME_STAMP, (orderChild.getDatetimeStamp() != null ? orderChild.getDatetimeStamp().getTime() : null));
        contentValues.put(DBHelper.COL_DISH_DETAIL_APPLY_GST, orderChild.isApplyGST() == true ? 1 : 0);
        contentValues.put(DBHelper.COL_DISH_DETAIL_APPLY_DISCOUNT, orderChild.isApplyDiscount() == true ? 1 : 0);
        contentValues.put(DBHelper.COL_ORDER_CHILD_STATUS_ID, orderChild.getStatusId());

        if (!isUpdate) {
            rowId = database.insertOrThrow(DBHelper.TABLE_ORDER_CHILD, null, contentValues);
            orderChild.setOrderChildId(orderChildId);
        } else {
            rowId = database.update(DBHelper.TABLE_ORDER_CHILD, contentValues, DBHelper.COL_ORDER_CHILD_ORDER_CHILD_ID + " = :primaryKey", new String[]{String.valueOf(orderChildId)});
        }
        closeDBConnection();
        return rowId;
    }

    public long addUpdateOrderCardDetail(OrderCardDetail orderCardDetail) {
        openDBConnection();
        long rowId = 0;
        ContentValues contentValues = new ContentValues();

        String cardDetailId = UUID.randomUUID().toString();
        boolean isUpdate = false;
        if (orderCardDetail.getCardDetailId() != null && orderCardDetail.getCardDetailId().length() > 0) {
            cardDetailId = orderCardDetail.getCardDetailId();
            isUpdate = true;
        }

        contentValues.put(DBHelper.COL_ORDER_CARD_DETAIL_CARD_DETAIL_ID, cardDetailId);
        contentValues.put(DBHelper.COL_ORDER_CARD_DETAIL_ORDER_MASTER_ID, orderCardDetail.getOrderMasterId());
        contentValues.put(DBHelper.COL_ORDER_CARD_DETAIL_GUEST_SPLIT_NO, orderCardDetail.getGuestSplitNo());
        contentValues.put(DBHelper.COL_ORDER_CARD_DETAIL_CARD_AMOUNT, orderCardDetail.getCardAmount());
        contentValues.put(DBHelper.COL_ORDER_CARD_DETAIL_CARD_NUMBER, orderCardDetail.getCardNumber());
        contentValues.put(DBHelper.COL_ORDER_CARD_DETAIL_CARD_TYPE, orderCardDetail.getCardType());

        if (!isUpdate) {
            rowId = database.insertOrThrow(DBHelper.TABLE_ORDER_CARD_DETAIL, null, contentValues);
            orderCardDetail.setCardDetailId(cardDetailId);
        } else {
            rowId = database.update(DBHelper.TABLE_ORDER_CARD_DETAIL, contentValues, DBHelper.COL_ORDER_CARD_DETAIL_CARD_DETAIL_ID + " = :primaryKey", new String[]{String.valueOf(cardDetailId)});
        }
        closeDBConnection();
        return rowId;
    }


    public long addUpdateShiftRecord(ShiftRecord shiftRecord) {
        openDBConnection();
        long rowId = 0;
        ContentValues contentValues = new ContentValues();

        String shiftRecordId = UUID.randomUUID().toString();
        boolean isUpdate = false;
        if (shiftRecord.getShiftRecordId() != null && shiftRecord.getShiftRecordId().length() > 0) {
            shiftRecordId = shiftRecord.getShiftRecordId();
            isUpdate = true;
        }

        contentValues.put(DBHelper.COL_SHIFT_RECORD_SHIFT_RECORD_ID, shiftRecordId);
        contentValues.put(DBHelper.COL_SHIFT_RECORD_USER_ID, shiftRecord.getUserId());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_SHIFT_TYPE_ID, shiftRecord.getShiftTypeId());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_SHIFT_DATE, shiftRecord.getShiftDate().getTime());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_START_TIME, shiftRecord.getStartTime().getTime());
        if (shiftRecord.getFinishTime() != null) {
            contentValues.put(DBHelper.COL_SHIFT_RECORD_FINISH_TIME, shiftRecord.getFinishTime().getTime());
        }
        contentValues.put(DBHelper.COL_SHIFT_RECORD_OPENING_CASH, shiftRecord.getOpeningCash());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_CLOSING_CASH, shiftRecord.getClosingCash());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_COMMENTS, shiftRecord.getComments());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_STATUS_ID, shiftRecord.getStatusId());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_SEND_STATUS_ID, shiftRecord.getSendStatusId());

        if (!isUpdate) {
            rowId = database.insertOrThrow(DBHelper.TABLE_SHIFT_RECORD, null, contentValues);
            shiftRecord.setShiftRecordId(shiftRecordId);
        } else {
            rowId = database.update(DBHelper.TABLE_SHIFT_RECORD, contentValues, DBHelper.COL_SHIFT_RECORD_SHIFT_RECORD_ID + " = :primaryKey", new String[]{String.valueOf(shiftRecordId)});
        }
        closeDBConnection();
        return rowId;
    }

    public long addShiftRecord(ShiftRecord shiftRecord) {
        openDBConnection();
        long rowId = 0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.COL_SHIFT_RECORD_SHIFT_RECORD_ID, shiftRecord.getShiftRecordId());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_USER_ID, shiftRecord.getUserId());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_SHIFT_TYPE_ID, shiftRecord.getShiftTypeId());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_SHIFT_DATE, shiftRecord.getShiftDate().getTime());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_START_TIME, shiftRecord.getStartTime().getTime());
        if (shiftRecord.getFinishTime() != null) {
            contentValues.put(DBHelper.COL_SHIFT_RECORD_FINISH_TIME, shiftRecord.getFinishTime().getTime());
        }
        contentValues.put(DBHelper.COL_SHIFT_RECORD_OPENING_CASH, shiftRecord.getOpeningCash());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_CLOSING_CASH, shiftRecord.getClosingCash());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_COMMENTS, shiftRecord.getComments());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_STATUS_ID, shiftRecord.getStatusId());
        contentValues.put(DBHelper.COL_SHIFT_RECORD_SEND_STATUS_ID, shiftRecord.getSendStatusId());

        rowId = database.insert(DBHelper.TABLE_SHIFT_RECORD, null, contentValues);

        closeDBConnection();
        return rowId;
    }

    public long addUpdatePrinterDetail(PrinterDetail printerDetail) {
        openDBConnection();
        long rowId = 0;
        ContentValues contentValues = new ContentValues();

        String printerId = UUID.randomUUID().toString();
        boolean isUpdate = false;
        if (printerDetail.getPrinterId() != null && printerDetail.getPrinterId().length() > 0) {
            printerId = printerDetail.getPrinterId();
            isUpdate = true;
        }

        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PRINTER_ID, printerId);
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_TITLE, printerDetail.getTitle());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PRINTER_TYPE, printerDetail.getPrinterType());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PRINTER_BRAND, printerDetail.getPrinterBrand());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PRINTER_MODEL, printerDetail.getPrinterModel());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PRINTER_IP, printerDetail.getPrinterIp());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PORT, printerDetail.getPort());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PRINT_SERVER_URL, printerDetail.getPrintServerURL());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PRINTER_NAME, printerDetail.getPrinterName());

        if (!isUpdate) {
            rowId = database.insertOrThrow(DBHelper.TABLE_PRINTER_DETAIL, null, contentValues);
            printerDetail.setPrinterId(printerId);
        } else {
            rowId = database.update(DBHelper.TABLE_PRINTER_DETAIL, contentValues, DBHelper.COL_PRINTER_DETAIL_PRINTER_ID + " = :primaryKey", new String[]{String.valueOf(printerId)});
        }
        closeDBConnection();
        return rowId;
    }

    public long addPrinterDetail(PrinterDetail printerDetail) {
        openDBConnection();
        long rowId = 0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PRINTER_ID, printerDetail.getPrinterId());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_TITLE, printerDetail.getTitle());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PRINTER_TYPE, printerDetail.getPrinterType());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PRINTER_BRAND, printerDetail.getPrinterBrand());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PRINTER_MODEL, printerDetail.getPrinterModel());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PRINTER_IP, printerDetail.getPrinterIp());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PORT, printerDetail.getPort());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PRINT_SERVER_URL, printerDetail.getPrintServerURL());
        contentValues.put(DBHelper.COL_PRINTER_DETAIL_PRINTER_NAME, printerDetail.getPrinterName());

        rowId = database.insert(DBHelper.TABLE_PRINTER_DETAIL, null, contentValues);

        closeDBConnection();
        return rowId;
    }

    public void deleteSyncData(Date dt) {
        openDBConnection();

        database.delete(DBHelper.TABLE_ORDER_CHILD, "order_master_id in (select order_master_id from order_master where send_status_id = 2 and order_device_date < " + dt.getTime() + ")", null);
        database.delete(DBHelper.TABLE_ORDER_MASTER, "send_status_id = 2 and order_device_date < " + dt.getTime(), null);
        database.delete(DBHelper.TABLE_SHIFT_RECORD, "send_status_id = 2 and shift_date < " + dt.getTime(), null);

        closeDBConnection();
    }

    public int deleteOrderMaster(String primaryKey) {
        openDBConnection();
        int rows = 0;
        rows += database.delete(DBHelper.TABLE_ORDER_MASTER, DBHelper.COL_ORDER_MASTER_ORDER_MASTER_ID + "=?", new String[]{primaryKey});
        rows += database.delete(DBHelper.TABLE_ORDER_CHILD, DBHelper.COL_ORDER_CHILD_ORDER_MASTER_ID + "=?", new String[]{primaryKey});
        closeDBConnection();
        return rows;
    }


    public int deleteOrderChild(String primaryKey) {
        openDBConnection();
        int rows = 0;
        rows = database.delete(DBHelper.TABLE_ORDER_CHILD, DBHelper.COL_ORDER_CHILD_ORDER_CHILD_ID + "=?", new String[]{primaryKey});
        closeDBConnection();
        return rows;
    }

    public int deletePrinterDetail(String primaryKey) {
        openDBConnection();
        int rows = 0;
        rows = database.delete(DBHelper.TABLE_PRINTER_DETAIL, DBHelper.COL_PRINTER_DETAIL_PRINTER_ID + "=?", new String[]{primaryKey});
        closeDBConnection();
        return rows;
    }

    public boolean updateOrderMasterUploaded(String primaryKeys) {
        openDBConnection();

        database.execSQL("update " + DBHelper.TABLE_ORDER_MASTER + " set " + DBHelper.COL_ORDER_MASTER_SEND_STATUS_ID + " = 2 where " + DBHelper.COL_ORDER_MASTER_ORDER_MASTER_ID + " in(" + primaryKeys + ")");
        closeDBConnection();
        return true;
    }

    public List<SpinnerItem> getReceiptList(String criteria, String orderBy) {

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        if (!orderBy.equals("")) {
            orderBy = " order by " + orderBy;
        }
        List<SpinnerItem> spinnerItems = new ArrayList<SpinnerItem>();

        openDBConnection();
        String strqry = "select order_master_id,device_receipt_no,customer_name from " + DBHelper.TABLE_ORDER_MASTER + criteria + orderBy;
        Cursor cursor = database.rawQuery(strqry, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            spinnerItems.add(new SpinnerItem(cursor.getString(0), "Rcpt # " + cursor.getString(1) + ((cursor.getString(2) != null && cursor.getString(2).length() > 0) ? " (" + cursor.getString(2) + ")" : "")));
            cursor.moveToNext();
        }
        closeDBConnection();
        return spinnerItems;
    }

    public int getMaxReceiptNo() {
        int receiptNo = 0;

        openDBConnection();
        String strqry = "select ifnull(max(" + DBHelper.COL_ORDER_MASTER_DEVICE_RECEIPT_NO + "),0) from " + DBHelper.TABLE_ORDER_MASTER;
        Cursor cursor = database.rawQuery(strqry, null);
        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {
            receiptNo = cursor.getInt(0);
        }
        closeDBConnection();
        return receiptNo;
    }

    public int getShiftActiveTransactionCount(String shiftRecordId) {
        int transactionCount = 0;

        openDBConnection();
        String strqry = "select count(*) from " + DBHelper.TABLE_ORDER_MASTER + " where " + DBHelper.COL_ORDER_MASTER_STATUS_ID + " = 1 and " + DBHelper.COL_ORDER_MASTER_SHIFT_RECORD_ID + " = '" + shiftRecordId + "'";
        Cursor cursor = database.rawQuery(strqry, null);
        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {
            transactionCount = cursor.getInt(0);
        }
        closeDBConnection();
        return transactionCount;
    }

    public DataRow getCustomerByContactNo(String contactNo) {
        String strqry = "select customer_name,customer_address,customer_contact_no from order_master where customer_contact_no='" + contactNo + "' COLLATE NOCASE";

        return getDataRow(strqry, null);
    }

    public DataTable getShiftRegisterItems(String ShiftRecordId) {
        String strQry = "select order_child.dish_id, category_name, order_child.dish_name, ifnull(sum(quantity),0) quantity, (ifnull(sum(order_child.total_amount),0)-ifnull(sum(order_child.discount_amount),0)) amount, ifnull(sum(order_child.discount_amount),0) discount_amount, ifnull(sum(order_child.net_amount),0) net_amount, ifnull(sum(order_child.total_amount),0) total_amount " +
                "from order_master inner join " +
                "order_child on order_child.order_master_id = order_master.order_master_id inner join " +
                "dish_detail on dish_detail.dish_id = order_child.dish_id inner join " +
                "category_detail on category_detail.category_id = dish_detail.category_id " +
                "where order_master.status_id = 2 and order_master.shift_record_id = :shift_record_id " +
                "group by order_child.dish_id " +
                "order by category_name, order_child.dish_name";

        String[] parameters = new String[]{ShiftRecordId};

        return getDataTable(strQry, parameters);
    }

    public DataTable getShiftRegisterItemVariants(String ShiftRecordId) {
        String strQry = "select order_child.dish_id, order_child.dish_name, ifnull(order_child.quantity,0) quantity, order_child.variant_data " +
                "from order_master inner join " +
                "order_child on order_child.order_master_id = order_master.order_master_id " +
                "where order_master.status_id = 2 and order_master.shift_record_id = :shift_record_id " +
                "order by order_child.dish_id";

        String[] parameters = new String[]{ShiftRecordId};

        return getDataTable(strQry, parameters);
    }

    public DataTable getShiftRegisterItemsSummary(String ShiftRecordId, int statusId) {
        String strQry = "select device_receipt_no, count(*) no_of_items ,ifnull(sum(quantity),0) quantity, (ifnull(sum(order_child.total_amount),0)-ifnull(sum(order_child.discount_amount),0)) amount " +
                "from order_master inner join " +
                "order_child on order_child.order_master_id = order_master.order_master_id " +
                "where order_master.status_id = " + statusId + " and order_master.order_type_id != 5 and order_master.shift_record_id = :shift_record_id " +
                "group by order_master.device_receipt_no, order_master.order_master_id";

        String[] parameters = new String[]{ShiftRecordId};

        return getDataTable(strQry, parameters);
    }

    public DataRow getShiftRegisterSummary(String ShiftRecordId, int statusId) {
        String strQry = "select ifnull(min(start_time),0) start_time, ifnull(min(finish_time),0) finish_time, ifnull(min(opening_cash),0) opening_cash, ifnull(min(closing_cash),0) closing_cash, ifnull(sum(sub_total_amount),0) sub_total_amount, ifnull(sum(total_amount),0) total_amount, ifnull(sum(product_discount_amount),0) product_discount_amount, ifnull(sum(discount_amount),0) discount_amount, (ifnull(sum(sub_total_amount),0)-ifnull(sum(discount_amount),0)) net_sale, ifnull(sum(sales_tax_amount),0) sales_tax_amount, ifnull(sum(tip),0) tip_amount, ifnull(sum(delivery_fee_amount),0) delivery_fee_amount, (ifnull(sum(cash_amount),0)-ifnull(sum(change_amount),0)) cash_amount, ifnull(sum(card_amount),0) card_amount, " +
                "(select count(*) from order_master cash_trans where cash_trans.cash_amount > 0 and cash_trans.status_id = " + statusId + " and cash_trans.shift_record_id = shift_record.shift_record_id) no_of_cash_transactions, " +
                "(select count(*) from order_master card_trans where card_trans.card_amount > 0 and card_trans.status_id = " + statusId + " and card_trans.shift_record_id = shift_record.shift_record_id) no_of_card_transactions " +
                "from shift_record left outer join " +
                "order_master on order_master.shift_record_id = shift_record.shift_record_id and order_master.status_id = " + statusId + " " +
                "where shift_record.shift_record_id = :shift_record_id " +
                "group by shift_record.shift_record_id";

        String[] parameters = new String[]{ShiftRecordId};

        return getDataRow(strQry, parameters);
    }


    public DataTable getShiftRegisterCardSummary(String ShiftRecordId) {
        String strQry = "select card_type, count(*) no_of_transactions " +
                "from order_master " +
                "where order_master.status_id = 2 and order_master.card_amount > 0 and order_master.shift_record_id = :shift_record_id " +
                "group by order_master.card_type";

        String[] parameters = new String[]{ShiftRecordId};

        return getDataTable(strQry, parameters);
    }

    public DataTable getShiftRegisterDeliverySummary(String ShiftRecordId) {
        String strQry = "select delivery_company_name, count(*) no_of_transactions, ifnull(sum(total_amount),0) total_amount " +
                "from order_master inner join " +
                "delivery_company on delivery_company.delivery_company_id = order_master.delivery_company " +
                "where order_master.status_id = 2 and order_master.shift_record_id = :shift_record_id " +
                "group by delivery_company_name";

        String[] parameters = new String[]{ShiftRecordId};

        return getDataTable(strQry, parameters);
    }

    public DataTable getShiftRegisterOrderTypeSummary(String ShiftRecordId) {
        String strQry = "select table_name, count(*) no_of_transactions, ifnull(sum(total_amount),0) total_amount " +
                "from order_master " +
                "where order_master.status_id = 2 and order_master.shift_record_id = :shift_record_id " +
                "group by table_name";

        String[] parameters = new String[]{ShiftRecordId};

        return getDataTable(strQry, parameters);
    }

    public DataTable getDayRegisterItems(Date ShiftDate) {
        String strQry = "select dish_name, ifnull(sum(quantity),0) quantity, (ifnull(sum(order_child.total_amount),0)-ifnull(sum(order_child.discount_amount),0)) amount, ifnull(sum(order_child.discount_amount),0) discount_amount, ifnull(sum(order_child.net_amount),0) net_amount, ifnull(sum(order_child.total_amount),0) total_amount " +
                "from order_master inner join " +
                "order_child on order_child.order_master_id = order_master.order_master_id inner join " +
                "shift_record on shift_record.shift_record_id = order_master.shift_record_id " +
                "where order_master.status_id = 2 and shift_record.shift_date = :shift_date " +
                "group by order_child.dish_id " +
                "order by dish_name";

        String[] parameters = new String[]{String.valueOf(ShiftDate.getTime())};

        return getDataTable(strQry, parameters);
    }

    public DataRow getDayRegisterSummary(Date ShiftDate) {
        String strQry = "select ifnull(min(start_time),0) start_time, ifnull(max(finish_time),0) finish_time, ifnull(min(opening_cash),0) opening_cash, ifnull(min(closing_cash),0) closing_cash, ifnull(sum(sub_total_amount),0) sub_total_amount, ifnull(sum(total_amount),0) total_amount, ifnull(sum(product_discount_amount),0) product_discount_amount, ifnull(sum(discount_amount),0) discount_amount, (ifnull(sum(sub_total_amount),0)-ifnull(sum(discount_amount),0)) net_sale, ifnull(sum(sales_tax_amount),0) sales_tax_amount, ifnull(sum(tip),0) tip_amount, ifnull(sum(delivery_fee_amount),0) delivery_fee_amount, (ifnull(sum(cash_amount),0)-ifnull(sum(change_amount),0)) cash_amount, ifnull(sum(card_amount),0) card_amount " +
                "from shift_record left outer join " +
                "order_master on order_master.shift_record_id = shift_record.shift_record_id and order_master.status_id = 2 " +
                "where shift_record.shift_date = :shift_date " +
                "group by shift_record.shift_date";

        String[] parameters = new String[]{String.valueOf(ShiftDate.getTime())};

        return getDataRow(strQry, parameters);
    }

    public DataRow getSyncDataDetail() {
        String strqry = "select " +
                "(select count(*) order_master_count from order_master where status_id = 2 and send_status_id != 2) order_master_count, " +
                "(select count(*) shift_record_count from shift_record where status_id = 2 and send_status_id != 2) shift_record_count ";

        return getDataRow(strqry, null);
    }

    public boolean clearOrderData() {
        openDBConnection();

        database.execSQL(DBHelper.DROP_TABLE_ORDER_CARD_DETAIL);
        database.execSQL(DBHelper.DROP_TABLE_ORDER_CHILD);
        database.execSQL(DBHelper.DROP_TABLE_ORDER_MASTER);
        database.execSQL(DBHelper.DROP_TABLE_SHIFT_RECORD);

        database.execSQL(DBHelper.CREATE_TABLE_ORDER_MASTER);
        database.execSQL(DBHelper.CREATE_TABLE_ORDER_CHILD);
        database.execSQL(DBHelper.CREATE_TABLE_ORDER_CARD_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_SHIFT_RECORD);

        closeDBConnection();
        return true;
    }

    public boolean clearData() {
        openDBConnection();


        database.execSQL(DBHelper.DROP_TABLE_SETTING_DETAIL);
        database.execSQL(DBHelper.DROP_TABLE_SYNC_DETAIL);
        database.execSQL(DBHelper.DROP_TABLE_PLACE_DETAIL);
        database.execSQL(DBHelper.DROP_TABLE_USER_DETAIL);
        database.execSQL(DBHelper.DROP_TABLE_FLOOR_DETAIL);
        database.execSQL(DBHelper.DROP_TABLE_TABLE_DETAIL);
        database.execSQL(DBHelper.DROP_TABLE_CATEGORY_DETAIL);
        database.execSQL(DBHelper.DROP_TABLE_DISH_DETAIL);
        database.execSQL(DBHelper.DROP_TABLE_BANK_PROMOTION);
        database.execSQL(DBHelper.DROP_TABLE_WAITER);
        database.execSQL(DBHelper.DROP_TABLE_ORDER_CARD_DETAIL);
        database.execSQL(DBHelper.DROP_TABLE_ORDER_CHILD);
        database.execSQL(DBHelper.DROP_TABLE_ORDER_MASTER);
        database.execSQL(DBHelper.DROP_TABLE_SHIFT_RECORD);
        database.execSQL(DBHelper.DROP_TABLE_PRINTER_DETAIL);
        database.execSQL(DBHelper.DROP_TABLE_DISH_PRINT_DETAIL);

        database.execSQL(DBHelper.CREATE_TABLE_SETTING_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_SYNC_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_PLACE_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_USER_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_FLOOR_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_TABLE_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_CATEGORY_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_DISH_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_BANK_PROMOTION);
        database.execSQL(DBHelper.CREATE_TABLE_WAITER);
        database.execSQL(DBHelper.CREATE_TABLE_ORDER_MASTER);
        database.execSQL(DBHelper.CREATE_TABLE_ORDER_CHILD);
        database.execSQL(DBHelper.CREATE_TABLE_ORDER_CARD_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_SHIFT_RECORD);
        database.execSQL(DBHelper.CREATE_TABLE_PRINTER_DETAIL);
        database.execSQL(DBHelper.CREATE_TABLE_DISH_PRINT_DETAIL);

        closeDBConnection();
        return true;
    }


    public DataTable getDataTable(String query, String[] parameters) {
        DataTable dataTable;
        openDBConnection();
        Cursor cursor = database.rawQuery(query, parameters);
        dataTable = new DataTable();
        dataTable.fillByCursor(cursor);
        closeDBConnection();
        return dataTable;
    }

    public DataRow getDataRow(String query, String[] parameters) {
        DataRow dataRow = null;
        DataTable dataTable = getDataTable(query, parameters);
        if (dataTable.size() > 0)
            dataRow = dataTable.get(0);
        return dataRow;
    }

    public int executeUpdate(String tableName, ContentValues contentValues, String primaryKeyColumn, long primaryKey) {
        openDBConnection();
        int rows;
        rows = database.update(tableName, contentValues, primaryKeyColumn + " = :primaryKey", new String[]{String.valueOf(primaryKey)});
        closeDBConnection();
        return rows;
    }

    public int executeUpdate(String tableName, ContentValues contentValues, String criteria) {
        openDBConnection();
        int rows;
        rows = database.update(tableName, contentValues, criteria, null);
        closeDBConnection();
        return rows;
    }

    public String getScalarValue(String tableName, String ColumnName, String criteria) {
        String value = "";

        if (!criteria.equals("")) {
            criteria = " where " + criteria;
        }

        openDBConnection();
        String strqry = "select " + ColumnName + " from " + tableName + criteria;
        Cursor cursor = database.rawQuery(strqry, null);
        cursor.moveToFirst();

        if (!cursor.isAfterLast()) {
            value = cursor.getString(0);
        }
        closeDBConnection();
        return value;
    }

}
