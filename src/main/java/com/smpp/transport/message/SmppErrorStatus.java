package com.smpp.transport.message;

/**
 * ErrorCode got from http://smpp.w2wts.com/SMPP_Developers.aspx
 * Created by rado on 3/9/15.
 */
public enum SmppErrorStatus {

    OK_SENT("0", "000"), // Successfully Delivered
    ABSENT_SUBSCRIBER("1", "001"), // Absent subscriber (network cannot contact subscriber)
    HANDSET_MEMORY_EXCEEDED("2", "002"),// Handset memory exceeded
    EQUIPMENT_PROTOCOL_ERROR("3", "003"), // Equipment protocol error
    NOT_EQUIPPED_WITH_SHORT_MESSAGE("4", "004"), // Equipment not equipped with short-message capability
    UNKNOWN_SERVICE_CENTER("5", "005"), // Unknown service centre (destination MSC)
    TOO_MUCH_INBOUND_TRAFFIC("6", "006"), // Service centre congestion (Too much inbound traffic at destination network)
    INVALID_SME_ADDRESS("7", "007"), // Invalid SME address
    NOT_SC_SUBSCRIBER("8", "008"), // Subscriber is not a SC subscriber (Number belongs to a different destination network but HLR is not updated)
    UNKNOWN_SUBSCRIBER("9", "009"), // Unknown subscriber (IMSI is unknown in the HLR)
    ILLEGAL_SUBSCRIBER("10", "00a"), // Illegal subscriber (The mobile station failed authentication)
    TELESERVICE_NOT_PROVISIONED("11", "00b"), // Teleservice not provisioned (Mobile subscription identified by the MSISDN number does not include the short message service)
    ILLEGAL_EQUIPMENT("12", "00c"), // Illegal equipment (IMEI check failed, blacklisted or not whitelisted)
    CALL_BARRED("13", "00d"), // Call barred (Operator barred the MSISDN number)
    FACILITY_NOT_SUPPORTED("14", "00e"), // Facility not supported (VLR in the PLMN does not support MT short message service)
    SUBSCRIBER_BUSY("15", "00f"), // Subscriber busy for MT short message
    SYSTEM_FAILURE("16", "010"), // System failure
    MESSAGE_WAITING("17", "011"), // Message waiting list at HLR is full
    DATA_MISSING("18", "012"), // Data missing
    UNEXPECTED_DATA_VALUE("19", "013"), // Unexpected data value
    RESOURCE_LIMITATION_AT_PEER("20", "014"), // Resource limitation at peer / destination network
    DUPLICATE_INVOKE_ID("21", "015"), // TCAP error (Duplicate invoke ID)
    NOT_SUPPORTED_SERVICE("22", "016"), // TCAP error (Not supported service)
    MISTYPED_PARAMETER("23", "017"), // TCAP error (Mistyped parameter)
    RESOURCE_LIMITATION("24", "018"), // TCP error (Resource Limitation)
    TCAP_INITIATING_RELEASE("25", "019"), // TCAP error (TCAP initiating release)
    UNEXPECTED_RESPONSE_FROM_PEER("26", "01a"), // TCAP error (Unexpected response from peer)
    SERVICE_COMPLETION_FAILURE("27", "01b"), // TCAP error (Service completion failure)
    NO_RESPONSE_FROM_PEER("28", "01c"), // TCAP error (No response from peer)
    INVALID_RESPONSE_RECEIVED("29", "01d"), // TCAP error (Invalid response received)
    UNIDENTIFIED_SUBSCRIBER("30", "01e"), // Unidentified subscriber
    SERVICE_TEMPORARY_NOT_AVAILABLE("31", "01f"), // Service temporary not available
    ILLEGAL_ERROR_CODE("32", "020"), // Illegal error code
    NETWORK_TIMEOUT("33", "021"), // Network timeout
    OPERATION_BARRED("34", "022"), // Operation Barred (From the MNO)
    DELIVERY_FAILED("35", "023"), // Delivery fail
    ERROR_IN_MS("36", "024"), // Error in MS
    PLMN_SYSTEM_FAILURE("37", "025"), // PLMN system failure
    HLR_SYSTEM_FAILURE("38", "026"), // HLR system failure
    VLR_SYSTEM_FAILURE("39", "027"), // VLR system failure
    CONTROLLING_MSC_FAILURE("40", "028"), // Controlling MSC failure (Error in the MSC where the user is located)
    VISITED_MSC_FAILURE("41", "029"), // Visited MSC failure (Error in the visited MSC)
    MNP_OTHER_OPERATOR_NOT_ALLOWED("42", "02a"), // MNP other operator not allowed (MSISDN is ported, delivery via this SMSC not permitted)
    SUBSCRIBER_TEMPORARILY_UNREACHABLE("43", "02b"), // Subscriber temporarily unreachable (While roaming)
    MESSAGE_STORE_BUSY("44", "02c"), // Message store busy
    SME_INTERFACE_BUSY("45", "02d"), // SME Interface busy
    CLOSED_USER_GROUP_REJECT("46", "02e"), // Closed user group reject
    PROVIDER_FAILURE("47", "02f"), // Network failure
    DEFERRED_DELIVERY("48", "030"), // Deferred Delivery (Message has not been delivered and is part of a deferred delivery schedule)
    ERROR_GETTING_ROUTE("49", "031"), // Error getting route
    INSUFFICIENT_CREDIT("50", "032"), // Insufficient credit

