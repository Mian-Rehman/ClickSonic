package com.rehman.clicksonic.Notification;

public class NotificationData {
    String notificationId, senderId, notificationType, notificationTitle, notificationMessage,notificationDate,
            notificationTime;
    Boolean seen;


    public NotificationData(String notificationId, String senderId, String notificationType, String notificationTitle, String notificationMessage, String notificationDate, String notificationTime, Boolean seen) {
        this.notificationId = notificationId;
        this.senderId = senderId;
        this.notificationType = notificationType;
        this.notificationTitle = notificationTitle;
        this.notificationMessage = notificationMessage;
        this.notificationDate = notificationDate;
        this.notificationTime = notificationTime;
        this.seen = seen;
    }

    public NotificationData() {
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}
