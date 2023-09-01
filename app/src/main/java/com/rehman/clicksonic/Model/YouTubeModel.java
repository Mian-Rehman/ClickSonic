package com.rehman.clicksonic.Model;

public class YouTubeModel
{
    String fullName,link,Subscriber,likes,views,Followers,userUID,Status,OrderDate,OrderID,OrderTime;
    int cost,updatedCoins;

    // Default constructor with no arguments
    public YouTubeModel() {
        // Default constructor is required for Firestore deserialization
    }




    public YouTubeModel(String fullName, String OrderDate, String OrderTime, String OrderID, String link, String Subscriber, String likes, String views, String Followers, String userUID, String Status, int cost, int updatedCoins) {
        this.fullName = fullName;
        this.link = link;
        this.Subscriber = Subscriber;
        this.Followers = Followers;
        this.userUID = userUID;
        this.Status = Status;
        this.OrderDate = OrderDate;
        this.OrderID = OrderID;
        this.OrderTime = OrderTime;
        this.updatedCoins = updatedCoins;
        this.likes = likes;
        this.views = views;
        this.cost = cost;
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

    public int getUpdatedCoins() {
        return updatedCoins;
    }

    public void setUpdatedCoins(int updatedCoins) {
        this.updatedCoins = updatedCoins;
    }
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getFollowers() {
        return Followers;
    }

    public void setFollowers(String followers) {
        Followers = followers;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    public String getSubscriber() {
        return Subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.Subscriber = subscriber;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }


}