    REJECTED_DESTINATION("51", "033"), // Rejected Destination
    REJECTED_UNKNOWN_REASON("52", "034"), // Rejected Unknown Reason
    REJECTED_ROUTING_ISSUE("53", "035"), // Rejected due to routing issue
    REJECTED_BLOCKING_ISSUE("54", "036"), // Rejected due to blocking issue
    REJECTED_NO_PRICE("55", "037"), // Rejected due to no price
    REJECTED_NOT_ENOUGH_MONEY("56", "038"), //Rejected due to not enough credits
    REJECTED_SPAM_FILTER("57", "039"), // Rejected due to spam filter
    REJECTED_FLOODING("58", "03a"), // Rejected due to flooding

    UNKNOWN_TCTYPE_SMS("59", "03b"), // UNKNOWN TCType,SMS
    UNKNOWN_TCTYPE_SRI("60", "03c"), // UNKNOWN TCType,SRI
    UNKNOWN("61", "03d"), // UNKNOWN

    FAILURE_TO_SUBMISSION("62", "03e"), // Failure due to submission towards AA19 destination
    UNABLE_TO_CONFIRM("63", "03f"), // Sent to SME but unable to confirm
    REPLACED("64", "040"),// Replaced at the SMSC
    QUALITY_SERVICE_NOT_AVAILABLE("65", "041"), // Quality service not available
    ERROR_IN_SMSC("66", "042"), // Error in SMSC

    REJECTED_PERIOD_EXPIRY("67", "043"), // Rejected by operator due to validity period expiry

    RETRYING("68", "044"), // Intermediate state notification that the message has not yet been delivered due to a phone related problem but is being retried.

    // Cannot determine whether this message has been delivered or has failed due to lack of final delivery state information from the operator.

    CANCELED("82", "052"), // Local Cancel (Temporary problem / Lost reach)

    EXPIRED("88", "058"), // Expired due to no response from supplier


    DELETE_BY_SENDER("95", "05f"), // SM deleted by the sender
    DELETE_BY_ADMIN("96", "060"), // SM deleted by SMSC Admin

    INVALID_FORMAT("778", "778"), //  if NOT (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass()))
    TOO_LARGE("777", "778"); // realize by SMSMessageValidator.MAX_TEXT_BODY_LENGTH

    public final String errCodeDEC;
    public final String errCodeHEX;

    SmppErrorStatus(String errCodeDEC, String errCodeHEX) {
        this.errCodeDEC = errCodeDEC;
        this.errCodeHEX = errCodeHEX;
    }

