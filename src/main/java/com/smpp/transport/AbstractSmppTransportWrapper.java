package com.smpp.transport;


import com.smpp.transport.message.SMSMessage;
import com.smpp.transport.message.SmppErrorStatus;
import com.smpp.transport.message.SmppTransactionMessage;
import com.smpp.transport.message.StatusType;
import com.smpp.transport.server.SmppServer;
import com.smpp.transport.server.SmppServerConnectResponse;
import com.smpp.transport.server.SmppServerType;
import com.smpp.transport.util.StopWatchHires;
import org.jsmpp.bean.*;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.*;
import org.jsmpp.util.DeliveryReceiptState;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;


/**
 * Created by rado on 1/8/15.
 */
public abstract class AbstractSmppTransportWrapper implements TransportWrapper {
    private static final Logger log = LoggerFactory.getLogger(AbstractSmppTransportWrapper.class);

    protected SMPPSession session;
    protected SmppServer server;
    protected SmppTransportPool transportPool;
    private ExecutorService receiveTask;
    private SessionStateListener stateListener;

    Predicate<SessionState> IS_SMPP_SESSION_DISABLE = s -> Objects.isNull(s)
            || s.equals(SessionState.UNBOUND) || s.equals(SessionState.CLOSED);

    private final String sessionName;


    public AbstractSmppTransportWrapper(SmppServer server,
                                        SmppTransportPool transportPool) {
        this.server = server;
        this.transportPool = transportPool;
        sessionName = String.valueOf(server.getId()) + ":" +
                server.getHostname() + ":" + server.getSystemId();
    }

    public abstract SmppServerType getSmppServerType();

    public abstract SmppTransactionMessage sendSmsMessage(SMSMessage message);

    public boolean isConnected() {
        return Objects.nonNull(session)
            && !IS_SMPP_SESSION_DISABLE.test(session.getSessionState());
    }

    @Override
    public void connect() {
        createSmppSession();
    }

    @Override
    public void reconnect() {
        log.info("SMPP session for {} start to reconnect", sessionName);
        if (!isConnected()) {

            interruptSession();
            createSmppSession();

        }

        log.info("{} smpp session reconnected duration", sessionName);

    }

    @Override
    public void interruptSession() {

        if (Objects.nonNull(session)) {
            session.unbindAndClose();
        }

        if (isConnected()) {

            log.warn("Could not unbind and close SMPP session {}", sessionName);

        } else {
            log.info("SMPPSession #{} unbind and close", sessionName);

        }

    }

    public void close() {

        log.info("Close SMPPSession for {}, {}, {}",
                server.getId(), server.getHostname(), server.getSystemId());

        transportPool.removeTransport(this);
        interruptSession();

        if (receiveTask != null) {
            receiveTask.shutdownNow();
        }

        Thread.currentThread().interrupt();
    }


    public void createSmppSession() {

        StopWatchHires watchHires = new StopWatchHires();
        watchHires.start();

        log.info("Start to create SMPPSession {}", sessionName);

        try {
            session = new SMPPSession();
            session.connectAndBind(server.getHostname(),
                    server.getPort(),
                    new BindParameter(
                            BindType.BIND_TRX,
                            server.getSystemId(),
                            server.getPassword(),
                            server.getSystemType(),
                            TypeOfNumber.UNKNOWN,
                            NumberingPlanIndicator.UNKNOWN,
                            null));

            stateListener = new SessionStateListenerImpl();

            session.addSessionStateListener(stateListener);
            session.setMessageReceiverListener(new SmsReceiverListenerImpl());
            session.setTransactionTimer(TRANSACTION_TIMER);

            if (Objects.isNull(receiveTask)
                    || receiveTask.isShutdown()
                    || receiveTask.isTerminated()) {

                this.receiveTask = Executors.newCachedThreadPool();
            }

            watchHires.stop();

            log.info("Open smpp session id {}, state {}, duration {} for {}",
                    session.getSessionId(), session.getSessionState().name(),
                    watchHires.toHiresString(), sessionName);

        } catch (IOException e) {

            watchHires.stop();

            if (SmppServerConnectResponse.contains(e.getMessage())) {
                log.error("Exception while SMPP session creating. Reason: {}. Duration {}, {}",
                        e.getMessage(), watchHires.toHiresString(), sessionName);

                close();
                return;

            } else if (e instanceof UnknownHostException) {
                log.error("Exception while SMPP session creating. Unknown hostname {}, duration {}, {}",
                        e.getMessage(), watchHires.toHiresString(), sessionName);

                close();
                return;

            } else {

                log.error("Failed to connect SMPP session for {}, duration {}, Because {}",
                        sessionName, watchHires.toHiresString(), e.getMessage());

            }

        }

        if (!isConnected()) {

            reconnect();
        }

    }


