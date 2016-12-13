package com.smpp.transport;

import com.smpp.transport.message.*;
import com.smpp.transport.server.SmppServer;
import com.smpp.transport.server.SmppServerType;
import com.smpp.transport.util.StopWatchHires;
import org.apache.commons.lang3.ArrayUtils;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.SMPPSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Send sms to SMSC using UDH headers. Algorithm:
 * 1. Get text encoding
 * 2. Count the size of one SMS
 * 3. Get count of sms, depends of what type of encode
 * 4. Create UDH Header which will be in the begin of every part of message
 * 5. Split sms to sms-parts with encode and needed size
 * 6. Send sms to SMSC
 *
 * https://code.google.com/p/jsmpp/source/browse/trunk/src/java/examples/org/jsmpp/examples/SubmitMultipartMultilangualExample.java?r=247
 *
 * http://stackoverflow.com/questions/21098643/smpp-submit-long-message-and-message-split/21121353#21121353
 * Created by rtymchik on 7/1/15.
 */
public class SmppTransportWrapperByUDH extends AbstractSmppTransportWrapper {

    private static final Logger log = LoggerFactory.getLogger(SmppTransportWrapperByUDH.class);



    public SmppTransportWrapperByUDH(SmppServer server,
                                     SmppTransportPool transportPool) {
        super(server, transportPool);
    }

