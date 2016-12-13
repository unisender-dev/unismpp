package com.smpp.transport.server;

/**
 * Created by rado on 7/12/16.
 */
public enum SmppServerConnectResponse {

    ESME_RALYBND("00000005"),
    ESME_RBINDFAIL("0000000d"),
    ESME_RINVPASWD("0000000e"),
    ESME_RINVSYSID("0000000f"),
    ESME_RUNKNOWNERR("000000ff");

    private final String errCode;

    SmppServerConnectResponse(String errCode) {
        this.errCode = errCode;
    }

    public static boolean contains(String errorCode) {

        if (ESME_RALYBND.errCode.equals(errorCode))
            return true;

        if (ESME_RBINDFAIL.errCode.equals(errorCode))
            return true;

        if (ESME_RINVPASWD.errCode.equals(errorCode))
            return true;

        if (ESME_RINVSYSID.errCode.equals(errorCode))
            return true;

        if (ESME_RUNKNOWNERR.errCode.equals(errorCode))
            return true;

        return false;
    }
}
