package com.revaki.revakipos.beans;

import java.util.Date;

public class DayRecord {
    private Date ShiftDate;
    private int NoOfShift;

    public DayRecord() {

    }

    public DayRecord(Date shiftDate, int noOfShift) {
        ShiftDate = shiftDate;
        NoOfShift = noOfShift;
    }

    public Date getShiftDate() {
        return ShiftDate;
    }

    public void setShiftDate(Date shiftDate) {
        ShiftDate = shiftDate;
    }

    public int getNoOfShift() {
        return NoOfShift;
    }

    public void setNoOfShift(int noOfShift) {
        NoOfShift = noOfShift;
    }
}
