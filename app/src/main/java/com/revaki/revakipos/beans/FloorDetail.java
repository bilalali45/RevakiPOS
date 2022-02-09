package com.revaki.revakipos.beans;

public class FloorDetail {
    private String FloorId;
    private String FloorName;
    private String Discription = null;
    private int OrderTypeId;
    private boolean IsShowTable;

    public FloorDetail() {

    }

    public FloorDetail(String FloorId, String FloorName, String Discription, int OrderTypeId, boolean IsShowTable) {
        this.FloorId = FloorId;
        this.FloorName = FloorName;
        this.Discription = Discription;
        this.OrderTypeId = OrderTypeId;
        this.IsShowTable = IsShowTable;
    }

    public String getFloorId() {
        return FloorId;
    }

    public String getFloorName() {
        return FloorName;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setFloorId(String FloorId) {
        this.FloorId = FloorId;
    }

    public void setFloorName(String FloorName) {
        this.FloorName = FloorName;
    }

    public void setDiscription(String Discription) {
        this.Discription = Discription;
    }

    public int getOrderTypeId() {
        return OrderTypeId;
    }

    public void setOrderTypeId(int orderTypeId) {
        OrderTypeId = orderTypeId;
    }

    public boolean isShowTable() {
        return IsShowTable;
    }

    public void setShowTable(boolean showTable) {
        IsShowTable = showTable;
    }
}
