package com.revaki.revakipos.beans;

public class PlaceDetail {
    private String PlaceId;
    private String Title;
    private String ContactInfo;
    private String Address;
    private String Email;
    private String Phone;
    private String Website;
    private String PrintLogoName;
    private String PrintLogoImage;
    private String STRN;
    private String NTN;
    private String GSTPercentage;
    private  String CardGSTPercentage;
    private String BottomText;
    private String UANNumber;
    private String DeliveryCharges;
    private String GSTTitle;
    private int GSTDeductionType;
    private int GSTDeductionOnFullDiscount;
    private String StartShiftDefaultAmount;
    private boolean AllowTableMultipleReceipts;
    private int FreeDeliveryAfterAmount = 0;
    private boolean PrintKitchenSummary;

    public PlaceDetail() {
    }

    public PlaceDetail(String placeId, String title, String contactInfo, String address, String email, String phone, String website, String printLogoName, String printLogoImage, String STRN, String NTN, String GSTPercentage,String CardGSTPercentage, String bottomText, String UANNumber, String deliveryCharges, String gSTTitle, int gSTDeductionType, int gSTDeductionOnFullDiscount, String startShiftDefaultAmount, boolean allowTableMultipleReceipts,int freeDeliveryAfterAmount, boolean printKitchenSummary) {
        PlaceId = placeId;
        Title = title;
        ContactInfo = contactInfo;
        Address = address;
        Email = email;
        Phone = phone;
        Website = website;
        PrintLogoName = printLogoName;
        PrintLogoImage = printLogoImage;
        this.STRN = STRN;
        this.NTN = NTN;
        this.GSTPercentage = GSTPercentage;
        this.CardGSTPercentage = CardGSTPercentage;
        BottomText = bottomText;
        this.UANNumber = UANNumber;
        this.DeliveryCharges = deliveryCharges;
        this.GSTTitle = gSTTitle;
        this.GSTDeductionType = gSTDeductionType;
        this.GSTDeductionOnFullDiscount = gSTDeductionOnFullDiscount;
        this.StartShiftDefaultAmount = startShiftDefaultAmount;
        this.AllowTableMultipleReceipts = allowTableMultipleReceipts;
        this.FreeDeliveryAfterAmount = freeDeliveryAfterAmount;
        this.PrintKitchenSummary = printKitchenSummary;
    }

    public String getPlaceId() {
        return PlaceId;
    }

    public void setPlaceId(String placeId) {
        PlaceId = placeId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContactInfo() {
        return ContactInfo;
    }

    public void setContactInfo(String contactInfo) {
        ContactInfo = contactInfo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getPrintLogoName() {
        return PrintLogoName;
    }

    public void setPrintLogoName(String printLogoName) {
        PrintLogoName = printLogoName;
    }

    public String getPrintLogoImage() {
        return PrintLogoImage;
    }

    public void setPrintLogoImage(String printLogoImage) {
        PrintLogoImage = printLogoImage;
    }

    public String getSTRN() {
        return STRN;
    }

    public void setSTRN(String STRN) {
        this.STRN = STRN;
    }

    public String getNTN() {
        return NTN;
    }

    public void setNTN(String NTN) {
        this.NTN = NTN;
    }

    public String getGSTPercentage() {
        return GSTPercentage;
    }

    public void setGSTPercentage(String GSTPercentage) {
        this.GSTPercentage = GSTPercentage;
    }

    public String getCardGSTPercentage() {
        return CardGSTPercentage;
    }

    public void setCardGSTPercentage(String cardGSTPercentage) {
        CardGSTPercentage = cardGSTPercentage;
    }

    public String getBottomText() {
        return BottomText;
    }

    public void setBottomText(String bottomText) {
        BottomText = bottomText;
    }

    public String getUANNumber() {
        return UANNumber;
    }

    public void setUANNumber(String UANNumber) {
        this.UANNumber = UANNumber;
    }

    public String getDeliveryCharges() {
        return DeliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        DeliveryCharges = deliveryCharges;
    }

    public String getGSTTitle() {
        return GSTTitle;
    }

    public void setGSTTitle(String GSTTitle) {
        this.GSTTitle = GSTTitle;
    }

    public int getGSTDeductionType() {
        return GSTDeductionType;
    }

    public void setGSTDeductionType(int GSTDeductionType) {
        this.GSTDeductionType = GSTDeductionType;
    }

    public int getGSTDeductionOnFullDiscount() {
        return GSTDeductionOnFullDiscount;
    }

    public void setGSTDeductionOnFullDiscount(int GSTDeductionOnFullDiscount) {
        this.GSTDeductionOnFullDiscount = GSTDeductionOnFullDiscount;
    }

    public String getStartShiftDefaultAmount() {
        return StartShiftDefaultAmount;
    }

    public void setStartShiftDefaultAmount(String startShiftDefaultAmount) {
        StartShiftDefaultAmount = startShiftDefaultAmount;
    }

    public boolean isAllowTableMultipleReceipts() {
        return AllowTableMultipleReceipts;
    }

    public void setAllowTableMultipleReceipts(boolean allowTableMultipleReceipts) {
        AllowTableMultipleReceipts = allowTableMultipleReceipts;
    }

    public int getFreeDeliveryAfterAmount() {
        return FreeDeliveryAfterAmount;
    }

    public void setFreeDeliveryAfterAmount(int freeDeliveryAfterAmount) {
        FreeDeliveryAfterAmount = freeDeliveryAfterAmount;
    }

    public boolean isPrintKitchenSummary() {
        return PrintKitchenSummary;
    }

    public void setPrintKitchenSummary(boolean printKitchenSummary) {
        PrintKitchenSummary = printKitchenSummary;
    }
}