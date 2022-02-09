package com.revaki.revakipos.beans;

import java.util.Date;

public class ShiftRecord {

    private String ShiftRecordId;
    private String UserId;
    private String ShiftTypeId;
    private Date ShiftDate;
    private Date StartTime;
    private Date FinishTime;
    private String OpeningCash = "0";
    private String ClosingCash = "0";
    private String Comments;
    private int StatusId;
    private transient String ShiftType;
    private int SendStatusId;


    public ShiftRecord() {

    }

    public ShiftRecord(String shiftRecordId, String userId, String shiftTypeId, Date shiftDate, Date startTime, Date finishTime, String openingCash, String closingCash, String comments, int statusId, int sendStatusId) {
        ShiftRecordId = shiftRecordId;
        UserId = userId;
        ShiftTypeId = shiftTypeId;
        ShiftDate = shiftDate;
        StartTime = startTime;
        FinishTime = finishTime;
        OpeningCash = openingCash;
        ClosingCash = closingCash;
        Comments = comments;
        StatusId = statusId;
        SendStatusId = sendStatusId;
    }

    public String getShiftRecordId() {
        return ShiftRecordId;
    }

    public void setShiftRecordId(String shiftRecordId) {
        ShiftRecordId = shiftRecordId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getShiftTypeId() {
        return ShiftTypeId;
    }

    public void setShiftTypeId(String shiftTypeId) {
        ShiftTypeId = shiftTypeId;
    }

    public Date getShiftDate() {
        return ShiftDate;
    }

    public void setShiftDate(Date shiftDate) {
        ShiftDate = shiftDate;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setStartTime(Date startTime) {
        StartTime = startTime;
    }

    public Date getFinishTime() {
        return FinishTime;
    }

    public void setFinishTime(Date finishTime) {
        FinishTime = finishTime;
    }

    public String getOpeningCash() {
        return OpeningCash;
    }

    public void setOpeningCash(String openingCash) {
        OpeningCash = openingCash;
    }

    public String getClosingCash() {
        return ClosingCash;
    }

    public void setClosingCash(String closingCash) {
        ClosingCash = closingCash;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public int getStatusId() {
        return StatusId;
    }

    public void setStatusId(int statusId) {
        StatusId = statusId;
    }

    public String getShiftType() {
        return ShiftType;
    }

    public void setShiftType(String shiftType) {
        ShiftType = shiftType;
    }

    public int getSendStatusId() {
        return SendStatusId;
    }

    public void setSendStatusId(int sendStatusId) {
        SendStatusId = sendStatusId;
    }
}