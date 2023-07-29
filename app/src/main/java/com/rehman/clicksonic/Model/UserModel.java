package com.rehman.clicksonic.Model;

public class UserModel
{
    String fullName,email,password,loginWith,profileImageLink,userUID;
    int coin,points,bonus;
    boolean isGetNewAccountBonus = false;

    public UserModel(String fullName, String email, String password, String loginWith, String profileImageLink, String userUID, int coin, int points, int bonus, boolean isGetNewAccountBonus) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.loginWith = loginWith;
        this.profileImageLink = profileImageLink;
        this.userUID = userUID;
        this.coin = coin;
        this.points = points;
        this.bonus = bonus;
        this.isGetNewAccountBonus = isGetNewAccountBonus;
    }

    public UserModel() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginWith() {
        return loginWith;
    }

    public void setLoginWith(String loginWith) {
        this.loginWith = loginWith;
    }

    public String getProfileImageLink() {
        return profileImageLink;
    }

    public void setProfileImageLink(String profileImageLink) {
        this.profileImageLink = profileImageLink;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public boolean isGetNewAccountBonus() {
        return isGetNewAccountBonus;
    }

    public void setGetNewAccountBonus(boolean getNewAccountBonus) {
        isGetNewAccountBonus = getNewAccountBonus;
    }
}