    public static SmppErrorStatus contains(String errorCode) {

        if (OK_SENT.errCodeDEC.equals(errorCode)
                || OK_SENT.errCodeHEX.equals(errorCode))
            return OK_SENT;

        if (ABSENT_SUBSCRIBER.errCodeDEC.equals(errorCode)
                || ABSENT_SUBSCRIBER.errCodeHEX.equals(errorCode))
            return ABSENT_SUBSCRIBER;

        if (HANDSET_MEMORY_EXCEEDED.errCodeDEC.equals(errorCode)
                || HANDSET_MEMORY_EXCEEDED.errCodeHEX.equals(errorCode))
            return HANDSET_MEMORY_EXCEEDED;

        if (EQUIPMENT_PROTOCOL_ERROR.errCodeDEC.equals(errorCode)
                || EQUIPMENT_PROTOCOL_ERROR.errCodeHEX.equals(errorCode))
            return EQUIPMENT_PROTOCOL_ERROR;

        if (NOT_EQUIPPED_WITH_SHORT_MESSAGE.errCodeDEC.equals(errorCode)
                || NOT_EQUIPPED_WITH_SHORT_MESSAGE.errCodeHEX.equals(errorCode))
            return NOT_EQUIPPED_WITH_SHORT_MESSAGE;

        if (UNKNOWN_SERVICE_CENTER.errCodeDEC.equals(errorCode)
                || UNKNOWN_SERVICE_CENTER.errCodeHEX.equals(errorCode))
            return UNKNOWN_SERVICE_CENTER;

        if (TOO_MUCH_INBOUND_TRAFFIC.errCodeDEC.equals(errorCode)
                || TOO_MUCH_INBOUND_TRAFFIC.errCodeHEX.equals(errorCode))
            return TOO_MUCH_INBOUND_TRAFFIC;

        if (INVALID_SME_ADDRESS.errCodeDEC.equals(errorCode)
                || INVALID_SME_ADDRESS.errCodeHEX.equals(errorCode))
            return INVALID_SME_ADDRESS;

        if (NOT_SC_SUBSCRIBER.errCodeDEC.equals(errorCode)
                || NOT_SC_SUBSCRIBER.errCodeHEX.equals(errorCode))
            return NOT_SC_SUBSCRIBER;

        if (UNKNOWN_SUBSCRIBER.errCodeDEC.equals(errorCode)
                || UNKNOWN_SUBSCRIBER.errCodeHEX.equals(errorCode))
            return UNKNOWN_SUBSCRIBER;

        if (ILLEGAL_SUBSCRIBER.errCodeDEC.equals(errorCode)
                || ILLEGAL_SUBSCRIBER.errCodeHEX.equals(errorCode))
            return ILLEGAL_SUBSCRIBER;

        if (TELESERVICE_NOT_PROVISIONED.errCodeDEC.equals(errorCode)
                || TELESERVICE_NOT_PROVISIONED.errCodeHEX.equals(errorCode))
            return TELESERVICE_NOT_PROVISIONED;

        if (ILLEGAL_EQUIPMENT.errCodeDEC.equals(errorCode)
                || ILLEGAL_EQUIPMENT.errCodeHEX.equals(errorCode))
            return ILLEGAL_EQUIPMENT;

        if (CALL_BARRED.errCodeDEC.equals(errorCode)
                || CALL_BARRED.errCodeHEX.equals(errorCode))
            return CALL_BARRED;

        if (FACILITY_NOT_SUPPORTED.errCodeDEC.equals(errorCode)
                || FACILITY_NOT_SUPPORTED.errCodeHEX.equals(errorCode))
            return FACILITY_NOT_SUPPORTED;

        if (SUBSCRIBER_BUSY.errCodeDEC.equals(errorCode)
                || SUBSCRIBER_BUSY.errCodeHEX.equals(errorCode))
            return SUBSCRIBER_BUSY;

        if (SYSTEM_FAILURE.errCodeDEC.equals(errorCode)
                || SYSTEM_FAILURE.errCodeHEX.equals(errorCode))
            return SYSTEM_FAILURE;

        if (MESSAGE_WAITING.errCodeDEC.equals(errorCode)
                || MESSAGE_WAITING.errCodeHEX.equals(errorCode))
            return MESSAGE_WAITING;

        if (DATA_MISSING.errCodeDEC.equals(errorCode)
                || DATA_MISSING.errCodeHEX.equals(errorCode))
            return DATA_MISSING;

        if (UNEXPECTED_DATA_VALUE.errCodeDEC.equals(errorCode)
                || UNEXPECTED_DATA_VALUE.errCodeHEX.equals(errorCode))
            return UNEXPECTED_DATA_VALUE;

        if (RESOURCE_LIMITATION_AT_PEER.errCodeDEC.equals(errorCode)
                || RESOURCE_LIMITATION_AT_PEER.errCodeHEX.equals(errorCode))
            return RESOURCE_LIMITATION_AT_PEER;

        if (DUPLICATE_INVOKE_ID.errCodeDEC.equals(errorCode)
                || DUPLICATE_INVOKE_ID.errCodeHEX.equals(errorCode))
            return DUPLICATE_INVOKE_ID;

        if (NOT_SUPPORTED_SERVICE.errCodeDEC.equals(errorCode)
                || NOT_SUPPORTED_SERVICE.errCodeHEX.equals(errorCode))
            return NOT_SUPPORTED_SERVICE;

        if (MISTYPED_PARAMETER.errCodeDEC.equals(errorCode)
                || MISTYPED_PARAMETER.errCodeHEX.equals(errorCode))
            return MISTYPED_PARAMETER;

        if (RESOURCE_LIMITATION.errCodeDEC.equals(errorCode)
                || RESOURCE_LIMITATION.errCodeHEX.equals(errorCode))
            return RESOURCE_LIMITATION;

        if (TCAP_INITIATING_RELEASE.errCodeDEC.equals(errorCode)
                || TCAP_INITIATING_RELEASE.errCodeHEX.equals(errorCode))
            return TCAP_INITIATING_RELEASE;

        if (UNEXPECTED_RESPONSE_FROM_PEER.errCodeDEC.equals(errorCode)
                || UNEXPECTED_RESPONSE_FROM_PEER.errCodeHEX.equals(errorCode))
            return UNEXPECTED_RESPONSE_FROM_PEER;

        if (SERVICE_COMPLETION_FAILURE.errCodeDEC.equals(errorCode)
                || SERVICE_COMPLETION_FAILURE.errCodeHEX.equals(errorCode))
            return SERVICE_COMPLETION_FAILURE;

        if (NO_RESPONSE_FROM_PEER.errCodeDEC.equals(errorCode)
                || NO_RESPONSE_FROM_PEER.errCodeHEX.equals(errorCode))
            return NO_RESPONSE_FROM_PEER;

        if (INVALID_RESPONSE_RECEIVED.errCodeDEC.equals(errorCode)
                || INVALID_RESPONSE_RECEIVED.errCodeHEX.equals(errorCode))
            return INVALID_RESPONSE_RECEIVED;

        if (UNIDENTIFIED_SUBSCRIBER.errCodeDEC.equals(errorCode)
                || UNIDENTIFIED_SUBSCRIBER.errCodeHEX.equals(errorCode))
            return UNIDENTIFIED_SUBSCRIBER;

        if (SERVICE_TEMPORARY_NOT_AVAILABLE.errCodeDEC.equals(errorCode)
                || SERVICE_TEMPORARY_NOT_AVAILABLE.errCodeHEX.equals(errorCode))
            return SERVICE_TEMPORARY_NOT_AVAILABLE;

        if (ILLEGAL_ERROR_CODE.errCodeDEC.equals(errorCode)
                || ILLEGAL_ERROR_CODE.errCodeHEX.equals(errorCode))
            return ILLEGAL_ERROR_CODE;

        if (OPERATION_BARRED.errCodeDEC.equals(errorCode)
                || OPERATION_BARRED.errCodeHEX.equals(errorCode))
            return OPERATION_BARRED;

        if (DELIVERY_FAILED.errCodeDEC.equals(errorCode)
                || DELIVERY_FAILED.errCodeHEX.equals(errorCode))
            return DELIVERY_FAILED;

        if (ERROR_IN_MS.errCodeDEC.equals(errorCode)
                || ERROR_IN_MS.errCodeHEX.equals(errorCode))
            return ERROR_IN_MS;

        if (PLMN_SYSTEM_FAILURE.errCodeDEC.equals(errorCode)
                || PLMN_SYSTEM_FAILURE.errCodeHEX.equals(errorCode))
            return PLMN_SYSTEM_FAILURE;

        if (HLR_SYSTEM_FAILURE.errCodeDEC.equals(errorCode)
                || HLR_SYSTEM_FAILURE.errCodeHEX.equals(errorCode))
            return HLR_SYSTEM_FAILURE;

        if (VLR_SYSTEM_FAILURE.errCodeDEC.equals(errorCode)
                || VLR_SYSTEM_FAILURE.errCodeHEX.equals(errorCode))
            return VLR_SYSTEM_FAILURE;

        if (CONTROLLING_MSC_FAILURE.errCodeDEC.equals(errorCode)
                || CONTROLLING_MSC_FAILURE.errCodeHEX.equals(errorCode))
            return CONTROLLING_MSC_FAILURE;

        if (VISITED_MSC_FAILURE.errCodeDEC.equals(errorCode)
                || VISITED_MSC_FAILURE.errCodeHEX.equals(errorCode))
            return VISITED_MSC_FAILURE;

        if (MNP_OTHER_OPERATOR_NOT_ALLOWED.errCodeDEC.equals(errorCode)
                || MNP_OTHER_OPERATOR_NOT_ALLOWED.errCodeHEX.equals(errorCode))
            return MNP_OTHER_OPERATOR_NOT_ALLOWED;

        if (SUBSCRIBER_TEMPORARILY_UNREACHABLE.errCodeDEC.equals(errorCode)
                || SUBSCRIBER_TEMPORARILY_UNREACHABLE.errCodeHEX.equals(errorCode))
            return SUBSCRIBER_TEMPORARILY_UNREACHABLE;

        if (MESSAGE_STORE_BUSY.errCodeDEC.equals(errorCode)
                || MESSAGE_STORE_BUSY.errCodeHEX.equals(errorCode))
            return MESSAGE_STORE_BUSY;

        if (SME_INTERFACE_BUSY.errCodeDEC.equals(errorCode)
                || SME_INTERFACE_BUSY.errCodeHEX.equals(errorCode))
            return SME_INTERFACE_BUSY;

        if (CLOSED_USER_GROUP_REJECT.errCodeDEC.equals(errorCode)
                || CLOSED_USER_GROUP_REJECT.errCodeHEX.equals(errorCode))
            return CLOSED_USER_GROUP_REJECT;

        if (PROVIDER_FAILURE.errCodeDEC.equals(errorCode)
                || PROVIDER_FAILURE.errCodeHEX.equals(errorCode))
            return PROVIDER_FAILURE;

        if (DEFERRED_DELIVERY.errCodeDEC.equals(errorCode)
                || DEFERRED_DELIVERY.errCodeHEX.equals(errorCode))
            return DEFERRED_DELIVERY;

        if (ERROR_GETTING_ROUTE.errCodeDEC.equals(errorCode)
                || ERROR_GETTING_ROUTE.errCodeHEX.equals(errorCode))
            return ERROR_GETTING_ROUTE;

        if (INSUFFICIENT_CREDIT.errCodeDEC.equals(errorCode)
                || INSUFFICIENT_CREDIT.errCodeHEX.equals(errorCode))
            return INSUFFICIENT_CREDIT;

        if (REJECTED_DESTINATION.errCodeDEC.equals(errorCode)
                || REJECTED_DESTINATION.errCodeHEX.equals(errorCode))
            return REJECTED_DESTINATION;

        if (REJECTED_UNKNOWN_REASON.errCodeDEC.equals(errorCode)
                || REJECTED_UNKNOWN_REASON.errCodeHEX.equals(errorCode))
            return REJECTED_UNKNOWN_REASON;

        if (REJECTED_ROUTING_ISSUE.errCodeDEC.equals(errorCode)
                || REJECTED_ROUTING_ISSUE.errCodeHEX.equals(errorCode))
            return REJECTED_ROUTING_ISSUE;

        if (REJECTED_BLOCKING_ISSUE.errCodeDEC.equals(errorCode)
                || REJECTED_BLOCKING_ISSUE.errCodeHEX.equals(errorCode))
            return REJECTED_BLOCKING_ISSUE;

        if (REJECTED_NO_PRICE.errCodeDEC.equals(errorCode)
                || REJECTED_NO_PRICE.errCodeHEX.equals(errorCode))
            return REJECTED_NO_PRICE;

        if (REJECTED_NOT_ENOUGH_MONEY.errCodeDEC.equals(errorCode)
                || REJECTED_NOT_ENOUGH_MONEY.errCodeHEX.equals(errorCode))
            return REJECTED_NOT_ENOUGH_MONEY;

        if (REJECTED_SPAM_FILTER.errCodeDEC.equals(errorCode)
                || REJECTED_SPAM_FILTER.errCodeHEX.equals(errorCode))
            return REJECTED_SPAM_FILTER;

        if (REJECTED_FLOODING.errCodeDEC.equals(errorCode)
                || REJECTED_FLOODING.errCodeHEX.equals(errorCode))
            return REJECTED_FLOODING;

        if (UNKNOWN_TCTYPE_SMS.errCodeDEC.equals(errorCode)
                || UNKNOWN_TCTYPE_SMS.errCodeHEX.equals(errorCode))
            return UNKNOWN_TCTYPE_SMS;

        if (UNKNOWN_TCTYPE_SRI.errCodeDEC.equals(errorCode)
                || UNKNOWN_TCTYPE_SRI.errCodeHEX.equals(errorCode))
            return UNKNOWN_TCTYPE_SRI;

        if (UNKNOWN.errCodeDEC.equals(errorCode)
                || UNKNOWN.errCodeHEX.equals(errorCode))
            return UNKNOWN;

        if (FAILURE_TO_SUBMISSION.errCodeDEC.equals(errorCode)
                || FAILURE_TO_SUBMISSION.errCodeHEX.equals(errorCode))
            return FAILURE_TO_SUBMISSION;

        if (UNABLE_TO_CONFIRM.errCodeDEC.equals(errorCode)
                || UNABLE_TO_CONFIRM.errCodeHEX.equals(errorCode))
            return UNABLE_TO_CONFIRM;

        if (REPLACED.errCodeDEC.equals(errorCode)
                || REPLACED.errCodeHEX.equals(errorCode))
            return REPLACED;

        if (QUALITY_SERVICE_NOT_AVAILABLE.errCodeDEC.equals(errorCode)
                || QUALITY_SERVICE_NOT_AVAILABLE.errCodeHEX.equals(errorCode))
            return QUALITY_SERVICE_NOT_AVAILABLE;

        if (ERROR_IN_SMSC.errCodeDEC.equals(errorCode)
                || ERROR_IN_SMSC.errCodeHEX.equals(errorCode))
            return ERROR_IN_SMSC;

        if (REJECTED_PERIOD_EXPIRY.errCodeDEC.equals(errorCode)
                || REJECTED_PERIOD_EXPIRY.errCodeHEX.equals(errorCode))
            return REJECTED_PERIOD_EXPIRY;

        if (RETRYING.errCodeDEC.equals(errorCode)
                || RETRYING.errCodeHEX.equals(errorCode))
            return RETRYING;

        else if (CANCELED.errCodeDEC.equals(errorCode)
                || CANCELED.errCodeHEX.equals(errorCode))
            return CANCELED;

        else if (EXPIRED.errCodeDEC.equals(errorCode)
                 || EXPIRED.errCodeHEX.equals(errorCode))
            return EXPIRED;

        else if (INVALID_FORMAT.errCodeDEC.equals(errorCode)
                || INVALID_FORMAT.errCodeHEX.equals(errorCode))
            return INVALID_FORMAT;

        else if (DELETE_BY_SENDER.errCodeDEC.equals(errorCode)
                || DELETE_BY_SENDER.errCodeHEX.equals(errorCode))
            return DELETE_BY_SENDER;

        else if (DELETE_BY_ADMIN.errCodeDEC.equals(errorCode)
                || DELETE_BY_ADMIN.errCodeHEX.equals(errorCode))
            return DELETE_BY_ADMIN;

        else
            return null;

    }

}
