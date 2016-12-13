package com.smpp.transport.server;

/**
 * Created by rtymchik on 6/30/15.
 */
public enum SmppServerType {

    UDH("udh"),
    SAR("sar"),
    PAYLOAD("payload");

    private final String value;

    SmppServerType(String value) {
        this.value = value;
    }

    public static SmppServerType find(String serverType) {
        for (SmppServerType type : values())
            if (type.value.equals(serverType))
                return type;

        return null;
    }
}
