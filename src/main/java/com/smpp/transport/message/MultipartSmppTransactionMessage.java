package com.smpp.transport.message;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by rado on 7/23/15.
 */
public class MultipartSmppTransactionMessage extends SmppTransactionMessage {

    private int numberOfParts;

    // how many times this message or parts of this message tried to resend
    private int numberOfResend;

    private Map<Integer, byte[]> messages;

    private Map<String, StatusType> transactionStatusMap;

    private Collection<String> deliveredIds;

    private boolean USC2Flag;

    public MultipartSmppTransactionMessage() {
        super();
    }

    public MultipartSmppTransactionMessage(String smsId, String phoneNumber,
                                           String sender,
                                           String userGUID, Integer serverId,
                                           String serverError, String campaignID) {

        super(smsId, phoneNumber,
                sender,
                userGUID, serverId,
                serverError, campaignID,
                true);

        this.messages = new HashMap<>();
        this.transactionStatusMap = new HashMap<>();
        this.deliveredIds = new HashSet<>();

        this.numberOfResend = 0;
        this.numberOfParts = 0;
    }

    public MultipartSmppTransactionMessage(SMSMessage smsMessage, Integer serverId,
                                           String serverError) {

        super(smsMessage, serverId,
                serverError,
                true);

        this.messages = new HashMap<>();
        this.transactionStatusMap = new HashMap<>();
        this.deliveredIds = new HashSet<>();

        this.numberOfResend = 0;
        this.numberOfParts = 0;
    }

    public MultipartSmppTransactionMessage(String smsId, String phoneNumber,
                                           String sender,
                                           String userGUID, Integer serverId,
                                           String serverError, String campaignID,
                                           Map<String, StatusType> transactionStatusMap,
                                           Map<Integer, byte[]> messages,
                                           boolean USC2Flag) {

        super(smsId, phoneNumber,
                sender,
                userGUID, serverId,
                serverError, campaignID, true);

        this.transactionStatusMap = new HashMap<>();
        this.transactionStatusMap.putAll(transactionStatusMap);

        this.messages = new HashMap<>();
        this.messages.putAll(messages);

        this.deliveredIds = new HashSet<>();
        this.USC2Flag = USC2Flag;

        this.numberOfResend = 0;
        this.numberOfParts = messages.size();
    }

    public MultipartSmppTransactionMessage(SMSMessage smsMessage, Integer serverId,
                                           String serverError,
                                           Map<String, StatusType> transactionStatusMap,
                                           Map<Integer, byte[]> messages,
                                           boolean USC2Flag, int numberOfParts) {

        super(smsMessage, serverId, serverError, true);

        this.transactionStatusMap = new HashMap<>();
        this.transactionStatusMap.putAll(transactionStatusMap);

        this.messages = new HashMap<>();
        this.messages.putAll(messages);

        this.deliveredIds = new HashSet<>();
        this.USC2Flag = USC2Flag;

        this.numberOfResend = 0;
        this.numberOfParts = numberOfParts;
    }

    public int getNumberOfParts() {
        return numberOfParts;
    }

    public void setNumberOfParts(int numberOfParts) {
        this.numberOfParts = numberOfParts;
    }

    public int getNumberOfResend() {
        return numberOfResend;
    }

    public void setNumberOfResend(int numberOfResend) {
        this.numberOfResend = numberOfResend;
    }

    public void increaseNumberOfResend() {
        numberOfResend++;
    }

    public void putSmsPartForResend (Map<Integer, byte[]> smsPart){
         messages.putAll(smsPart);
    }

    public boolean isSentAllSmsParts() {
        return messages.isEmpty();
    }

    public Map<Integer, byte[]> getMessages() {
        return messages;
    }

    public void setMessages(Map<Integer, byte[]> messages) {
        this.messages = messages;
    }

    public void setTransactionStatusMap(Map<String, StatusType> transactionStatusMap) {
        this.transactionStatusMap = transactionStatusMap;
    }

    public void setDeliveredIds(Collection<String> deliveredIds) {
        this.deliveredIds = deliveredIds;
    }

    public boolean isUSC2Flag() {
        return USC2Flag;
    }

    public void setUSC2Flag(boolean USC2Flag) {
        this.USC2Flag = USC2Flag;
    }

    public Map<String, StatusType> getTransactionStatusMap() {
        return transactionStatusMap;
    }

    public Collection<String> getDeliveredIds() {
        return deliveredIds;
    }

    public void addTransactionStatusType(String transactionId, StatusType statusType) {
        transactionStatusMap.put(transactionId, statusType);
    }

    public void addDeliveredId(String deliveredId) {
        deliveredIds.add(deliveredId);
    }

    public boolean isMultipartSms() {
        return true;
    }

    public boolean isMessageDelivered (){
        return deliveredIds.containsAll(transactionStatusMap.keySet());
    }

    @Override
    public String toString() {
        return new StringBuilder("Multipart transaction message: [")
                .append(super.toString())
                .append("; USC2 : ")
                .append(USC2Flag)
                .append("; numberOfParts: ")
                .append(numberOfParts)
                .append("; numberOfResend: ")
                .append(numberOfResend)
                .append("; transaction ids: [")
                .append(transactionStatusMap)
                .append("]; delivered ids: [")
                .append(deliveredIds)
                .append("]").toString();
    }

}
