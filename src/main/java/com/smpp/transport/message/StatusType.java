package com.smpp.transport.message;

/**
 * User: gmayshmaz
 * Date: 6/20/13
 */
public enum StatusType {
    SMPP_SEND_ERROR(5, true),
    SMPP_SEND_OK(5, false),

    ACCEPTED(6, true),
    EXPIRED(6, true),
    DELETED(6, true),
    UNKNOWN(6, true),
    REJECTED(6, true),

    SMS_DELIVERED(6, true),
    SMS_UNDELIVERED(6, true);

    public final int rang;
    public final boolean isFinal;

    private StatusType(int rang, boolean isFinal) {
        this.rang = rang;
        this.isFinal = isFinal;
    }

}
