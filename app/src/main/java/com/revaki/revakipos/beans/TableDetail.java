package com.revaki.revakipos.beans;

public class TableDetail {
    private String TableId;
    private String TableName;
    private int Capacity;
    private String FloorId;
    private String Discription = null;
    private int ActiveBillCount = 0;

    public TableDetail() {

    }

    public TableDetail(String TableId,String TableName,int Capacity,String FloorId, String Discription,int ActiveBillCount) {
        this.TableId=TableId;
        this.TableName=TableName;
        this.Capacity=Capacity;
        this.FloorId = FloorId;
        this.Discription = Discription;
        this.ActiveBillCount=ActiveBillCount;
    }

    public String getTableId() {
        return TableId;
    }

    public void setTableId(String tableId) {
        TableId = tableId;
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public String getFloorId() {
        return FloorId;
    }

    public void setFloorId(String floorId) {
        FloorId = floorId;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public int getActiveBillCount() {
        return ActiveBillCount;
    }

    public void setActiveBillCount(int activeBillCount) {
        ActiveBillCount = activeBillCount;
    }

    @Override
    public String toString() {
        return TableName;
    }
}