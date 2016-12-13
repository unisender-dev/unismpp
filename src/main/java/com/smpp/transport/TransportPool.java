package com.smpp.transport;

/**
 * Created by rado on 12/7/15.
 */
public interface TransportPool {

    boolean isActiveTransport(long connectionTimeOut);

    boolean connect();

    void close();

    boolean isEstablishTransport(TransportWrapper transport,
                                 int availableReconnectCount,
                                 long connectionTimeOut);

    boolean isConnected();

    void cleanAllTransports();

    void releaseTransport(TransportWrapper transportWrapper);

    void removeTransport(TransportWrapper transportWrapper);

    TransportWrapper acquireTransport();

    int size();
}
