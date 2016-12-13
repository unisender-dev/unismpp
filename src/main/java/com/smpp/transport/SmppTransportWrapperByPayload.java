package com.smpp.transport;

import com.smpp.transport.message.SMSMessage;
import com.smpp.transport.message.SingleSmppTransactionMessage;
import com.smpp.transport.server.SmppResponseError;
import com.smpp.transport.server.SmppServer;
import com.smpp.transport.server.SmppServerType;
import com.smpp.transport.util.StopWatchHires;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.*;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * Creates standard input data which send to Sms Server
 *
 * Created by rtymchik on 6/23/15.
 */
public class SmppTransportWrapperByPayload extends AbstractSmppTransportWrapper {
    private static final Logger log = LoggerFactory.getLogger(SmppTransportWrapperByPayload.class);

    private static final byte [] EMPTY_ARRAY = new byte[]{};

    public SmppTransportWrapperByPayload(SmppServer server,
                                         SmppTransportPool transportPool) {
        super(server, transportPool);
    }

    @Override
    public SingleSmppTransactionMessage sendSmsMessage(final SMSMessage message) {

        final String bodyText = message.getTextBody();
        final int smsLength = bodyText.length();
        OptionalParameter messagePayloadParameter;
        String transportId = null;
        String error = null;
        boolean isUSC2 = false;
        boolean isFlashSms = message.isFlash();

        StopWatchHires watchHires = new StopWatchHires();
        watchHires.start();

        log.debug("Start to send sms id {} length {}",
                message.getSmsId(), smsLength);

        try {

            byte[] encoded;
            if ((encoded = Gsm0338.encodeInGsm0338(bodyText)) != null) {
                messagePayloadParameter =
                        new OptionalParameter.OctetString(
                                OptionalParameter.Tag.MESSAGE_PAYLOAD.code(),
                                encoded);

                log.debug("Found Latin symbols in sms id {} message", message.getSmsId());

            } else {
                isUSC2 = true;
                messagePayloadParameter =
                        new OptionalParameter.OctetString(
                                OptionalParameter.Tag.MESSAGE_PAYLOAD.code(),
                                bodyText,
                                "UTF-16BE");
                log.debug("Found Cyrillic symbols in sms id {} message", message.getSmsId());
            }

            GeneralDataCoding dataCoding = getDataCodingForServer(isUSC2, isFlashSms);

            log.debug("Selected data_coding: {}, value: {}, SMPP server type: {}",
                    dataCoding.getAlphabet(),
                    dataCoding.toByte(),
                    server.getServerType());

            transportId = session.submitShortMessage(
                    "CMT",
                    TypeOfNumber.ALPHANUMERIC,
                    NumberingPlanIndicator.UNKNOWN,
                    message.getSender(),
                    TypeOfNumber.INTERNATIONAL,
                    NumberingPlanIndicator.ISDN,
                    message.getPhone(),
                    ESM_CLASS,
                    ZERO_BYTE,
                    ONE_BYTE,
                    null,
                    null,
                    rd,
                    ZERO_BYTE,
                    dataCoding,
                    ZERO_BYTE,
                    EMPTY_ARRAY,
                    messagePayloadParameter);


        } catch (PDUException e) {
            error = e.getMessage();
            // Invalid PDU parameter
            log.error("SMS id:{}. Invalid PDU parameter {}",
                    message.getSmsId(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (ResponseTimeoutException e) {
            error = analyseExceptionMessage(e.getMessage());
            // Response timeout
            log.error("SMS id:{}. Response timeout: {}",
                    message.getSmsId(), e.getMessage());
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (InvalidResponseException e) {
            error = e.getMessage();
            // Invalid response
            log.error("SMS id:{}. Receive invalid response: {}",
                    message.getSmsId(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (NegativeResponseException e) {
            // get smpp error codes
            error = String.valueOf(e.getCommandStatus());
            // Receiving negative response (non-zero command_status)
            log.error("SMS id:{}, {}. Receive negative response: {}",
                    message.getSmsId(), message.getPhone(), e.getMessage());
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (IOException e) {
            error = analyseExceptionMessage(e.getMessage());
            log.error("SMS id:{}. IO error occur {}",
                    message.getSmsId(), e.getMessage());
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (Exception e) {
            error = e.getMessage();
            log.error("SMS id:{}. Unexpected exception error occur {}",
                    message.getSmsId(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        }

        watchHires.stop();

        log.info("Sms id:{} length {} sent with transaction id:{} from {} to {}, duration {}",
                message.getSmsId(), smsLength,
                transportId, message.getSender(),
                message.getPhone(), watchHires.toHiresString());

        return new SingleSmppTransactionMessage(message, server.getId(), error, transportId);
    }

    private GeneralDataCoding getDataCodingForServer (boolean isUCS2Coding, boolean isFlashSms){

        GeneralDataCoding coding;

        if (isFlashSms) {
            coding = isUCS2Coding ? UCS2_CODING : DEFAULT_CODING;
        } else {
            coding = isUCS2Coding ? UCS2_CODING_WITHOUT_CLASS : DEFAULT_CODING_WITHOUT_CLASS;
        }

        return coding;
    }

    /**
     * Analyze exception message for our problem with session
     * While schedule reconnecting session sms didn't send and didn't put to resend
     */
    private String analyseExceptionMessage(String exMessage){

        if(Objects.isNull(exMessage))
            return exMessage;

        if (exMessage.contains("No response after waiting for"))
            return SmppResponseError.RECONNECT_RSPCTIMEOUT.getErrCode();

        else if (exMessage.contains("Cannot submitShortMessage while"))
            return SmppResponseError.RECONNECT_CANNTSUBMIT.getErrCode();

        else if (exMessage.contains("Failed sending submit_sm command"))
            return SmppResponseError.RECONNECT_FAILEDSUBMIT.getErrCode();

        return exMessage;
    }

    @Override
    public SmppServerType getSmppServerType(){
        return SmppServerType.PAYLOAD;
    }
}
