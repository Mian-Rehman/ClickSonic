package com.rehman.clicksonic.Model;

public class YouTubeModel
{
    String fullName,link,Subscriber,likes,views,Followers;
    int cost;

    // Default constructor with no arguments
    public YouTubeModel() {
        // Default constructor is required for Firestore deserialization
    }

    public YouTubeModel(String fullName, String link, String Subscriber, String likes, String views,String Followers, int cost) {
        this.fullName = fullName;
        this.link = link;
        this.Subscriber = Subscriber;
        this.Followers = Followers;
        this.likes = likes;
        this.views = views;
        this.cost = cost;
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
