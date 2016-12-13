package com.smpp.transport.message;

import java.util.Date;

/**
 * User: boy
 * Date: 11/27/13
 */
public class SMSMessage extends Message {

    /**
     * sms_id - ASCII string up to 64 characters long
     */
    private String smsId;

    /**
     * sender - 14 digit or 11 letter-digit symbols
     */

    private String sender;

    /**
     * phone - phone number of receiver
     */
    private String phone;

    /**
     * priority
     */

    protected Integer priority;

    /**
     * flash - is this message flash or not.
     */
    private boolean flash;


    public SMSMessage(String smsId,
                      String phone,
                      String sender,
                      String userID,
                      String campaignID) {

        this.smsId = smsId;
        this.phone = phone;
        this.sender = sender;
        this.userID = userID;
        this.campaignID = campaignID;
    }

    public SMSMessage(String smsId, String sender, String phone, boolean flash, Integer priority, String userID,
                      String campaignID, Date date, String textBody, String templateId) {
        this.smsId = smsId;
        this.sender = sender;
        this.phone = phone;
        this.flash = flash;
        this.priority = priority;
        this.userID = userID;
        this.campaignID = campaignID;
        this.date = date;
        this.textBody = textBody;
        this.templateId = templateId;
    }

    public SMSMessage() {
    }

    public String getSmsId() {
        return smsId;
    }
    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getID() {
        return smsId;
    }

    public boolean isFlash() {
        return flash;
    }
    public void setFlash(Boolean flash) {
        this.flash = flash;
    }


    public Integer getPriority() {
        return priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public void cleanPreparedTextAndMimes() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SMSMessage that = (SMSMessage) o;

        return smsId.equals(that.smsId);
    }

    @Override
    public int hashCode() {
        return smsId.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SMSMessage {");
        sb.append("smsID:");
        sb.append(smsId);
        sb.append(", sender='");
        sb.append(sender);
        sb.append("', phone='");
        sb.append(phone);
        sb.append(", userID='");
        sb.append(userID);
        sb.append("', campaignID:");
        sb.append(campaignID);

        return sb.toString();
    }

    @Override
    public SMSMessage toLightweightMessage() {
        SMSMessage lightweightMessage = new SMSMessage(
                smsId,
                sender,
                phone,
                flash,
                priority,
                userID,
                campaignID,
                date,
                textBody,
                templateId);

        lightweightMessage.size = size;

        return lightweightMessage;
    }

    protected Long calculateMessageSize() {
        Long size = super.calculateMessageSize();

        if (sender != null)
            size += sender.length();

        if (phone != null)
            size += phone.length();

        return size;
    }

}
