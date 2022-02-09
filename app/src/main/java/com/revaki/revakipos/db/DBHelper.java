package com.revaki.revakipos.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RevakiPOS.db";
    private static final int DATABASE_VERSION = 9;

    public static final String TABLE_SETTING_DETAIL = "setting_detail";

    public static final String COL_SETTING_DETAIL_SETTING_DETAIL_ID = "setting_detail_id";
    public static final String COL_SETTING_DETAIL_DEFAULT_PRINTER_ID = "default_printer_id";
    public static final String COL_SETTING_DETAIL_KITCHEN_PRINTER_ID = "kitchen_printer_id";
    public static final String COL_SETTING_DETAIL_KITCHEN_PRINT_COPY = "kitchen_print_copy";
    public static final String COL_SETTING_DETAIL_KITCHEN_PRINTER_CATEGORIES = "kitchen_printer_categories";
    public static final String COL_SETTING_DETAIL_SHOW_PRE_BILL_PREVIEW = "show_pre_bill_preview";
    public static final String COL_SETTING_DETAIL_SHOW_POST_BILL_PREVIEW = "show_post_bill_preview";
    public static final String COL_SETTING_DETAIL_SHOW_KITCHEN_PRINT_PREVIEW = "show_kitchen_print_preview";
    public static final String COL_SETTING_DETAIL_AUTO_PRINT_AFTER_CHECKOUT = "auto_print_after_checkout";
    public static final String COL_SETTING_DETAIL_OPEN_CASH_DRAWER_AFTER_CHECKOUT = "open_cash_drawer_after_checkout";


    public static final String TABLE_SYNC_DETAIL = "sync_detail";

    public static final String COL_SYNC_DETAIL_TABLE_NAME = "table_name";
    public static final String COL_SYNC_DETAIL_TABLE_TITLE = "table_title";
    public static final String COL_SYNC_DETAIL_TABLE_TYPE = "table_type";
    public static final String COL_SYNC_DETAIL_TABLE_ORDER = "table_order";
    public static final String COL_SYNC_DETAIL_RECORD_COUNT = "record_count";
    public static final String COL_SYNC_DETAIL_LAST_SYNCED_ON = "last_synced_on";


    public static final String TABLE_PLACE_DETAIL = "place_detail";

    public static final String COL_PLACE_DETAIL_PLACE_ID = "place_id";
    public static final String COL_PLACE_DETAIL_TITLE = "title";
    public static final String COL_PLACE_DETAIL_CONTACT_INFO = "contact_info";
    public static final String COL_PLACE_DETAIL_ADDRESS = "address";
    public static final String COL_PLACE_DETAIL_EMAIL = "email";
    public static final String COL_PLACE_DETAIL_PHONE = "phone";
    public static final String COL_PLACE_DETAIL_WEBSITE = "website";
    public static final String COL_PLACE_DETAIL_PRINT_LOGO_NAME = "print_logo_name";
    public static final String COL_PLACE_DETAIL_PRINT_LOGO_IMAGE = "print_logo_image";
    public static final String COL_PLACE_DETAIL_STRN = "strn";
    public static final String COL_PLACE_DETAIL_NTN = "ntn";
    public static final String COL_PLACE_DETAIL_GST_PERCENTAGE = "gst_percentage";
    public static final String COL_PLACE_DETAIL_CARD_GST_PERCENTAGE = "card_gst_percentage";
    public static final String COL_PLACE_DETAIL_BOTTOM_TEXT = "bottom_text";
    public static final String COL_PLACE_DETAIL_UAN_NUMBER = "uan_number";
    public static final String COL_PLACE_DETAIL_DELIVERY_CHARGES = "delivery_charges";
    public static final String COL_PLACE_DETAIL_GST_TITLE = "gst_title";
    public static final String COL_PLACE_DETAIL_GST_DEDUCTION_TYPE = "gst_deduction_type";
    public static final String COL_PLACE_DETAIL_GST_DEDUCTION_ON_FULL_DISCOUNT = "gst_deduction_on_full_discount";
    public static final String COL_PLACE_DETAIL_START_SHIFT_DEFAULT_AMOUNT = "start_shift_default_amount";
    public static final String COL_PLACE_DETAIL_ALLOW_TABLE_MULTIPLE_RECEIPTS = "allow_table_multiple_receipts";
    public static final String COL_PLACE_DETAIL_FREE_DELIVERY_AFTER_AMOUNT = "free_delivery_after_amount";
    public static final String COL_PLACE_DETAIL_PRINT_KITCHEN_SUMMARY = "print_kitchen_summary";


    public static final String TABLE_USER_DETAIL = "user_detail";

    public static final String COL_USER_DETAIL_USER_ID = "user_id";
    public static final String COL_USER_DETAIL_FIRST_NAME = "first_name";
    public static final String COL_USER_DETAIL_LAST_NAME = "last_name";
    public static final String COL_USER_DETAIL_USERNAME = "username";
    public static final String COL_USER_DETAIL_PASSWORD = "password";
    public static final String COL_USER_DETAIL_HASH_PASSWORD = "hash_password";
    public static final String COL_USER_DETAIL_HASH_POS_KEY = "pos_key";
    public static final String COL_USER_DETAIL_HASH_MODIFICATION_KEY = "modification_key";


    public static final String TABLE_FLOOR_DETAIL = "floor_detail";

    public static final String COL_FLOOR_DETAIL_FLOOR_ID = "floor_id";
    public static final String COL_FLOOR_DETAIL_FLOOR_NAME = "floor_name";
    public static final String COL_FLOOR_DETAIL_DESCRIPTION = "discription";
    public static final String COL_FLOOR_DETAIL_ORDER_TYPE_ID = "order_type_id";
    public static final String COL_FLOOR_DETAIL_IS_SHOW_TABLE = "is_show_table";


    public static final String TABLE_TABLE_DETAIL = "table_detail";

    public static final String COL_TABLE_DETAIL_TABLE_ID = "table_id";
    public static final String COL_TABLE_DETAIL_TABLE_NAME = "table_name";
    public static final String COL_TABLE_DETAIL_CAPACITY = "capacity";
    public static final String COL_TABLE_DETAIL_FLOOR_ID = "floor_id";
    public static final String COL_TABLE_DETAIL_DESCRIPTION = "discription";


    public static final String TABLE_CATEGORY_DETAIL = "category_detail";

    public static final String COL_CATEGORY_DETAIL_CATEGORY_ID = "category_id";
    public static final String COL_CATEGORY_DETAIL_CATEGORY_NAME = "category_name";
    public static final String COL_CATEGORY_DETAIL_IMAGE_URL = "image_url";
    public static final String COL_CATEGORY_DETAIL_TYPE = "type";
    public static final String COL_CATEGORY_DETAIL_DESCRIPTION = "discription";


    public static final String TABLE_DISH_DETAIL = "dish_detail";

    public static final String COL_DISH_DETAIL_DISH_ID = "dish_id";
    public static final String COL_DISH_DETAIL_DISH_NAME = "dish_name";
    public static final String COL_DISH_DETAIL_CATEGORY_ID = "category_id";
    public static final String COL_DISH_DETAIL_IMAGE_URL = "image_url";
    public static final String COL_DISH_DETAIL_BARCODE = "barcode";
    public static final String COL_DISH_DETAIL_IN_MINUTE = "minute";
    public static final String COL_DISH_DETAIL_IN_SECOND = "second";
    public static final String COL_DISH_DETAIL_WEIGHT_IN_GRAM = "weight_in_gram";
    public static final String COL_DISH_DETAIL_TOTAL_PRICE = "total_price";
    public static final String COL_DISH_DETAIL_PRICE_START_FROM = "price_start_from";
    public static final String COL_DISH_DETAIL_APPLY_GST = "apply_gst";
    public static final String COL_DISH_DETAIL_APPLY_DISCOUNT = "apply_discount";
    public static final String COL_DISH_DETAIL_DESCRIPTION = "discription";
    public static final String COL_DISH_DETAIL_VARIANT_DATA = "variant_data";


    public static final String TABLE_BANK_PROMOTION = "bank_promotion";

    public static final String COL_BANK_PROMOTION_BANK_DETAIL_ID = "bank_detail_id";
    public static final String COL_BANK_PROMOTION_BANK_NAME = "bank_name";
    public static final String COL_BANK_DISCOUNT_PERCENTAGE = "discount_percentage";
    public static final String COL_BANK_DISCOUNT_AMOUNT = "discount_amount";
    public static final String COL_BANK_START_DATE = "start_date";
    public static final String COL_BANK_END_DATE = "end_date";


    public static final String TABLE_SHIFT_RECORD = "shift_record";

    public static final String COL_SHIFT_RECORD_SHIFT_RECORD_ID = "shift_record_id";
    public static final String COL_SHIFT_RECORD_USER_ID = "user_id";
    public static final String COL_SHIFT_RECORD_SHIFT_TYPE_ID = "shift_type_id";
    public static final String COL_SHIFT_RECORD_SHIFT_DATE = "shift_date";
    public static final String COL_SHIFT_RECORD_START_TIME = "start_time";
    public static final String COL_SHIFT_RECORD_FINISH_TIME = "finish_time";
    public static final String COL_SHIFT_RECORD_OPENING_CASH = "opening_cash";
    public static final String COL_SHIFT_RECORD_CLOSING_CASH = "closing_cash";
    public static final String COL_SHIFT_RECORD_COMMENTS = "comments";
    public static final String COL_SHIFT_RECORD_STATUS_ID = "status_id";
    public static final String COL_SHIFT_RECORD_SEND_STATUS_ID = "send_status_id";


    public static final String TABLE_SHIFT_TYPE = "shift_type";

    public static final String COL_SHIFT_TYPE_SHIFT_TYPE_ID = "shift_type_id";
    public static final String COL_SHIFT_TYPE_SHIFT_TYPE_NAME = "shift_type_name";


    public static final String TABLE_DELIVERY_COMPANY = "delivery_company";

    public static final String COL_DELIVERY_COMPANY_DELIVERY_COMPANY_ID = "delivery_company_id";
    public static final String COL_DELIVERY_COMPANY_DELIVERY_COMPANY_NAME = "delivery_company_name";


    public static final String TABLE_CUSTOMER = "customer";

    public static final String COL_CUSTOMER_CUSTOMER_ID = "customer_id";
    public static final String COL_CUSTOMER_FULL_NAME = "full_name";
    public static final String COL_CUSTOMER_CONTACT_NO = "contact_no";
    public static final String COL_CUSTOMER_ADDRESS = "address";
    public static final String COL_CUSTOMER_ADDRESS_DATA = "address_data";
    public static final String COL_CUSTOMER_REWARD_BALANCE = "reward_balance";
    public static final String COL_CUSTOMER_SEND_STATUS_ID = "send_status_id";

    public static final String TABLE_WAITER = "waiter";

    public static final String COL_WAITER_WAITER_ID = "waiter_id";
    public static final String COL_WAITER_WAITER_NAME = "waiter_name";


    public static final String TABLE_ORDER_MASTER = "order_master";

    public static final String COL_ORDER_MASTER_ORDER_MASTER_ID = "order_master_id";
    public static final String COL_ORDER_MASTER_ORDER_DEVICE_DATE = "order_device_date";
    public static final String COL_ORDER_MASTER_CREATION_DEVICE_DATETIME = "creation_device_datetime";
    public static final String COL_ORDER_MASTER_DEVICE_RECEIPT_NO = "device_receipt_no";
    public static final String COL_ORDER_MASTER_ORDER_TYPE_ID = "order_type_id";
    public static final String COL_ORDER_MASTER_TABLE_ID = "table_id";
    public static final String COL_ORDER_MASTER_TABLE_NAME = "table_name";
    public static final String COL_ORDER_MASTER_WAITER_ID = "waiter_id";
    public static final String COL_ORDER_MASTER_WAITER_NAME = "waiter_name";
    public static final String COL_ORDER_MASTER_NO_OF_PERSON = "no_of_person";
    public static final String COL_ORDER_MASTER_CUSTOMER_ID = "customer_id";
    public static final String COL_ORDER_MASTER_CUSTOMER_NAME = "customer_name";
    public static final String COL_ORDER_MASTER_CUSTOMER_CONTACT_NO = "customer_contact_no";
    public static final String COL_ORDER_MASTER_CUSTOMER_ADDRESS = "customer_address";
    public static final String COL_ORDER_MASTER_DISCOUNT_TYPE_ID = "discount_type_id";
    public static final String COL_ORDER_MASTER_DISCOUNT_PERCENTAGE = "discount_percentage";
    public static final String COL_ORDER_MASTER_DISCOUNT_AMOUNT = "discount_amount";
    public static final String COL_ORDER_MASTER_PRODUCT_DISCOUNT_AMOUNT = "product_discount_amount";
    public static final String COL_ORDER_MASTER_COMPLEMENTARY_AMOUNT = "complementary_amount";
    public static final String COL_ORDER_MASTER_BANK_PROMOTION_ID = "bank_promotion_id";
    public static final String COL_ORDER_MASTER_BANK_PROMOTION_DISCOUNT_AMOUNT = "bank_promotion_discount_amount";
    public static final String COL_ORDER_MASTER_SALES_TAX_PERCENT = "sales_tax_percent";
    public static final String COL_ORDER_MASTER_SALES_TAX_AMOUNT = "sales_tax_amount";
    public static final String COL_ORDER_MASTER_TIP = "tip";
    public static final String COL_ORDER_MASTER_SUB_TOTAL_AMOUNT = "sub_total_amount";
    public static final String COL_ORDER_MASTER_TOTAL_AMOUNT = "total_amount";
    public static final String COL_ORDER_MASTER_CASH_AMOUNT = "cash_amount";
    public static final String COL_ORDER_MASTER_CARD_AMOUNT = "card_amount";
    public static final String COL_ORDER_MASTER_CHANGE_AMOUNT = "change_amount";
    public static final String COL_ORDER_MASTER_CARD_NUMBER = "card_number";
    public static final String COL_ORDER_MASTER_CARD_TYPE = "card_type";
    public static final String COL_ORDER_MASTER_CARD_NO_OF_SPLIT = "card_no_of_split";
    public static final String COL_ORDER_MASTER_DELIVERY_FEE_AMOUNT = "delivery_fee_amount";
    public static final String COL_ORDER_MASTER_DELIVERY_COMPANY = "delivery_company";
    public static final String COL_ORDER_MASTER_DELIVERY_TYPE = "delivery_type";
    public static final String COL_ORDER_MASTER_DELIVERY_DATE = "delivery_date";
    public static final String COL_ORDER_MASTER_RIDER_NAME = "rider_name";
    public static final String COL_ORDER_MASTER_RIDER_MOBILE_NO = "rider_mobile_no";
    public static final String COL_ORDER_MASTER_RIDER_BIKE_NO = "rider_bike_no";
    public static final String COL_ORDER_MASTER_SHIFT_RECORD_ID = "shift_record_id";
    public static final String COL_ORDER_MASTER_PAYMENT_TYPE_ID = "payment_type_id";
    public static final String COL_ORDER_MASTER_USER_ID = "user_id";
    public static final String COL_ORDER_MASTER_USERNAME = "username";
    public static final String COL_ORDER_MASTER_CHECKOUT_DEVICE_DATETIME = "checkout_device_datetime";
    public static final String COL_ORDER_MASTER_STATUS_ID = "status_id";
    public static final String COL_ORDER_MASTER_SEND_STATUS_ID = "send_status_id";


    public static final String TABLE_ORDER_CHILD = "order_child";

    public static final String COL_ORDER_CHILD_ORDER_CHILD_ID = "order_child_id";
    public static final String COL_ORDER_CHILD_ORDER_MASTER_ID = "order_master_id";
    public static final String COL_ORDER_CHILD_DISH_ID = "dish_id";
    public static final String COL_ORDER_CHILD_DISH_NAME = "dish_name";
    public static final String COL_ORDER_CHILD_QUANTITY = "quantity";
    public static final String COL_ORDER_CHILD_PRICE = "price";
    public static final String COL_ORDER_CHILD_AMOUNT = "amount";
    public static final String COL_ORDER_CHILD_VARIANT_AMOUNT = "variant_amount";
    public static final String COL_ORDER_CHILD_PRINT_QUANTITY = "print_quantity";
    public static final String COL_ORDER_CHILD_SPECIAL_INSTRUCTION = "special_instruction";
    public static final String COL_ORDER_CHILD_DISCOUNT_TYPE_ID = "discount_type_id";
    public static final String COL_ORDER_CHILD_DISCOUNT_PERCENTAGE = "discount_percentage";
    public static final String COL_ORDER_CHILD_DISCOUNT_AMOUNT = "discount_amount";
    public static final String COL_ORDER_CHILD_COMPLEMENTARY_QUANTITY = "complementary_quantity";
    public static final String COL_ORDER_CHILD_COMPLEMENTARY_AMOUNT = "complementary_amount";
    public static final String COL_ORDER_CHILD_NET_AMOUNT = "net_amount";
    public static final String COL_ORDER_CHILD_TOTAL_AMOUNT = "total_amount";
    public static final String COL_ORDER_CHILD_VARIANT_DATA = "variant_data";
    public static final String COL_ORDER_CHILD_DATETIME_STAMP = "datetime_stamp";
    public static final String COL_ORDER_CHILD_APPLY_GST = "apply_gst";
    public static final String COL_ORDER_CHILD_APPLY_DISCOUNT = "apply_discount";
    public static final String COL_ORDER_CHILD_STATUS_ID = "status_id";

    public static final String TABLE_ORDER_CARD_DETAIL = "order_card_detail";

    public static final String COL_ORDER_CARD_DETAIL_CARD_DETAIL_ID = "card_detail_id";
    public static final String COL_ORDER_CARD_DETAIL_ORDER_MASTER_ID = "order_master_id";
    public static final String COL_ORDER_CARD_DETAIL_GUEST_SPLIT_NO = "guest_split_no";
    public static final String COL_ORDER_CARD_DETAIL_CARD_AMOUNT = "card_amount";
    public static final String COL_ORDER_CARD_DETAIL_CARD_NUMBER = "card_number";
    public static final String COL_ORDER_CARD_DETAIL_CARD_TYPE = "card_type";


    public static final String TABLE_DISH_PRINT_DETAIL = "dish_print_detail";

    public static final String COL_DISH_PRINT_DETAIL_DISH_ID = "dish_Id";
    public static final String COL_DISH_PRINT_DETAIL_CATEGORY_ID = "category_id";
    public static final String COL_DISH_PRINT_DETAIL_PRINTER_IP = "printer_ip";
    public static final String COL_DISH_PRINT_DETAIL_PORT = "port";


    public static final String TABLE_PRINTER_DETAIL = "printer_detail";

    public static final String COL_PRINTER_DETAIL_PRINTER_ID = "printer_id";
    public static final String COL_PRINTER_DETAIL_TITLE = "title";
    public static final String COL_PRINTER_DETAIL_PRINTER_TYPE = "printer_type";
    public static final String COL_PRINTER_DETAIL_PRINTER_BRAND = "printer_brand";
    public static final String COL_PRINTER_DETAIL_PRINTER_MODEL = "printer_model";
    public static final String COL_PRINTER_DETAIL_PRINTER_IP = "printer_ip";
    public static final String COL_PRINTER_DETAIL_PORT = "port";
    public static final String COL_PRINTER_DETAIL_PRINT_SERVER_URL = "print_server_url";
    public static final String COL_PRINTER_DETAIL_PRINTER_NAME = "printer_name";


    public static final String CREATE_TABLE_SETTING_DETAIL = "create table " + TABLE_SETTING_DETAIL + "( " +
            COL_SETTING_DETAIL_SETTING_DETAIL_ID + " text primary key, " +
            COL_SETTING_DETAIL_DEFAULT_PRINTER_ID + " text, " +
            COL_SETTING_DETAIL_KITCHEN_PRINTER_ID + " text, " +
            COL_SETTING_DETAIL_KITCHEN_PRINT_COPY + " integer, " +
            COL_SETTING_DETAIL_KITCHEN_PRINTER_CATEGORIES + " text, " +
            COL_SETTING_DETAIL_SHOW_PRE_BILL_PREVIEW + " integer not null, " +
            COL_SETTING_DETAIL_SHOW_POST_BILL_PREVIEW + " integer not null, " +
            COL_SETTING_DETAIL_SHOW_KITCHEN_PRINT_PREVIEW + " integer not null, " +
            COL_SETTING_DETAIL_AUTO_PRINT_AFTER_CHECKOUT + " integer not null, " +
            COL_SETTING_DETAIL_OPEN_CASH_DRAWER_AFTER_CHECKOUT + " integer not null " +
            " )";


    public static final String CREATE_TABLE_SYNC_DETAIL = "create table " + TABLE_SYNC_DETAIL + " ( " +
            COL_SYNC_DETAIL_TABLE_NAME + " text primary key, " +
            COL_SYNC_DETAIL_TABLE_TITLE + " text not null, " +
            COL_SYNC_DETAIL_TABLE_TYPE + " integer not null, " +
            COL_SYNC_DETAIL_TABLE_ORDER + " integer not null, " +
            COL_SYNC_DETAIL_RECORD_COUNT + " integer not null, " +
            COL_SYNC_DETAIL_LAST_SYNCED_ON + " long not null " +
            " ) ";

    public static final String CREATE_TABLE_PLACE_DETAIL = "create table " + TABLE_PLACE_DETAIL + "( " +
            COL_PLACE_DETAIL_PLACE_ID + " text primary key, " +
            COL_PLACE_DETAIL_TITLE + " text, " +
            COL_PLACE_DETAIL_CONTACT_INFO + " text, " +
            COL_PLACE_DETAIL_ADDRESS + " text, " +
            COL_PLACE_DETAIL_EMAIL + " text, " +
            COL_PLACE_DETAIL_PHONE + " text, " +
            COL_PLACE_DETAIL_WEBSITE + " text, " +
            COL_PLACE_DETAIL_PRINT_LOGO_NAME + " text, " +
            COL_PLACE_DETAIL_PRINT_LOGO_IMAGE + " text, " +
            COL_PLACE_DETAIL_STRN + " text, " +
            COL_PLACE_DETAIL_NTN + " text, " +
            COL_PLACE_DETAIL_GST_PERCENTAGE + " text, " +
            COL_PLACE_DETAIL_CARD_GST_PERCENTAGE + " text, " +
            COL_PLACE_DETAIL_BOTTOM_TEXT + " text, " +
            COL_PLACE_DETAIL_UAN_NUMBER + " text, " +
            COL_PLACE_DETAIL_DELIVERY_CHARGES + " text, " +
            COL_PLACE_DETAIL_GST_TITLE + " text, " +
            COL_PLACE_DETAIL_GST_DEDUCTION_TYPE + " integer, " +
            COL_PLACE_DETAIL_GST_DEDUCTION_ON_FULL_DISCOUNT + " integer, " +
            COL_PLACE_DETAIL_START_SHIFT_DEFAULT_AMOUNT + " text, " +
            COL_PLACE_DETAIL_ALLOW_TABLE_MULTIPLE_RECEIPTS + " integer, " +
            COL_PLACE_DETAIL_FREE_DELIVERY_AFTER_AMOUNT + " integer, " +
            COL_PLACE_DETAIL_PRINT_KITCHEN_SUMMARY + " integer " +
            " )";

    public static final String CREATE_TABLE_USER_DETAIL = "create table " + TABLE_USER_DETAIL + " ( " +
            COL_USER_DETAIL_USER_ID + " text primary key, " +
            COL_USER_DETAIL_FIRST_NAME + " text, " +
            COL_USER_DETAIL_LAST_NAME + " text, " +
            COL_USER_DETAIL_USERNAME + " text not null, " +
            COL_USER_DETAIL_PASSWORD + " text not null, " +
            COL_USER_DETAIL_HASH_PASSWORD + " text, " +
            COL_USER_DETAIL_HASH_POS_KEY + " integer, " +
            COL_USER_DETAIL_HASH_MODIFICATION_KEY + " integer " +
            " ) ";


    public static final String CREATE_TABLE_FLOOR_DETAIL = "create table " + TABLE_FLOOR_DETAIL + " ( " +
            COL_FLOOR_DETAIL_FLOOR_ID + " text primary key, " +
            COL_FLOOR_DETAIL_FLOOR_NAME + " text not null, " +
            COL_FLOOR_DETAIL_DESCRIPTION + " text, " +
            COL_FLOOR_DETAIL_ORDER_TYPE_ID + " integer, " +
            COL_FLOOR_DETAIL_IS_SHOW_TABLE + " integer " +
            " ) ";


    public static final String CREATE_TABLE_TABLE_DETAIL = "create table " + TABLE_TABLE_DETAIL + " ( " +
            COL_TABLE_DETAIL_TABLE_ID + " text primary key, " +
            COL_TABLE_DETAIL_TABLE_NAME + " text not null, " +
            COL_TABLE_DETAIL_CAPACITY + " integer, " +
            COL_TABLE_DETAIL_FLOOR_ID + " text, " +
            COL_TABLE_DETAIL_DESCRIPTION + " text " +
            " ) ";


    public static final String CREATE_TABLE_CATEGORY_DETAIL = "create table " + TABLE_CATEGORY_DETAIL + " ( " +
            COL_CATEGORY_DETAIL_CATEGORY_ID + " text primary key, " +
            COL_CATEGORY_DETAIL_CATEGORY_NAME + " text not null, " +
            COL_CATEGORY_DETAIL_IMAGE_URL + " text, " +
            COL_CATEGORY_DETAIL_TYPE + " text, " +
            COL_CATEGORY_DETAIL_DESCRIPTION + " text " +
            " ) ";


    public static final String CREATE_TABLE_DISH_DETAIL = "create table " + TABLE_DISH_DETAIL + " ( " +
            COL_DISH_DETAIL_DISH_ID + " text primary key, " +
            COL_DISH_DETAIL_DISH_NAME + " text not null, " +
            COL_DISH_DETAIL_CATEGORY_ID + " text, " +
            COL_DISH_DETAIL_IMAGE_URL + " text, " +
            COL_DISH_DETAIL_BARCODE + " text, " +
            COL_DISH_DETAIL_IN_MINUTE + " integer, " +
            COL_DISH_DETAIL_IN_SECOND + " integer, " +
            COL_DISH_DETAIL_WEIGHT_IN_GRAM + " text, " +
            COL_DISH_DETAIL_TOTAL_PRICE + " text, " +
            COL_DISH_DETAIL_PRICE_START_FROM + " text, " +
            COL_DISH_DETAIL_APPLY_GST + " integer, " +
            COL_DISH_DETAIL_APPLY_DISCOUNT + " integer, " +
            COL_DISH_DETAIL_DESCRIPTION + " text, " +
            COL_DISH_DETAIL_VARIANT_DATA + " text " +
            " ) ";


    public static final String CREATE_TABLE_BANK_PROMOTION = "create table " + TABLE_BANK_PROMOTION + " ( " +
            COL_BANK_PROMOTION_BANK_DETAIL_ID + " text primary key, " +
            COL_BANK_PROMOTION_BANK_NAME + " text not null, " +
            COL_BANK_DISCOUNT_PERCENTAGE + " integer, " +
            COL_BANK_DISCOUNT_AMOUNT + " integer, " +
            COL_BANK_START_DATE + " long, " +
            COL_BANK_END_DATE + " long " +
            " ) ";

    public static final String CREATE_TABLE_WAITER = "create table " + TABLE_WAITER + " ( " +
            COL_WAITER_WAITER_ID + " text primary key, " +
            COL_WAITER_WAITER_NAME + " text not null " +
            " ) ";

    public static final String CREATE_TABLE_ORDER_MASTER = "create table " + TABLE_ORDER_MASTER + " ( " +
            COL_ORDER_MASTER_ORDER_MASTER_ID + " text primary key, " +
            COL_ORDER_MASTER_ORDER_DEVICE_DATE + " long, " +
            COL_ORDER_MASTER_CREATION_DEVICE_DATETIME + " long, " +
            COL_ORDER_MASTER_DEVICE_RECEIPT_NO + " integer not null, " +
            COL_ORDER_MASTER_ORDER_TYPE_ID + " integer not null, " +
            COL_ORDER_MASTER_TABLE_ID + " text, " +
            COL_ORDER_MASTER_TABLE_NAME + " text, " +
            COL_ORDER_MASTER_WAITER_ID + " text, " +
            COL_ORDER_MASTER_WAITER_NAME + " text, " +
            COL_ORDER_MASTER_NO_OF_PERSON + " integer, " +
            COL_ORDER_MASTER_CUSTOMER_ID + " text, " +
            COL_ORDER_MASTER_CUSTOMER_NAME + " text, " +
            COL_ORDER_MASTER_CUSTOMER_CONTACT_NO + " text, " +
            COL_ORDER_MASTER_CUSTOMER_ADDRESS + " text, " +
            COL_ORDER_MASTER_DISCOUNT_TYPE_ID + " integer not null, " +
            COL_ORDER_MASTER_DISCOUNT_PERCENTAGE + " text, " +
            COL_ORDER_MASTER_DISCOUNT_AMOUNT + " text, " +
            COL_ORDER_MASTER_PRODUCT_DISCOUNT_AMOUNT + " text, " +
            COL_ORDER_MASTER_COMPLEMENTARY_AMOUNT + " text, " +
            COL_ORDER_MASTER_BANK_PROMOTION_ID + " text, " +
            COL_ORDER_MASTER_BANK_PROMOTION_DISCOUNT_AMOUNT + " text, " +
            COL_ORDER_MASTER_SALES_TAX_PERCENT + " text, " +
            COL_ORDER_MASTER_SALES_TAX_AMOUNT + " text, " +
            COL_ORDER_MASTER_TIP + " text, " +
            COL_ORDER_MASTER_SUB_TOTAL_AMOUNT + " text, " +
            COL_ORDER_MASTER_TOTAL_AMOUNT + " text, " +
            COL_ORDER_MASTER_CASH_AMOUNT + " text, " +
            COL_ORDER_MASTER_CARD_AMOUNT + " text, " +
            COL_ORDER_MASTER_CHANGE_AMOUNT + " text, " +
            COL_ORDER_MASTER_CARD_NUMBER + " text, " +
            COL_ORDER_MASTER_CARD_TYPE + " text, " +
            COL_ORDER_MASTER_CARD_NO_OF_SPLIT + " integer, " +
            COL_ORDER_MASTER_DELIVERY_FEE_AMOUNT + " text, " +
            COL_ORDER_MASTER_DELIVERY_COMPANY + " text, " +
            COL_ORDER_MASTER_DELIVERY_TYPE + " integer, " +
            COL_ORDER_MASTER_DELIVERY_DATE + " long, " +
            COL_ORDER_MASTER_RIDER_NAME + " text, " +
            COL_ORDER_MASTER_RIDER_MOBILE_NO + " text, " +
            COL_ORDER_MASTER_RIDER_BIKE_NO + " text, " +
            COL_ORDER_MASTER_SHIFT_RECORD_ID + " text, " +
            COL_ORDER_MASTER_PAYMENT_TYPE_ID + " integer, " +
            COL_ORDER_MASTER_USER_ID + " text not null, " +
            COL_ORDER_MASTER_USERNAME + " text not null, " +
            COL_ORDER_MASTER_CHECKOUT_DEVICE_DATETIME + " long, " +
            COL_ORDER_MASTER_STATUS_ID + " integer not null, " +
            COL_ORDER_MASTER_SEND_STATUS_ID + " integer not null " +
            " ) ";


    public static final String CREATE_TABLE_ORDER_CHILD = "create table " + TABLE_ORDER_CHILD + " ( " +
            COL_ORDER_CHILD_ORDER_CHILD_ID + " text primary key, " +
            COL_ORDER_CHILD_ORDER_MASTER_ID + " text not null, " +
            COL_ORDER_CHILD_DISH_ID + " text not null, " +
            COL_ORDER_CHILD_DISH_NAME + " text, " +
            COL_ORDER_CHILD_QUANTITY + " text, " +
            COL_ORDER_CHILD_PRICE + " text, " +
            COL_ORDER_CHILD_AMOUNT + " text, " +
            COL_ORDER_CHILD_VARIANT_AMOUNT + " text, " +
            COL_ORDER_CHILD_PRINT_QUANTITY + " text, " +
            COL_ORDER_CHILD_SPECIAL_INSTRUCTION + " text, " +
            COL_ORDER_CHILD_DISCOUNT_TYPE_ID + " integer not null, " +
            COL_ORDER_CHILD_DISCOUNT_PERCENTAGE + " text, " +
            COL_ORDER_CHILD_DISCOUNT_AMOUNT + " text, " +
            COL_ORDER_CHILD_COMPLEMENTARY_QUANTITY + " text, " +
            COL_ORDER_CHILD_COMPLEMENTARY_AMOUNT + " text, " +
            COL_ORDER_CHILD_NET_AMOUNT + " text, " +
            COL_ORDER_CHILD_TOTAL_AMOUNT + " text, " +
            COL_ORDER_CHILD_VARIANT_DATA + " text, " +
            COL_ORDER_CHILD_DATETIME_STAMP + " long, " +
            COL_ORDER_CHILD_APPLY_GST + " integer, " +
            COL_ORDER_CHILD_APPLY_DISCOUNT + " integer, " +
            COL_ORDER_CHILD_STATUS_ID + " integer not null, " +
            " foreign key (" + COL_ORDER_CHILD_ORDER_MASTER_ID + ") references " + TABLE_ORDER_MASTER + " (" + COL_ORDER_MASTER_ORDER_MASTER_ID + ") " +
            " ) ";


    public static final String CREATE_TABLE_ORDER_CARD_DETAIL = "create table " + TABLE_ORDER_CARD_DETAIL + " ( " +
            COL_ORDER_CARD_DETAIL_CARD_DETAIL_ID + " text primary key, " +
            COL_ORDER_CARD_DETAIL_ORDER_MASTER_ID + " text not null, " +
            COL_ORDER_CARD_DETAIL_GUEST_SPLIT_NO + " integer, " +
            COL_ORDER_CARD_DETAIL_CARD_AMOUNT + " text, " +
            COL_ORDER_CARD_DETAIL_CARD_NUMBER + " text, " +
            COL_ORDER_CARD_DETAIL_CARD_TYPE + " text, " +
            " foreign key (" + COL_ORDER_CARD_DETAIL_ORDER_MASTER_ID + ") references " + TABLE_ORDER_MASTER + " (" + COL_ORDER_MASTER_ORDER_MASTER_ID + ") " +
            " ) ";


    public static final String CREATE_TABLE_SHIFT_RECORD = "create table " + TABLE_SHIFT_RECORD + " ( " +
            COL_SHIFT_RECORD_SHIFT_RECORD_ID + " text primary key, " +
            COL_SHIFT_RECORD_USER_ID + " text not null, " +
            COL_SHIFT_RECORD_SHIFT_TYPE_ID + " text, " +
            COL_SHIFT_RECORD_SHIFT_DATE + " long, " +
            COL_SHIFT_RECORD_START_TIME + " long, " +
            COL_SHIFT_RECORD_FINISH_TIME + " long, " +
            COL_SHIFT_RECORD_OPENING_CASH + " text, " +
            COL_SHIFT_RECORD_CLOSING_CASH + " text, " +
            COL_SHIFT_RECORD_COMMENTS + " text, " +
            COL_SHIFT_RECORD_STATUS_ID + " integer not null, " +
            COL_SHIFT_RECORD_SEND_STATUS_ID + " integer not null " +
            " ) ";


    public static final String CREATE_TABLE_SHIFT_TYPE = "create table " + TABLE_SHIFT_TYPE + " ( " +
            COL_SHIFT_TYPE_SHIFT_TYPE_ID + " text primary key, " +
            COL_SHIFT_TYPE_SHIFT_TYPE_NAME + " text not null " +
            " ) ";


    public static final String CREATE_TABLE_DELIVERY_COMPANY = "create table " + TABLE_DELIVERY_COMPANY + " ( " +
            COL_DELIVERY_COMPANY_DELIVERY_COMPANY_ID + " text primary key, " +
            COL_DELIVERY_COMPANY_DELIVERY_COMPANY_NAME + " text not null " +
            " ) ";


    public static final String CREATE_TABLE_CUSTOMER = "create table " + TABLE_CUSTOMER + " ( " +
            COL_CUSTOMER_CUSTOMER_ID + " text primary key, " +
            COL_CUSTOMER_FULL_NAME + " text not null, " +
            COL_CUSTOMER_CONTACT_NO + " text not null, " +
            COL_CUSTOMER_ADDRESS + " text, " +
            COL_CUSTOMER_ADDRESS_DATA + " text, " +
            COL_CUSTOMER_REWARD_BALANCE + " text, " +
            COL_CUSTOMER_SEND_STATUS_ID + " integer " +
            " ) ";


    public static final String CREATE_TABLE_PRINTER_DETAIL = "create table " + TABLE_PRINTER_DETAIL + "( " +
            COL_PRINTER_DETAIL_PRINTER_ID + " text primary key, " +
            COL_PRINTER_DETAIL_TITLE + " text, " +
            COL_PRINTER_DETAIL_PRINTER_TYPE + " text not null, " +
            COL_PRINTER_DETAIL_PRINTER_BRAND + " text not null, " +
            COL_PRINTER_DETAIL_PRINTER_MODEL + " text not null, " +
            COL_PRINTER_DETAIL_PRINTER_IP + " text not null, " +
            COL_PRINTER_DETAIL_PORT + " integer not null, " +
            COL_PRINTER_DETAIL_PRINT_SERVER_URL + " text not null, " +
            COL_PRINTER_DETAIL_PRINTER_NAME + " text not null " +
            " )";


    public static final String CREATE_TABLE_DISH_PRINT_DETAIL = "create table " + TABLE_DISH_PRINT_DETAIL + "( " +
            COL_DISH_PRINT_DETAIL_DISH_ID + " text primary key, " +
            COL_DISH_PRINT_DETAIL_CATEGORY_ID + " text not null, " +
            COL_DISH_PRINT_DETAIL_PRINTER_IP + " text not null, " +
            COL_DISH_PRINT_DETAIL_PORT + " integer not null " +
            " )";


    public static final String DROP_TABLE_SETTING_DETAIL = "drop table if exists " + TABLE_SETTING_DETAIL;
    public static final String DROP_TABLE_SYNC_DETAIL = "drop table if exists " + TABLE_SYNC_DETAIL;
    public static final String DROP_TABLE_PLACE_DETAIL = "drop table if exists " + TABLE_PLACE_DETAIL;
    public static final String DROP_TABLE_USER_DETAIL = "drop table if exists " + TABLE_USER_DETAIL;
    public static final String DROP_TABLE_FLOOR_DETAIL = "drop table if exists " + TABLE_FLOOR_DETAIL;
    public static final String DROP_TABLE_TABLE_DETAIL = "drop table if exists " + TABLE_TABLE_DETAIL;
    public static final String DROP_TABLE_CATEGORY_DETAIL = "drop table if exists " + TABLE_CATEGORY_DETAIL;
    public static final String DROP_TABLE_DISH_DETAIL = "drop table if exists " + TABLE_DISH_DETAIL;
    public static final String DROP_TABLE_BANK_PROMOTION = "drop table if exists " + TABLE_BANK_PROMOTION;
    public static final String DROP_TABLE_WAITER = "drop table if exists " + TABLE_WAITER;
    public static final String DROP_TABLE_ORDER_MASTER = "drop table if exists " + TABLE_ORDER_MASTER;
    public static final String DROP_TABLE_ORDER_CHILD = "drop table if exists " + TABLE_ORDER_CHILD;
    public static final String DROP_TABLE_ORDER_CARD_DETAIL = "drop table if exists " + TABLE_ORDER_CARD_DETAIL;
    public static final String DROP_TABLE_SHIFT_RECORD = "drop table if exists " + TABLE_SHIFT_RECORD;
    public static final String DROP_TABLE_SHIFT_TYPE = "drop table if exists " + TABLE_SHIFT_TYPE;
    public static final String DROP_TABLE_DELIVERY_COMPANY = "drop table if exists " + TABLE_DELIVERY_COMPANY;
    public static final String DROP_TABLE_CUSTOMER = "drop table if exists " + TABLE_CUSTOMER;
    public static final String DROP_TABLE_PRINTER_DETAIL = "drop table if exists " + TABLE_PRINTER_DETAIL;
    public static final String DROP_TABLE_DISH_PRINT_DETAIL = "drop table if exists " + TABLE_DISH_PRINT_DETAIL;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_SETTING_DETAIL);
        db.execSQL(CREATE_TABLE_SYNC_DETAIL);
        db.execSQL(CREATE_TABLE_PLACE_DETAIL);
        db.execSQL(CREATE_TABLE_USER_DETAIL);
        db.execSQL(CREATE_TABLE_FLOOR_DETAIL);
        db.execSQL(CREATE_TABLE_TABLE_DETAIL);
        db.execSQL(CREATE_TABLE_CATEGORY_DETAIL);
        db.execSQL(CREATE_TABLE_DISH_DETAIL);
        db.execSQL(CREATE_TABLE_BANK_PROMOTION);
        db.execSQL(CREATE_TABLE_WAITER);
        db.execSQL(CREATE_TABLE_ORDER_MASTER);
        db.execSQL(CREATE_TABLE_ORDER_CHILD);
        db.execSQL(CREATE_TABLE_ORDER_CARD_DETAIL);
        db.execSQL(CREATE_TABLE_SHIFT_RECORD);
        db.execSQL(CREATE_TABLE_SHIFT_TYPE);
        db.execSQL(CREATE_TABLE_DELIVERY_COMPANY);
        db.execSQL(CREATE_TABLE_CUSTOMER);
        db.execSQL(CREATE_TABLE_PRINTER_DETAIL);
        db.execSQL(CREATE_TABLE_DISH_PRINT_DETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String UPGRADE_SQL = "";
        if (oldVersion < 2) {
            UPGRADE_SQL = "ALTER TABLE " + TABLE_SETTING_DETAIL + " ADD COLUMN " + COL_SETTING_DETAIL_AUTO_PRINT_AFTER_CHECKOUT + " integer not null DEFAULT 0;";
            upgradeDb(db, UPGRADE_SQL);
            UPGRADE_SQL = "ALTER TABLE " + TABLE_SETTING_DETAIL + " ADD COLUMN " + COL_SETTING_DETAIL_OPEN_CASH_DRAWER_AFTER_CHECKOUT + " integer not null DEFAULT 0;";
            upgradeDb(db, UPGRADE_SQL);

            UPGRADE_SQL = "ALTER TABLE " + TABLE_ORDER_MASTER + " ADD COLUMN " + COL_ORDER_MASTER_CARD_NO_OF_SPLIT + " integer;";
            upgradeDb(db, UPGRADE_SQL);

            upgradeDb(db, CREATE_TABLE_ORDER_CARD_DETAIL);
        }
        if (oldVersion < 3) {
            UPGRADE_SQL = "ALTER TABLE " + TABLE_PLACE_DETAIL + " ADD COLUMN " + COL_PLACE_DETAIL_GST_DEDUCTION_TYPE + " integer;";
            upgradeDb(db, UPGRADE_SQL);
            UPGRADE_SQL = "ALTER TABLE " + TABLE_PLACE_DETAIL + " ADD COLUMN " + COL_PLACE_DETAIL_GST_DEDUCTION_ON_FULL_DISCOUNT + " integer;";
            upgradeDb(db, UPGRADE_SQL);
            UPGRADE_SQL = "ALTER TABLE " + TABLE_PLACE_DETAIL + " ADD COLUMN " + COL_PLACE_DETAIL_START_SHIFT_DEFAULT_AMOUNT + " text;";
            upgradeDb(db, UPGRADE_SQL);
            UPGRADE_SQL = "ALTER TABLE " + TABLE_PLACE_DETAIL + " ADD COLUMN " + COL_PLACE_DETAIL_ALLOW_TABLE_MULTIPLE_RECEIPTS + " integer;";
            upgradeDb(db, UPGRADE_SQL);
        }
        if (oldVersion < 4) {
            UPGRADE_SQL = "ALTER TABLE " + TABLE_PLACE_DETAIL + " ADD COLUMN " + COL_PLACE_DETAIL_GST_TITLE + " text;";
            upgradeDb(db, UPGRADE_SQL);
        }
        if (oldVersion < 5) {
            UPGRADE_SQL = "ALTER TABLE " + TABLE_PLACE_DETAIL + " ADD COLUMN " + COL_PLACE_DETAIL_CARD_GST_PERCENTAGE + " text;";
            upgradeDb(db, UPGRADE_SQL);
            UPGRADE_SQL = "ALTER TABLE " + TABLE_ORDER_MASTER + " ADD COLUMN " + COL_ORDER_MASTER_PAYMENT_TYPE_ID + " integer;";
            upgradeDb(db, UPGRADE_SQL);
        }
        if (oldVersion < 6) {
            UPGRADE_SQL = "ALTER TABLE " + TABLE_PLACE_DETAIL + " ADD COLUMN " + COL_PLACE_DETAIL_FREE_DELIVERY_AFTER_AMOUNT + " integer;";
            upgradeDb(db, UPGRADE_SQL);
            UPGRADE_SQL = "ALTER TABLE " + TABLE_DISH_DETAIL + " ADD COLUMN " + COL_DISH_DETAIL_PRICE_START_FROM + " text;";
            upgradeDb(db, UPGRADE_SQL);
            UPGRADE_SQL = "ALTER TABLE " + TABLE_ORDER_CHILD + " ADD COLUMN " + COL_ORDER_CHILD_VARIANT_AMOUNT + " text;";
            upgradeDb(db, UPGRADE_SQL);
        }

        if (oldVersion < 7) {
            UPGRADE_SQL = "ALTER TABLE " + TABLE_PLACE_DETAIL + " ADD COLUMN " + COL_PLACE_DETAIL_PRINT_KITCHEN_SUMMARY + " integer;";
            upgradeDb(db, UPGRADE_SQL);
        }

        if (oldVersion < 8) {
            UPGRADE_SQL = "ALTER TABLE " + TABLE_ORDER_CHILD + " ADD COLUMN " + COL_ORDER_CHILD_DATETIME_STAMP + " long;";
            upgradeDb(db, UPGRADE_SQL);
            UPGRADE_SQL = "ALTER TABLE " + TABLE_ORDER_CHILD + " ADD COLUMN " + COL_ORDER_CHILD_STATUS_ID + " integer;";
            upgradeDb(db, UPGRADE_SQL);
        }

        if (oldVersion < 9) {
            UPGRADE_SQL = "ALTER TABLE " + TABLE_DISH_DETAIL + " ADD COLUMN " + COL_DISH_DETAIL_APPLY_GST + " integer;";
            upgradeDb(db, UPGRADE_SQL);
            UPGRADE_SQL = "ALTER TABLE " + TABLE_DISH_DETAIL + " ADD COLUMN " + COL_DISH_DETAIL_APPLY_DISCOUNT + " integer;";
            upgradeDb(db, UPGRADE_SQL);
            UPGRADE_SQL = "ALTER TABLE " + TABLE_ORDER_CHILD + " ADD COLUMN " + COL_ORDER_CHILD_APPLY_GST + " integer;";
            upgradeDb(db, UPGRADE_SQL);
            UPGRADE_SQL = "ALTER TABLE " + TABLE_ORDER_CHILD + " ADD COLUMN " + COL_ORDER_CHILD_APPLY_DISCOUNT + " integer;";
            upgradeDb(db, UPGRADE_SQL);
        }
    }

    private void upgradeDb(SQLiteDatabase db, String sql) {
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
