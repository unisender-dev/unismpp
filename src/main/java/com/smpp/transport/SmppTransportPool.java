package com.smpp.transport;

import com.smpp.transport.message.StatusType;
import com.smpp.transport.server.SmppServer;
import com.smpp.transport.server.SmppServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class SmppTransportPool extends AbstractTransportPool {
    private static final Logger log = LoggerFactory.getLogger(SmppTransportPool.class);

    private static final long RELEASE_TIMEOUT = 30 * 1000;  // 30 sec

    public SmppTransportPool(SmppServer server,
                             int smppConnectionPoolSize) {

        super(smppConnectionPoolSize);

        for (int i = 0; i < smppConnectionPoolSize; i++) {

            if (SmppServerType.UDH.equals(server.getServerType())) {

                transports.add(
                        new SmppTransportWrapperByUDH(server, this));

            } else if (SmppServerType.SAR.equals(server.getServerType())) {

                transports.add(
                        new SmppTransportWrapperBySAR(server, this));

            } else {

                transports.add(
                        new SmppTransportWrapperByPayload(server, this));

            }

        }
    }


    public void saveDeliveryStatus(String transactionId, StatusType statusType, String subStatus, String phoneNumber) {
        // you can save delivery data in your storage there
    }

    public boolean isAnySmppConnection() {
        return transports.stream()
                .anyMatch(TransportWrapper::isConnected);
    }

    public boolean isAllSmppConnection() {
        return transports.stream()
                .allMatch(TransportWrapper::isConnected);
    }

    public boolean isUDHSmppServerExist() {
        boolean exist = false;
        for (TransportWrapper wrapper : transports) {

            if (Objects.nonNull(wrapper)
                    && SmppServerType.UDH == ((AbstractSmppTransportWrapper) wrapper).getSmppServerType()) {

                exist = true;
                break;
            }

        }

        return exist;
    }


    public SmppTransportWrapperByUDH acquireUDHTransport() {

        AbstractSmppTransportWrapper transport;
        while (isUDHSmppServerExist()) {
            transport = (AbstractSmppTransportWrapper) acquireTransport();

            if (Objects.nonNull(transport)
                    && transport instanceof SmppTransportWrapperByUDH) {

                return (SmppTransportWrapperByUDH) transport;
            } else {
                try {
                    Thread.currentThread().wait(RELEASE_TIMEOUT);
                    acquireUDHTransport();
                } catch (InterruptedException e) {
                    log.warn("Couldn't acquire UDH transport connection to resend message");
                }
            }
        }
        return null;
    }


}