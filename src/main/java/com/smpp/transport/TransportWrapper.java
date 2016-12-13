package com.smpp.transport;

import org.jsmpp.bean.*;

/**
 * Created by rado on 12/7/15.
 */
public interface TransportWrapper {

    byte ZERO_BYTE = (byte) 0,
            ONE_BYTE = (byte) 1;

    // for Latin symbols
    GeneralDataCoding DEFAULT_CODING = new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false);
    // for Cyrillic symbols UTF-16BE
    GeneralDataCoding UCS2_CODING = new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS1, false);
    // for Latin symbols without message class (for Sms Traffic Server)
    GeneralDataCoding DEFAULT_CODING_WITHOUT_CLASS = new GeneralDataCoding(Alphabet.ALPHA_DEFAULT);
    // for Cyrillic symbols UTF-16BE without message class (for Sms Traffic Server)
    GeneralDataCoding UCS2_CODING_WITHOUT_CLASS = new GeneralDataCoding(Alphabet.ALPHA_UCS2);


    ESMClass ESM_CLASS = new ESMClass();
    ESMClass ESM_CLASS_UDH = new ESMClass(64);

    RegisteredDelivery rd =  new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE);

    long RECONNECT_INTERVAL = 1; // min
    long TRANSACTION_TIMER = 30000L;

    int ONE_LATIN_MSG_LENGTH = 160;
    int ONE_CYRILLIC_MSG_LENGTH = 70;
    int LATIN_PART_LENGTH = 153;
    int CYRILLIC_PART_LENGTH = 67;
    String UTF_16BE = "UTF-16BE";
    String SERVICE_TYPE = "CMT";

    String getHostname();

    boolean isConnected();

    void reconnect();

    void connect();

    void interruptSession();

}
