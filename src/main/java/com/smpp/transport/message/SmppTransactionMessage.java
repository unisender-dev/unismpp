package com.smpp.transport.message;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rado on 8/10/15.
 */
public abstract class SmppTransactionMessage implements Serializable {

    private String smsId;

    private String phoneNumber;

    private String sender;

    private String userGUID;

    private Integer serverId;

    private String serverError;

    private boolean multipartFlag;

    private Date reportedDateTime;

    private Date dateTimeSentToSmpp;

    private Date dateTimeReceiveFinalState;

//    protected StatusType statusType;

    public SmppTransactionMessage() {

    }

    public SmppTransactionMessage(SMSMessage smsMessage, Integer serverId,
                                  String serverError,
                                  boolean multipartFlag) {

        this.smsId = smsMessage.getSmsId();
        this.phoneNumber = smsMessage.getPhone();
        this.sender = smsMessage.getSender();
        this.userGUID = smsMessage.getUserID();
        this.serverId = serverId;
        this.serverError = serverError;
        this.multipartFlag = multipartFlag;
        this.reportedDateTime = new Date();
    }

    public SmppTransactionMessage(SMSMessage smsMessage, Integer serverId,
                                  String serverError,
                                  boolean multipartFlag,
                                  Date dateTimeSentToSmpp,
                                  Date dateTimeReceiveFinalState) {

        this.smsId = smsMessage.getSmsId();
        this.phoneNumber = smsMessage.getPhone();
        this.sender = smsMessage.getSender();
        this.userGUID = smsMessage.getUserID();
        this.serverId = serverId;
        this.serverError = serverError;
        this.multipartFlag = multipartFlag;
        this.reportedDateTime = new Date();
        this.dateTimeSentToSmpp = dateTimeSentToSmpp;
        this.dateTimeReceiveFinalState = dateTimeReceiveFinalState;
    }

    public SmppTransactionMessage(String smsId, String phoneNumber,
                                  String sender,
                                  String userGUID, Integer serverId,
                                  String serverError, String campaignID,
                                  boolean multipartFlag) {
        this.smsId = smsId;
        this.phoneNumber = phoneNumber;
        this.sender = sender;
        this.userGUID = userGUID;
        this.serverId = serverId;
        this.serverError = serverError;
        this.multipartFlag = multipartFlag;
        this.reportedDateTime = new Date();
    }

    public SmppTransactionMessage(String smsId, String phoneNumber,
                                  String sender,
                                  String userGUID, Integer serverId,
                                  String serverError, String campaignID,
                                  boolean multipartFlag, Date dateTimeSentToSmpp,
                                  Date dateTimeReceiveFinalState) {
        this.smsId = smsId;
        this.phoneNumber = phoneNumber;
        this.sender = sender;
        this.userGUID = userGUID;
        this.serverId = serverId;
        this.serverError = serverError;
        this.multipartFlag = multipartFlag;
        this.reportedDateTime = new Date();
        this.dateTimeSentToSmpp = dateTimeSentToSmpp;
        this.dateTimeReceiveFinalState = dateTimeReceiveFinalState;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getUserGUID() {
        return userGUID;
    }

    public void setUserGUID(String userGUID) {
        this.userGUID = userGUID;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getServerError() {
        return serverError;
    }

    public void setServerError(String serverError) {
        this.serverError = serverError;
    }

    public boolean isMultipartFlag() {
        return multipartFlag;
    }

    public void setMultipartFlag(boolean multipartFlag) {
        this.multipartFlag = multipartFlag;
    }

    public Date getReportedDateTime() {
        return reportedDateTime;
    }

    public void setReportedDateTime(Date reportedDateTime) {
        this.reportedDateTime = reportedDateTime;
    }

    public Date getDateTimeSentToSmpp() {
        return dateTimeSentToSmpp;
    }

    public void setDateTimeSentToSmpp(Date dateTimeSentToSmpp) {
        this.dateTimeSentToSmpp = dateTimeSentToSmpp;
    }

    public Date getDateTimeReceiveFinalState() {
        return dateTimeReceiveFinalState;
    }

    public void setDateTimeReceiveFinalState(Date dateTimeReceiveFinalState) {
        this.dateTimeReceiveFinalState = dateTimeReceiveFinalState;
    }

    /*public abstract void setStatusType(StatusType statusType);

    public abstract StatusType getStatusType();*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SmppTransactionMessage that = (SmppTransactionMessage) o;

        if (!smsId.equals(that.smsId)) return false;
        if (!phoneNumber.equals(that.phoneNumber)) return false;
        return sender.equals(that.sender);

    }

    @Override
    public int hashCode() {
        int result = smsId.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + sender.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder("")
                .append("smsid : ")
                .append(smsId)
                .append(", from: ")
                .append(sender)
                .append(", to: ")
                .append(phoneNumber)
                .append("; user guid: ")
                .append(userGUID)
                .append("; serverError : ")
                .append(serverError)
                .append("; serverId : ")
                .append(serverId)
                .append("; multipartFlag: ")
                .append(multipartFlag)
                .toString();
    }

}
