package com.smpp.transport.message;


/**
 * Created by rado on 7/23/15.
 */
public class SingleSmppTransactionMessage extends SmppTransactionMessage {

    private String transactionId;

    private StatusType statusType;

    public SingleSmppTransactionMessage() {
        super();
    }

    public SingleSmppTransactionMessage(SMSMessage smsMessage,
                                        Integer serverId,
                                        String serverError,
                                        String transactionId) {

        super(smsMessage, serverId,
                serverError,false);

        this.transactionId = transactionId;
    }

    public SingleSmppTransactionMessage(SMSMessage smsMessage,
                                        Integer serverId,
                                        String serverError,
                                        String transactionId,
                                        StatusType statusType) {

        super(smsMessage, serverId,
                serverError,false);

        this.transactionId = transactionId;
        this.statusType = statusType;
    }

    public SingleSmppTransactionMessage(SMSMessage smsMessage,
                                        Integer serverId,
                                        String serverError,
                                        StatusType statusType,
                                        String transactionId) {

        super(smsMessage, serverId,
                serverError,false);

        this.statusType = statusType;
        this.transactionId = transactionId;
    }

    public SingleSmppTransactionMessage(String smsId, String phoneNumber,
                                        String sender,
                                        String userGUID, Integer serverId,
                                        String serverError, String campaignID,
                                        String transactionId) {

        super(smsId, phoneNumber,
                sender,
                userGUID, serverId,
                serverError, campaignID,
                false);

        this.transactionId = transactionId;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return new StringBuilder("Single transaction message: [")
                .append(super.toString())
                .append("; status: ")
                .append(statusType)
                .append("; transaction id : ")
                .append(transactionId)
                .append("]").toString();
    }
}
