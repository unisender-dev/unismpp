package com.smpp.transport.message;

import com.smpp.transport.TransactionalMessage;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * User: gmayshmaz
 * Date: 6/13/13
 */
public abstract class Message implements Serializable, TransactionalMessage {

    /**
     * campaign_id - campaign ID
     */
    protected String campaignID;

    /**
     * user_guid - internal user GUID
     */

    protected String userID;

    /**
     * user_id - internal user ID
     */

    protected String userId;

    /**
     * Id of template used. If is filled html_body and text_body should be empty.
     */
    protected String templateId;

    /**
     * text_body - text content of message, can be empty if template_id is filled, can be up to 4096 Bytes.
     */
    protected String textBody;

    /**
     * vars - variables for template
     */
    protected Map<String, Object> vars;

    /**
     * extra_params - params used for blocking
     */
    protected Map<String, String> extraParams;

    /**
     * date using for TTL
     */
    protected Date date = new Date();


    protected Long size;

    protected Long transactionId;
    protected Date transactionDate;

    protected boolean isMonitorEvent;


    public boolean isMonitorEvent() {
        return isMonitorEvent;
    }

    public void setMonitorEvent(boolean isMonitorEvent) {
        this.isMonitorEvent = isMonitorEvent;
    }

    public String getCampaignID() {
        return campaignID;
    }
    public void setCampaignID(String campaignID) {
        this.campaignID = campaignID;
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTextBody() {
        return textBody;
    }
    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }

    public Map<String, Object> getVars() {
        return vars;
    }
    public void setVars(Map<String, Object> vars) {
        this.vars = vars;
    }

    public Map<String, String> getExtraParams() {
        return extraParams;
    }
    public void setExtraParams(Map<String, String> extraParams) {
        this.extraParams = extraParams;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public Long getSize() {
        return size;
    }
    public void setSize(Long size) {
        this.size = size;
    }

    public abstract String getID();
    public abstract String getSender();
    public abstract void cleanPreparedTextAndMimes();

    public abstract Message toLightweightMessage();

    protected Long calculateMessageSize() {
        Long size = 0L;

        if (vars != null && !vars.isEmpty()) {

            Iterator<Map.Entry<String, Object>> iterator = vars.entrySet().iterator();
            Map.Entry<String, Object> entry;

            while (iterator.hasNext()) {
                entry = iterator.next();

                size += entry.getKey().length();

                if (Objects.nonNull(entry.getValue())) {
                    size += entry.getValue().toString().length();
                }
            }

        }

        if (extraParams != null && !extraParams.isEmpty()) {
            Iterator<Map.Entry<String, String>> iterator = extraParams.entrySet().iterator();
            Map.Entry<String, String> entry;

            while (iterator.hasNext()) {
                entry = iterator.next();

                size += entry.getKey().length();

                if (Objects.nonNull(entry.getValue()))
                    size += entry.getValue().length();
            }

        }

        if (textBody != null)
            size += textBody.length();


        return size;
    }

    public Long getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void clearTransaction() {
        transactionId = null;
        transactionDate = null;
    }
}
