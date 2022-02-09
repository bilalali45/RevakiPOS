package com.revaki.revakipos.beans;

public class WaiterDetail {

    public String WaiterId;
    public String WaiterName;

    public WaiterDetail() {

    }

    public WaiterDetail(String waiterId, String waiterName) {
        WaiterId = waiterId;
        WaiterName = waiterName;
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

    @Override
    public String toString() {
        return WaiterName;
    }
}
