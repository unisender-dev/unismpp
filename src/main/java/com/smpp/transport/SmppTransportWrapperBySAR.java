package com.smpp.transport;

import com.smpp.transport.message.SMSMessage;
import com.smpp.transport.message.SingleSmppTransactionMessage;
import com.smpp.transport.message.SmppTransactionMessage;
import com.smpp.transport.server.SmppServer;
import com.smpp.transport.server.SmppServerType;
import com.smpp.transport.util.StopWatchHires;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Send long sms without message_payload parameter
 * as recommended in https://code.google.com/p/smslib/issues/detail?id=448
 *
 * Created for sms provider "IT-Decision"
 *
 * Created by rtymchik on 6/23/15.
 */
public class SmppTransportWrapperBySAR extends AbstractSmppTransportWrapper {
    private static final Logger log = LoggerFactory.getLogger(SmppTransportWrapperBySAR.class);

    private static int ONE_FULL_SIZE_SMS = 254;

    public SmppTransportWrapperBySAR(SmppServer server,
                                     SmppTransportPool transportPool) {
        super(server, transportPool);
    }

    public SmppTransactionMessage sendSmsMessage(final SMSMessage message) {

        final String bodyText = message.getTextBody();
        final int smsLength = bodyText.length();
        String transportId = null;
        String error = null;
        byte[] msgBytes;
        int fullPartSms = 0;
        int notFullSms;
        boolean isUSC2 = true;

        StopWatchHires watchHires = new StopWatchHires();
        watchHires.start();

        log.debug("Start to send sms id {} length {}",
                message.getSmsId(), smsLength);

        try {

            byte[] encoded;
            if ((encoded = Gsm0338.encodeInGsm0338(bodyText)) != null) {

                isUSC2 = false;
                log.debug("Found Latin symbols in sms id {} message", message.getSmsId());

            } else {

                encoded = bodyText.getBytes("UTF-16BE");
                log.debug("Found Cyrillic symbols in sms id {} message", message.getSmsId());
            }

// Count how mach sms in this textBody
            if (smsLength > ONE_FULL_SIZE_SMS) {
                fullPartSms = smsLength / ONE_FULL_SIZE_SMS;
                notFullSms = smsLength % ONE_FULL_SIZE_SMS;
            } else {
                notFullSms = smsLength;
            }

            List<byte []> messages = splitSmsBySize(fullPartSms, notFullSms, encoded);

            log.debug("Slitted bodyText to fullSms: {} and one notFullSms {} symbols length. Total: {} messages",
                    fullPartSms, notFullSms, messages.size());

//  Номер ссылки для конкретного склеенного короткого сообщения.
            OptionalParameter sarMsgRefNumParameter = new OptionalParameter.Int(OptionalParameter.Tag.SAR_MSG_REF_NUM, 0);
//  Указывает общее количество коротких сообщений в рамках склеенного короткого сообщения.
            OptionalParameter sarTotalSegmentsParameter = new OptionalParameter.Int(OptionalParameter.Tag.SAR_TOTAL_SEGMENTS, messages.size());
//  Указывает номер последовательности конкретного фрагмента короткого сообщенияв рамках склеенного короткого сообщения.
            OptionalParameter sarSegmentSeqnumParameter;

            int currentMsgNumber = 0;
            for (byte [] msg : messages) {

                sarSegmentSeqnumParameter = new OptionalParameter.Int(OptionalParameter.Tag.SAR_SEGMENT_SEQNUM, ++currentMsgNumber);

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
                        isUSC2 ? UCS2_CODING : DEFAULT_CODING,
                        ZERO_BYTE,
                        msg,
                        sarMsgRefNumParameter,
                        sarTotalSegmentsParameter,
                        sarSegmentSeqnumParameter);
            }

            log.info("Sms id:{} length {} sent with transaction id:{} from {} to {}",
                    message.getSmsId(), smsLength,
                    transportId, message.getSender(),
                    message.getPhone());

        } catch (PDUException e) {
            error = e.getMessage();
            // Invalid PDU parameter
            log.error("SMS id:{}. Invalid PDU parameter {}",
                    message.getSmsId(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (ResponseTimeoutException e) {
            error = e.getMessage();
            // Response timeout
            log.error("SMS id:{}. Response timeout: {}",
                    message.getSmsId(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (InvalidResponseException e) {
            error = e.getMessage();
            // Invalid response
            log.error("SMS id:{}. Receive invalid response: {}",
                    message.getSmsId(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (NegativeResponseException e) {
            error = e.getMessage();
            // Receiving negative response (non-zero command_status)
            log.error("SMS id:{}, {}. Receive negative response: {}",
                    message.getSmsId(), message.getPhone(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (IOException e) {
            error = e.getMessage();
            log.error("SMS id:{}. IO error occur {}",
                    message.getSmsId(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (Exception e) {
            error = e.getMessage();
            log.error("SMS id:{}. Unexpected exception error occur {}",
                    message.getSmsId(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        }

        watchHires.stop();
        log.info("Sms {} length {} processed, duration {}",
                message.getSmsId(),
                smsLength,
                watchHires.toHiresString());

        return new SingleSmppTransactionMessage(message, server.getId(), error, transportId);
    }

    /**
     * Split bodyText to several sms
     * @param fullPartSms - count of full sms which could be not more than 254 symbols
     * @param notFullSms - size of last sms. if there are a few full sms this param will be last sms (which not full).
     * @param msgBytes - text body in bytes
     * @return list of sms which ready to send
     */
    public List<byte []> splitSmsBySize (int fullPartSms, int notFullSms, byte [] msgBytes) {
        List<byte []> messages = new ArrayList<>();
        int firstSymbol = 0;
        for (int i = 1 ; i <= fullPartSms; i++) {
            byte[] msgPart = new byte[ONE_FULL_SIZE_SMS];
            System.arraycopy(msgBytes, firstSymbol, msgPart , 0, ONE_FULL_SIZE_SMS);
            firstSymbol = ONE_FULL_SIZE_SMS * i;
            messages.add(msgPart);
        }

        if (notFullSms > 0) {
            byte[] msgPart = new byte[notFullSms];
            System.arraycopy(msgBytes, firstSymbol, msgPart , 0, notFullSms);
            messages.add(msgPart);
        }
        return messages;
    }

    @Override
    public SmppServerType getSmppServerType(){
        return SmppServerType.SAR;
    }
}