    @Override
    public SmppTransactionMessage sendSmsMessage(SMSMessage message) {

        final String bodyText = message.getTextBody();
        final int bodyTextLength = bodyText.length();
        String transportId = null;
        Map<String, StatusType> transportIds = new HashMap<>();
        String error = "";
        boolean UCS2Flag = true;
        boolean singleSmsFlag = true;
        int amountOfMultipart = 0;
        Map<Integer, byte[]> messages = null;

        StopWatchHires watchHires = new StopWatchHires();
        watchHires.start();

        log.debug("Start to send sms id {} length {}",
                message.getSmsId(), bodyTextLength);

        SmppTransactionMessage transactionMessage;

        try {

            if (Gsm0338.isGsm0338(bodyText))
                UCS2Flag = false;

// is this SmsMessage multipart or single
 // If sms is not single - split bodyText and add UDH header to each, else one message without UDH
            if (bodyText.length() > (UCS2Flag ? ONE_CYRILLIC_MSG_LENGTH : ONE_LATIN_MSG_LENGTH)) {
                singleSmsFlag = false;

                messages = createConcatenatedBinaryShortMessages(bodyText, UCS2Flag);

                Map<Integer, Integer> sizeOfMessages = new HashMap<>();
                for (Map.Entry<Integer, byte[]> msg : messages.entrySet()) {

                    sizeOfMessages.put(msg.getKey(), msg.getValue().length);
                }

                log.debug("Start to send UDH sms id {}, phone: {}, full text length: {}. Split text: {}",
                        message.getSmsId(), message.getPhone(), bodyTextLength, sizeOfMessages);

                amountOfMultipart = messages.size();

                Iterator<Map.Entry<Integer, byte[]>> iter = messages.entrySet().iterator();

                // send sms parts and collect got transaction id into map -> transportIds
                while (iter.hasNext()) {

                    Map.Entry<Integer, byte[]> msg = iter.next();
                    byte[] smsPart = msg.getValue();

                    transportId = sendPreparedMessage(
                            message.getSender(), message.getPhone(),
                            singleSmsFlag, UCS2Flag, smsPart);

                    if (Objects.nonNull(transportId)
                            && !transportId.isEmpty()) {

                        transportIds.put(transportId, StatusType.SMPP_SEND_OK);
                        iter.remove();
                    }
                }

            } else {

                transportId = sendPreparedMessage(
                        message.getSender(), message.getPhone(),
                        singleSmsFlag, UCS2Flag,
                        (UCS2Flag ? bodyText.getBytes(UTF_16BE) : Gsm0338.encodeInGsm0338(bodyText)));

            }
        }catch (UnsupportedEncodingException e) {
            error = e.getMessage();
            log.error("SMS id:{}. Unsupported encoding exception {}",
                    message.getSmsId(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
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

        } finally {

            if (singleSmsFlag) {
                transactionMessage = new SingleSmppTransactionMessage(message,
                        server.getId(), error, transportId);

            } else {

                transactionMessage = new MultipartSmppTransactionMessage(
                        message,
                        server.getId(),
                        error,
                        transportIds,
                        messages,
                        UCS2Flag,
                        amountOfMultipart);

            }
        }

        watchHires.stop();

        log.info("Sms id:{} length {} sent with transaction id:{} from {} to {}, duration {}",
                message.getSmsId(), bodyTextLength,
                singleSmsFlag ? transportId : transportIds,
                message.getSender(), message.getPhone(), watchHires.toHiresString());

        return transactionMessage;
    }

    /**
     * Send full sms ot one part to chosen server via session
     *
     * @param singleFlag - one sms sms or multipart message
     * @param isUSC2 - cyrillic or latin
     * @param smsPart - sms or part of sms
     * @return transactionId - unique id of sms returned by SMSC server
     */

    public String sendPreparedMessage (String sender, String phone,
                                       boolean singleFlag, boolean isUSC2,
                                       byte[] smsPart)
            throws PDUException,
            IOException,
            InvalidResponseException,
            NegativeResponseException,
            ResponseTimeoutException {

        return getSMPPSession().submitShortMessage(
                SERVICE_TYPE,
                TypeOfNumber.ALPHANUMERIC,
                NumberingPlanIndicator.UNKNOWN,
                sender,
                TypeOfNumber.INTERNATIONAL,
                NumberingPlanIndicator.ISDN,
                phone,
                singleFlag ? ESM_CLASS : ESM_CLASS_UDH,
                ZERO_BYTE,
                ONE_BYTE,
                null,
                null,
                rd,
                ZERO_BYTE,
                isUSC2 ? UCS2_CODING : DEFAULT_CODING,
                ZERO_BYTE,
                smsPart);
    }

    public SMPPSession getSMPPSession() {
        return session;
    }

    /**
     * UDH header to the beginning each part of message
     * udh[4] - total amount message parts
     * udh[5] - sequence number of message part
     *
     * Split bodyText to several sms parts
//     * @param binaryShortMessage - message
     * @return map - sequence number of message part, message part in byte
     *
     */
    public static Map<Integer, byte[]> createConcatenatedBinaryShortMessages(String message, boolean USC2Flag) throws IllegalArgumentException, UnsupportedEncodingException {

        Map<Integer, byte[]> shortMessageParts = new HashMap();

        List<String> splittedMessages = splitMessageToEqualParts(message, USC2Flag ? CYRILLIC_PART_LENGTH : LATIN_PART_LENGTH);

        int partNumber = 1;
        for (String msgPart : splittedMessages) {

            byte[] msgBytes = USC2Flag ? msgPart.getBytes(UTF_16BE) : Gsm0338.encodeInGsm0338(msgPart);

            byte[] udhHeader = new byte[6];
            udhHeader[0] = (byte) 0x05;
            udhHeader[1] = (byte) 0x00;
            udhHeader[2] = (byte) 0x03;
            udhHeader[3] = (byte) 0x00;
            udhHeader[4] = (byte) splittedMessages.size();
            udhHeader[5] = (byte) (partNumber);

            shortMessageParts.put(partNumber, ArrayUtils.addAll(udhHeader, msgBytes));
            partNumber++;
        }

        return shortMessageParts;
    }

    public static List<String> splitMessageToEqualParts(String message, int messageSize) {

        List<String> messages = new ArrayList();

        int index = 0;
        while (index < message.length()) {
            messages.add(message.substring(index, Math.min(index + messageSize, message.length())));
            index += messageSize;
        }

        return messages;
    }

    public void resendMessage(MultipartSmppTransactionMessage result) {
//String sender, String phone, boolean isSingleSms, boolean isUSC2, byte[] smsPart
        log.debug("Try to resend {}", result);

        String error = null;

        try {

            Iterator<byte[]> iter = result.getMessages().values().iterator();

            while (iter.hasNext()) {

                String transactionId = sendPreparedMessage(result.getSender(),
                        result.getPhoneNumber(),
                        false,
                        result.isUSC2Flag(),
                        iter.next());

                if (Objects.nonNull(transactionId)
                        && !transactionId.isEmpty()) {

                    result.addTransactionStatusType(transactionId, StatusType.SMPP_SEND_OK);
                    iter.remove();

                    log.info("One part of sms id {} sent with transaction id {}, left {}",
                            result.getSmsId(), transactionId, result.getMessages().size());
                }

            }


        } catch (PDUException e) {
            error = e.getMessage();
            // Invalid PDU parameter
            log.error("SMS id:{}. Invalid PDU parameter {}",
                    result.getSmsId(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (ResponseTimeoutException e) {
            error = e.getMessage();
            // Response timeout
            log.error("SMS id:{}. Response timeout: {}",
                    result.getSmsId(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (InvalidResponseException e) {
            error = e.getMessage();
            // Invalid response
            log.error("SMS id:{}. Receive invalid response: {}",
                    result.getSmsId(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (NegativeResponseException e) {
            error = e.getMessage();
            // Receiving negative response (non-zero command_status)
            log.error("SMS id:{}, {}. Receive negative response: {}",
                    result.getSmsId(), result.getPhoneNumber(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (IOException e) {
            error = e.getMessage();
            log.error("SMS id:{}. IO error occur {}",
                    result.getSmsId(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);
        } catch (Exception e) {
            error = e.getMessage();
            log.error("SMS id:{}. Unexpected exception error occur {}",
                    result.getSmsId(), error);
            log.debug("Session id {}, state {}. {}", session.getSessionId(), session.getSessionState().name(), e);

        }

        if (Objects.nonNull(error) && !error.isEmpty())
            result.setServerError(error);

        result.setServerId(server.getId());
        result.increaseNumberOfResend();
    }


    @Override
    public SmppServerType getSmppServerType() {
        return SmppServerType.UDH;
    }

}
