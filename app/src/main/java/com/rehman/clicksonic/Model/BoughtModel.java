package com.rehman.clicksonic.Model;

public class BoughtModel
{
    String dateOfPurchase,moneySpent,offerBought,userUID;

    public BoughtModel(String dateOfPurchase, String moneySpent, String offerBought, String userUID) {
        this.dateOfPurchase = dateOfPurchase;
        this.moneySpent = moneySpent;
        this.offerBought = offerBought;
        this.userUID = userUID;
    }

    public BoughtModel() {
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(String moneySpent) {
        this.moneySpent = moneySpent;
    }

    public String getOfferBought() {
        return offerBought;
    }

    public void setOfferBought(String offerBought) {
        this.offerBought = offerBought;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}
