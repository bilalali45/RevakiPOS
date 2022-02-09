package com.revaki.revakipos.beans;

public class DeliveryCompanyDetail {
    private String Id;
    private String DeliveryCompany;

    public DeliveryCompanyDetail() {

    }

    public DeliveryCompanyDetail(String id, String deliveryCompany) {
        Id = id;
        DeliveryCompany = deliveryCompany;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDeliveryCompany() {
        return DeliveryCompany;
    }

    public void setDeliveryCompany(String deliveryCompany) {
        DeliveryCompany = deliveryCompany;
    }


    @Override
    public String toString() {
        return DeliveryCompany;
    }
}
