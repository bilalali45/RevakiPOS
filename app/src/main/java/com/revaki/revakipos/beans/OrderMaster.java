package com.revaki.revakipos.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderMaster {
    private String OrderMasterId;
    private Date OrderDeviceDate;
    private Date CreationDeviceDatetime;
    private int DeviceReceiptNo;
    private int OrderTypeId;
    private String TableId;
    private String TableName;
    public String WaiterId = "";
    public String WaiterName = "";
    private int NoOfPerson;
    private String CustomerId = "";
    private String CustomerName = "";
    private String CustomerContactNo = "";
    private String CustomerAddress = "";
    private int DiscountTypeId = 1;
    private String DiscountPercentage = "0";
    private String DiscountAmount = "0";
    private String ProductDiscountAmount = "0";
    private String ComplementaryAmount = "0";
    private String BankPromotionId;
    private String BankPromotionDiscountAmount = "0";
    private String SalesTaxPercent = "0";
    private String SalesTaxAmount = "0";
    private String Tip = "0";
    private String SubTotalAmount = "0";
    private String TotalAmount = "0";
    private String CashAmount = "0";
    private String CardAmount = "0";
    private String ChangeAmount = "0";
    private String CardNumber = "";
    private String CardType = "";
    private int CardNoOfSplit;
    private String DeliveryFeeAmount = "0";
    private String DeliveryCompany = "";
    private int DeliveryType = 1;
    private Date DeliveryDate = null;
    private String RiderName = "";
    private String RiderMobileNo = "";
    private String RiderBikeNo = "";
    private String ShiftRecordId;
    private int PaymentTypeId;
    private String UserId;
    private String Username;
    private Date CheckoutDeviceDatetime = null;
    private int StatusId = 0;
    private int SendStatusId = 0;
    private transient int GSTDeductionType = 1;
    private transient int GSTDeductionOnFullDiscount = 2;
    private List<OrderChild> orderChilds = new ArrayList<OrderChild>();
    private List<OrderCardDetail> orderCardDetails = new ArrayList<OrderCardDetail>();

    public OrderMaster() {

    }

    public OrderMaster(Date orderDeviceDate, Date creationDeviceDatetime, int deviceReceiptNo, int orderTypeId, String tableId, String tableName, String waiterId, String waiterName, int noOfPerson, String customerId, String customerName, String customerContactNo, String customerAddress, int discountTypeId, String discountPercentage, String discountAmount, String productDiscountAmount, String complementaryAmount, String bankPromotionId, String bankPromotionDiscountAmount, String salesTaxPercent, String salesTaxAmount, String tip, String subTotalAmount, String totalAmount, String cashAmount, String cardAmount, String changeAmount, String cardNumber, String cardType, int cardNoOfSplit, String deliveryFeeAmount, String deliveryCompany, int deliveryType, Date deliveryDate, String riderName, String riderMobileNo, String riderBikeNo, String shiftRecordId, int paymentTypeId) {
        OrderDeviceDate = orderDeviceDate;
        CreationDeviceDatetime = creationDeviceDatetime;
        DeviceReceiptNo = deviceReceiptNo;
        OrderTypeId = orderTypeId;
        TableId = tableId;
        TableName = tableName;
        WaiterId = waiterId;
        WaiterName = waiterName;
        NoOfPerson = noOfPerson;
        CustomerId = customerId;
        CustomerName = customerName;
        CustomerContactNo = customerContactNo;
        CustomerAddress = customerAddress;
        DiscountTypeId = discountTypeId;
        DiscountPercentage = discountPercentage;
        DiscountAmount = discountAmount;
        ProductDiscountAmount = productDiscountAmount;
        ComplementaryAmount = complementaryAmount;
        BankPromotionId = bankPromotionId;
        BankPromotionDiscountAmount = bankPromotionDiscountAmount;
        SalesTaxPercent = salesTaxPercent;
        SalesTaxAmount = salesTaxAmount;
        Tip = tip;
        SubTotalAmount = subTotalAmount;
        TotalAmount = totalAmount;
        CashAmount = cashAmount;
        CardAmount = cardAmount;
        ChangeAmount = changeAmount;
        CardNumber = cardNumber;
        CardType = cardType;
        CardNoOfSplit = cardNoOfSplit;
        DeliveryFeeAmount = deliveryFeeAmount;
        DeliveryCompany = deliveryCompany;
        DeliveryType = deliveryType;
        DeliveryDate = deliveryDate;
        RiderName = riderName;
        RiderMobileNo = riderMobileNo;
        RiderBikeNo = riderBikeNo;
        ShiftRecordId = shiftRecordId;
        PaymentTypeId = paymentTypeId;
    }

    public String getOrderMasterId() {
        return OrderMasterId;
    }

    public void setOrderMasterId(String orderMasterId) {
        OrderMasterId = orderMasterId;
    }

    public Date getOrderDeviceDate() {
        return OrderDeviceDate;
    }

    public void setOrderDeviceDate(Date orderDeviceDate) {
        OrderDeviceDate = orderDeviceDate;
    }

    public Date getCreationDeviceDatetime() {
        return CreationDeviceDatetime;
    }

    public void setCreationDeviceDatetime(Date creationDeviceDatetime) {
        CreationDeviceDatetime = creationDeviceDatetime;
    }

    public int getDeviceReceiptNo() {
        return DeviceReceiptNo;
    }

    public void setDeviceReceiptNo(int deviceReceiptNo) {
        DeviceReceiptNo = deviceReceiptNo;
    }

    public int getOrderTypeId() {
        return OrderTypeId;
    }

    public void setOrderTypeId(int orderTypeId) {
        OrderTypeId = orderTypeId;
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

    public String getWaiterId() {
        return WaiterId;
    }

    public void setWaiterId(String waiterId) {
        WaiterId = waiterId;
    }

    public String getWaiterName() {
        return WaiterName;
    }

    public void setWaiterName(String waiterName) {
        WaiterName = waiterName;
    }

    public int getNoOfPerson() {
        return NoOfPerson;
    }

    public void setNoOfPerson(int noOfPerson) {
        NoOfPerson = noOfPerson;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerContactNo() {
        return CustomerContactNo;
    }

    public void setCustomerContactNo(String customerContactNo) {
        CustomerContactNo = customerContactNo;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        CustomerAddress = customerAddress;
    }

    public int getDiscountTypeId() {
        return DiscountTypeId;
    }

    public void setDiscountTypeId(int discountTypeId) {
        DiscountTypeId = discountTypeId;
    }

    public String getDiscountPercentage() {
        return DiscountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        DiscountPercentage = discountPercentage;
    }

    public String getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        DiscountAmount = discountAmount;
    }

    public String getProductDiscountAmount() {
        return ProductDiscountAmount;
    }

    public void setProductDiscountAmount(String productDiscountAmount) {
        ProductDiscountAmount = productDiscountAmount;
    }

    public String getComplementaryAmount() {
        return ComplementaryAmount;
    }

    public void setComplementaryAmount(String complementaryAmount) {
        ComplementaryAmount = complementaryAmount;
    }

    public String getBankPromotionId() {
        return BankPromotionId;
    }

    public void setBankPromotionId(String bankPromotionId) {
        BankPromotionId = bankPromotionId;
    }

    public String getBankPromotionDiscountAmount() {
        return BankPromotionDiscountAmount;
    }

    public void setBankPromotionDiscountAmount(String bankPromotionDiscountAmount) {
        BankPromotionDiscountAmount = bankPromotionDiscountAmount;
    }

    public String getSalesTaxPercent() {
        return SalesTaxPercent;
    }

    public void setSalesTaxPercent(String salesTaxPercent) {
        SalesTaxPercent = salesTaxPercent;
    }

    public String getSalesTaxAmount() {
        return SalesTaxAmount;
    }

    public void setSalesTaxAmount(String salesTaxAmount) {
        SalesTaxAmount = salesTaxAmount;
    }

    public String getTip() {
        return Tip;
    }

    public void setTip(String tip) {
        Tip = tip;
    }

    public String getSubTotalAmount() {
        return SubTotalAmount;
    }

    public void setSubTotalAmount(String subTotalAmount) {
        SubTotalAmount = subTotalAmount;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getCashAmount() {
        return CashAmount;
    }

    public void setCashAmount(String cashAmount) {
        CashAmount = cashAmount;
    }

    public String getCardAmount() {
        return CardAmount;
    }

    public void setCardAmount(String cardAmount) {
        CardAmount = cardAmount;
    }

    public String getChangeAmount() {
        return ChangeAmount;
    }

    public void setChangeAmount(String changeAmount) {
        ChangeAmount = changeAmount;
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

    public int getCardNoOfSplit() {
        return CardNoOfSplit;
    }

    public void setCardNoOfSplit(int cardNoOfSplit) {
        CardNoOfSplit = cardNoOfSplit;
    }

    public String getDeliveryFeeAmount() {
        return DeliveryFeeAmount;
    }

    public void setDeliveryFeeAmount(String deliveryFeeAmount) {
        DeliveryFeeAmount = deliveryFeeAmount;
    }

    public String getDeliveryCompany() {
        return DeliveryCompany;
    }

    public void setDeliveryCompany(String deliveryCompany) {
        DeliveryCompany = deliveryCompany;
    }

    public int getDeliveryType() {
        return DeliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        DeliveryType = deliveryType;
    }

    public Date getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getRiderName() {
        return RiderName;
    }

    public void setRiderName(String riderName) {
        RiderName = riderName;
    }

    public String getRiderMobileNo() {
        return RiderMobileNo;
    }

    public void setRiderMobileNo(String riderMobileNo) {
        RiderMobileNo = riderMobileNo;
    }

    public String getRiderBikeNo() {
        return RiderBikeNo;
    }

    public void setRiderBikeNo(String riderBikeNo) {
        RiderBikeNo = riderBikeNo;
    }

    public String getShiftRecordId() {
        return ShiftRecordId;
    }

    public void setShiftRecordId(String shiftRecordId) {
        ShiftRecordId = shiftRecordId;
    }

    public int getPaymentTypeId() {
        return PaymentTypeId;
    }

    public void setPaymentTypeId(int paymentTypeId) {
        PaymentTypeId = paymentTypeId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public Date getCheckoutDeviceDatetime() {
        return CheckoutDeviceDatetime;
    }

    public void setCheckoutDeviceDatetime(Date checkoutDeviceDatetime) {
        CheckoutDeviceDatetime = checkoutDeviceDatetime;
    }

    public int getStatusId() {
        return StatusId;
    }

    public void setStatusId(int statusId) {
        StatusId = statusId;
    }

    public int getSendStatusId() {
        return SendStatusId;
    }

    public void setSendStatusId(int sendStatusId) {
        SendStatusId = sendStatusId;
    }

    public List<OrderChild> getOrderChilds() {
        return orderChilds;
    }

    public void setOrderChilds(List<OrderChild> orderChilds) {
        this.orderChilds = orderChilds;
    }

    public List<OrderCardDetail> getOrderCardDetails() {
        return orderCardDetails;
    }

    public void setOrderCardDetails(List<OrderCardDetail> orderCardDetails) {
        this.orderCardDetails = orderCardDetails;
    }

    public float getNumberOfItems() {
        int numOfItems = 0;
        for (OrderChild orderChild : orderChilds) {
            numOfItems += Float.valueOf(orderChild.getQuantity());

        }

        return numOfItems;
    }

    public void calculateValues() {

        double subTotalAmount = 0;
        double subTotalAmountForGST = 0;
        double subTotalAmountForDiscount = 0;
        double totalAmount = 0;
        double taxableAmount = 0;
        double beforeSaleTaxAmount = 0;
        double productDiscountAmount = 0;
        double fullDiscountProductAmount = 0;
        double discountAmount = Double.valueOf("0" + DiscountAmount);
        double discountPercentage = Double.valueOf("0" + DiscountPercentage);
        double saleTaxPercent = Double.valueOf(SalesTaxPercent);
        double deliveryFeeAmount = Double.valueOf("0" + DeliveryFeeAmount);
        double tipAmount = Double.valueOf("0" + Tip);
        double saleTaxAmount = 0;

        for (OrderChild orderChild : orderChilds) {
            if (Double.valueOf(orderChild.getTotalAmount()) == 0) {
                fullDiscountProductAmount += Double.valueOf(orderChild.getDiscountAmount());
            }
            productDiscountAmount += Double.valueOf(orderChild.getDiscountAmount());
            subTotalAmount += Double.valueOf(orderChild.getTotalAmount());
            if (orderChild.isApplyGST()) {
                subTotalAmountForGST += Double.valueOf(orderChild.getTotalAmount());
            }
            if (orderChild.isApplyDiscount()) {
                subTotalAmountForDiscount += Double.valueOf(orderChild.getTotalAmount());
            }
        }

        if (subTotalAmount > 0) {
            if (DiscountTypeId == 1 && discountAmount > 0) {
                discountPercentage = (discountAmount / subTotalAmountForDiscount) * 100;
            } else if (DiscountTypeId == 2 && discountPercentage > 0) {
                discountAmount = (subTotalAmountForDiscount / 100) * discountPercentage;
            }

            discountAmount = Math.floor(discountAmount);
        } else {
            discountAmount = 0;
            discountPercentage = 0;
        }

        if (discountAmount > subTotalAmount) {
            discountAmount = subTotalAmount;
        }

        beforeSaleTaxAmount = subTotalAmount - discountAmount;
        if (GSTDeductionType == 1) {
            taxableAmount = subTotalAmountForGST + productDiscountAmount;
            if (GSTDeductionOnFullDiscount == 2 && taxableAmount >= fullDiscountProductAmount) {
                taxableAmount -= fullDiscountProductAmount;
            }
        } else {
            taxableAmount = subTotalAmountForGST - discountAmount;
        }

        if (GSTDeductionOnFullDiscount == 2 && beforeSaleTaxAmount == 0) {
            taxableAmount = 0;
        }

        if (saleTaxPercent > 0 && taxableAmount > 0) {
            saleTaxAmount = Math.ceil((taxableAmount / 100) * saleTaxPercent);
        }

        totalAmount = beforeSaleTaxAmount + saleTaxAmount + deliveryFeeAmount + tipAmount;

        setProductDiscountAmount(String.format("%.2f", productDiscountAmount).replaceFirst("\\.?0*$", ""));
        setDiscountAmount(String.format("%.2f", discountAmount).replaceFirst("\\.?0*$", ""));
        setDiscountPercentage(String.format("%.2f", discountPercentage).replaceFirst("\\.?0*$", ""));
        setSalesTaxAmount(String.format("%.2f", saleTaxAmount).replaceFirst("\\.?0*$", ""));
        setSubTotalAmount(String.format("%.2f", subTotalAmount).replaceFirst("\\.?0*$", ""));
        setTotalAmount(String.format("%.2f", totalAmount).replaceFirst("\\.?0*$", ""));
    }


}
