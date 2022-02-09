package com.revaki.revakipos.beans;

import java.util.Date;

public class SyncDetail {
    private String TableName;
    private String TableTitle;
    private int TableType;
    private int TableOrder;
    private int RecordCount;
    private Date LastSyncedOn;

    public SyncDetail() {

    }

    public SyncDetail(String tableName, String tableTitle, int tableType, int tableOrder, int recordCount, Date lastSyncedOn) {
        TableName = tableName;
        TableTitle = tableTitle;
        TableType = tableType;
        TableOrder = tableOrder;
        RecordCount = recordCount;
        LastSyncedOn = lastSyncedOn;
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public String getTableTitle() {
        return TableTitle;
    }

    public void setTableTitle(String tableTitle) {
        TableTitle = tableTitle;
    }

    public int getTableType() {
        return TableType;
    }

    public void setTableType(int tableType) {
        TableType = tableType;
    }

    public int getTableOrder() {
        return TableOrder;
    }

    public void setTableOrder(int tableOrder) {
        TableOrder = tableOrder;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int recordCount) {
        RecordCount = recordCount;
    }

    public Date getLastSyncedOn() {
        return LastSyncedOn;
    }

    public void setLastSyncedOn(Date lastSyncedOn) {
        LastSyncedOn = lastSyncedOn;
    }
}
