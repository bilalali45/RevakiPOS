package com.revaki.revakipos.beans;

public class OrderCardDetail {
    private String CardDetailId;
    private String OrderMasterId;
    private int GuestSplitNo;
    private String CardAmount = "0";
    private String CardNumber = "";
    private String CardType = "";

    public OrderCardDetail() {
    }

    public OrderCardDetail(int guestSplitNo, String cardAmount, String cardNumber, String cardType) {
        GuestSplitNo = guestSplitNo;
        CardAmount = cardAmount;
        CardNumber = cardNumber;
        CardType = cardType;
    }

    public String getCardDetailId() {
        return CardDetailId;
    }

    public void setCardDetailId(String cardDetailId) {
        CardDetailId = cardDetailId;
    }

    public String getOrderMasterId() {
        return OrderMasterId;
    }

    public void setOrderMasterId(String orderMasterId) {
        OrderMasterId = orderMasterId;
    }

    public int getGuestSplitNo() {
        return GuestSplitNo;
    }

    public void setGuestSplitNo(int guestSplitNo) {
        GuestSplitNo = guestSplitNo;
    }

    public String getCardAmount() {
        return CardAmount;
    }

    public void setCardAmount(String cardAmount) {
        CardAmount = cardAmount;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }
}


