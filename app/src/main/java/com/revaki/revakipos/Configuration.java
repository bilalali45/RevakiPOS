package com.revaki.revakipos;

import com.revaki.revakipos.beans.PlaceDetail;
import com.revaki.revakipos.beans.User;

import java.util.Date;
import java.util.List;

public class Configuration {

    private static boolean appLoaded = false;
    private static boolean isLogin;
    private static boolean isOfflineLogin;
    private static User user;
    private static String placeId;
    private static Date currentDate;
    private static PlaceDetail placeDetail;
    private static String shiftRecordId = "";
    private static int pushChunkSize = 500;
    private static int uploadingEntriesLimit = 300;
    private static int syncInterval = 15;

    public static String getBuildVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public static boolean isAppLoaded() {
        return appLoaded;
    }

    public static void setAppLoaded(boolean appLoaded) {
        Configuration.appLoaded = appLoaded;
    }

    public static boolean isLogin() {
        return isLogin;
    }

    public static void setLogin(boolean login) {
        Configuration.isLogin = login;
    }

    public static boolean IsOfflineLogin() {
        return isOfflineLogin;
    }

    public static void setOfflineLogin(boolean offlineLogin) {
        Configuration.isOfflineLogin = offlineLogin;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Configuration.user = user;
    }

    public static String getUserId() {
        return user.getUserId();
    }


    public static String getPlaceId() {
        return placeId;
    }

    public static void setPlaceId(String placeId) {
        Configuration.placeId = placeId;
    }

    public static Date getCurrentDate() {
        return currentDate;
    }

    public static void setCurrentDate(Date currentDate) {
        Configuration.currentDate = currentDate;
    }

    public static PlaceDetail getPlaceDetail() {
        return placeDetail;
    }

    public static void setPlaceDetail(PlaceDetail placeDetail) {
        Configuration.placeDetail = placeDetail;
    }

    public static String getShiftRecordId() {
        return shiftRecordId;
    }

    public static void setShiftRecordId(String shiftRecordId) {
        Configuration.shiftRecordId = shiftRecordId;
    }

    public static int getPushChunkSize() {
        return pushChunkSize;
    }

    public static void setPushChunkSize(int pushChunkSize) {
        Configuration.pushChunkSize = pushChunkSize;
    }

    public static int getUploadingEntriesLimit() {
        return uploadingEntriesLimit;
    }

    public static void setUploadingEntriesLimit(int uploadingEntriesLimit) {
        Configuration.uploadingEntriesLimit = uploadingEntriesLimit;
    }

    public static int getSyncInterval() {
        return syncInterval;
    }

    public static void setSyncInterval(int syncInterval) {
        Configuration.syncInterval = syncInterval;
    }

}
