package com.rehman.clicksonic.Model;

public class PaymentModel {

    String OrderDate,OrderID,OrderTime,Status,verifyTID,userUID,senderNumber,paymentMethod,amountTransferred,
            order;



    public PaymentModel(String orderDate, String orderID, String orderTime, String status, String verifyTID,
                        String userUID, String senderNumber, String paymentMethod, String amountTransferred, String order) {
        this.OrderDate = orderDate;
        this.OrderID = orderID;
        this.OrderTime = orderTime;
        this.Status = status;
        this.verifyTID = verifyTID;
        this.userUID = userUID;
        this.senderNumber = senderNumber;
        this.paymentMethod = paymentMethod;
        this.amountTransferred = amountTransferred;
        this.order = order;
    }
    public PaymentModel() {
    }
    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getVerifyTID() {
        return verifyTID;
    }

    public void setVerifyTID(String verifyTID) {
        this.verifyTID = verifyTID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAmountTransferred() {
        return amountTransferred;
    }

    public void setAmountTransferred(String amountTransferred) {
        this.amountTransferred = amountTransferred;
    }
}
