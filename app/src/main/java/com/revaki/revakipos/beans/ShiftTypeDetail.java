package com.revaki.revakipos.beans;

public class ShiftTypeDetail {
    private String Id;
    private String ShiftType;

    public ShiftTypeDetail() {

    }

    public ShiftTypeDetail(String id, String shiftType) {
        Id = id;
        ShiftType = shiftType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getShiftType() {
        return ShiftType;
    }

    public void setShiftType(String shiftType) {
        ShiftType = shiftType;
    }
}
