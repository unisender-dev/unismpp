package com.smpp.transport;

import java.util.Date;

/**
 * Base message class with support transactional batch processing
 *
 * Created by v.maksymenko on 15.09.14.
 */
public interface TransactionalMessage {
    public Long getTransactionId();
    public void setTransactionId(Long messageTransactionId);

    public Date getTransactionDate();
    public void setTransactionDate(Date messageTransactionDate);

    public void clearTransaction();
}
