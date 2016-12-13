package com.smpp.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by rado on 12/7/15.
 */
public abstract class AbstractTransportPool implements TransportPool {
    private static final Logger log = LoggerFactory.getLogger(AbstractTransportPool.class);

    protected BlockingQueue<TransportWrapper> transports;

    protected static final long CONNECTION_TIMEOUT = 10 * 1000;

    /**
     * Creates and starts a new connector thread for each underlying transport.
     * This method returns after the first successful transport connection is made.
     */

    public AbstractTransportPool(int size) {
        transports = new ArrayBlockingQueue<>(size*2);
    }

    /**
     * Closes the TransportPool.  Implementation is as follows:
     * <ol>
     * <li>Interrupts the execution of any active transport threads</li>
     * <li>Closes the underlying transports</li>
     * </ol>
     *
     * @see javax.mail.Service#close()
     */
    public void close() {

        transports.forEach(TransportWrapper::interruptSession);

        cleanAllTransports();

    }

    @Override
    public boolean connect() {

        transports.forEach(TransportWrapper::connect);

        isActiveTransport(CONNECTION_TIMEOUT);

        List<TransportWrapper> notConnectedWrappers = transports.stream()
                .filter(w -> !w.isConnected())
                .collect(Collectors.toList());

        if (!notConnectedWrappers.isEmpty()) {

            log.warn("Not all Session could be opened. First size: {}, not connected: {}",
                    transports.size(), notConnectedWrappers.size());

            for (TransportWrapper tw : notConnectedWrappers) {
                tw.interruptSession();
                transports.remove(tw);
            }

        }

        return isConnected();
    }

    @Override
    public boolean isEstablishTransport(final TransportWrapper transport,
                                        int availableReconnectCount,
                                        long connectionTimeOut) {

        boolean isEstablished = transport.isConnected();

        while (!isEstablished && availableReconnectCount >= 0) {//Check if connection is not dead

            if (availableReconnectCount > 0) {
                log.warn("{} try to reconnect", transport.getHostname());
                transport.connect();

                isEstablished = transport.isConnected();

                if (isEstablished)
                    transports.add(transport);

            } else {//availableReconnectCount==0, no reconnect tries left

                //the last chance - wait for any transport connected or released

                try {
                    // Wait until the first transport is available, or until connect timeout
                    Thread.sleep(connectionTimeOut);

                    log.debug("Could not establish transport connection after {} tries with ttl {}",
                            availableReconnectCount, connectionTimeOut);

                } catch (InterruptedException e) {

                    log.error("Interrupt reconnect of transport connection: {}", e.getMessage());
                    break;
                }

            }

            availableReconnectCount--;
        }

        return isEstablished;
    }


    @Override
    public boolean isActiveTransport(long connectionTimeOut) {

// If there are no active transports, then timeout was exceeded.
        if (transports.isEmpty()) {

            try {
                Thread.sleep(connectionTimeOut);

                log.debug("Time out {} ms. No active transport connections", connectionTimeOut);
            } catch (InterruptedException e) {

                log.error("Interrupt while waiting of incoming transport connections: {}", e.getMessage());
            }

        }

        return transports.stream().allMatch(TransportWrapper::isConnected);
    }

    @Override
    public void releaseTransport(final TransportWrapper transportWrapper) {

        if (Objects.nonNull(transportWrapper)) {

            try {
                transports.offer(transportWrapper, 1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                log.warn("Could not return Transport in pool, reason: {}", e.getMessage());
            }

            log.debug("After release TransportWrapper: {}, size is: {}",
                    transportWrapper.getHostname(), size());
        }

    }

    @Override
    public boolean isConnected() {
        return !transports.isEmpty()
                && transports.stream().anyMatch(TransportWrapper::isConnected);
    }


    /**
     * Acquires Transport.
     * Transport returned is either connected  and available for sending messages or null
     */
    public TransportWrapper acquireTransport() {

        log.debug("Trying to get free Transport. Size pool is: {}", size());

        int repeat = 10;
        TransportWrapper transport = null;

        try {
            do {

                transport = transports.poll(1000, TimeUnit.MILLISECONDS);

                if (Objects.nonNull(transport) && !transport.isConnected()) {
                    transport.reconnect();
                }

                if (repeat <= 0)
                    break;

                repeat--;

                if (Objects.nonNull(transport) && !transport.isConnected())
                    releaseTransport(transport);

            } while (Objects.isNull(transport) || !transport.isConnected());

        } catch (InterruptedException e) {
            log.warn("Could not get Transport from pool, reason: {}", e.getMessage());
        }

        return transport;
    }


    @Override
    public void cleanAllTransports() {
        transports.clear();
    }


    @Override
    public void removeTransport(TransportWrapper transportWrapper) {
        transports.remove(transportWrapper);
    }

    @Override
    public int size() {
        return transports.size();
    }

}