    /**
     * This class will receive the notification from {@link SMPPSession} for the
     * state changes. It will schedule to re-initialize session.
     */
    private class SessionStateListenerImpl implements SessionStateListener {

        @Override
        public void onStateChange(SessionState newState, SessionState oldState, Object source) {

            if (!newState.isBound()) {

                log.warn("SmppSession changed status from {} to {}. {}", oldState, newState, sessionName);
                reconnect();
            }
        }
    }

    /*The logic on this listener should be accomplish in a short time,
    because the deliver_sm_resp will be processed after the logic executed.*/
    private class SmsReceiverListenerImpl implements MessageReceiverListener {

        public SmsReceiverListenerImpl() {
        }

        @Override
        public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {

            if (Objects.isNull(deliverSm)) {
                log.error("Smpp server return NULL delivery answer");
                return;
            }

            try {
                // this message is delivery receipt
                DeliveryReceipt delReceipt = deliverSm.getShortMessageAsDeliveryReceipt();

                //delReceipt.getId() must be equals transactionId from SMPPServer
                String transactionId = delReceipt.getId();
                StatusType statusType;
                String subStatus;
                if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {

                    //  && delReceipt.getDelivered() == 1
                    statusType = getDeliveryStatusType(delReceipt.getFinalStatus());

                    SmppErrorStatus smppErrorStatus =
                            SmppErrorStatus.contains(delReceipt.getError());

                    if (smppErrorStatus != null)
                        subStatus = smppErrorStatus.name();
                    else
                        subStatus = delReceipt.getError();

                } else {
                    statusType = StatusType.SMS_UNDELIVERED;

                    // this message is regular short message
                    log.error("Delivery SMS event has wrong receipt. Message: {}", deliverSm.getShortMessage());

                    subStatus = SmppErrorStatus.INVALID_FORMAT.name();
                }


                String phoneNumber = deliverSm.getDestAddress();

                receiveTask.submit(() -> transportPool.saveDeliveryStatus(transactionId, statusType, subStatus, phoneNumber));

                log.info("Receiving delivery receipt from {} to {}, transaction id {}, status {}, subStatus {}",
                        deliverSm.getSourceAddr(), deliverSm.getDestAddress(),
                        transactionId, statusType, subStatus);

            } catch (InvalidDeliveryReceiptException e) {
                log.error("Exception while SMS is sending, destination address {}, {}",
                        deliverSm.getDestAddress(), e.getMessage(), e);
            }


        }

        @Override
        public void onAcceptAlertNotification(AlertNotification alertNotification) {
            log.error("Error on sending SMS message: {}", alertNotification.toString());
        }

        @Override
        public DataSmResult onAcceptDataSm(DataSm dataSm, Session source)
                throws ProcessRequestException {
            log.debug("Event in SmsReceiverListenerImpl.onAcceptDataSm!");
            return null;
        }

        private StatusType getDeliveryStatusType(DeliveryReceiptState state) {
            if (state.equals(DeliveryReceiptState.DELIVRD))
                return StatusType.SMS_DELIVERED;

            else if (state.equals(DeliveryReceiptState.ACCEPTD))
                return StatusType.ACCEPTED;

            else if (state.equals(DeliveryReceiptState.DELETED))
                return StatusType.DELETED;

            else if (state.equals(DeliveryReceiptState.EXPIRED))
                return StatusType.EXPIRED;

            else if (state.equals(DeliveryReceiptState.REJECTD))
                return StatusType.REJECTED;

            else if (state.equals(DeliveryReceiptState.UNKNOWN))
                return StatusType.UNKNOWN;

            else
                return StatusType.SMS_UNDELIVERED;

        }
    }

    public SmppServer getServer() {
        return this.server;
    }

    public SMPPSession getSession() {
        return session;
    }

    @Override
    public String getHostname() {
        return server.getHostname();
    }

}
