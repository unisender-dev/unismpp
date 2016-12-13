package com.smpp.transport.server;

/**
 * http://www.nowsms.com/smpp-error-code-reference
 * Created by rado on 8/19/15.
 */
public enum SmppResponseError {

    ESME_RINVMSGLEN("1", "Invalid Message Length (sm_length parameter)"),
    ESME_RINVCMDLEN("2", "Invalid Command Length (command_length in SMPP PDU)"),
    ESME_RINVCMDID("3", "Invalid Command ID (command_id in SMPP PDU)"),
    ESME_RINVBNDSTS("4", "Incorrect BIND status for given command (example: trying to submit a message when bound only as a receiver)"),
    ESME_RALYBND("5", "ESME already in bound state (example: sending a second bind command during an existing SMPP session)"),
    ESME_RINVPRTFLG("6", "Invalid Priority Flag (priority_flag parameter)"),
    ESME_RINVREGDLVFLG("7", "Invalid Regstered Delivery Flag (registered_delivery parameter)"),
    ESME_RSYSERR("8", "System Error (indicates server problems on the SMPP host)"),
    ESME_RINVSRCADR("10", "Invalid source address (sender/source address is not valid)"),
    ESME_RINVDSTADR("11", "Invalid destination address (recipient/destination phone number is not valid)"),
    ESME_RINVMSGID("12", "Message ID is invalid (error only relevant to query_sm, replace_sm, cancel_sm commands)"),
    ESME_RBINDFAIL("13", "Bind failed (login/bind failed – invalid login credentials or login restricted by IP address)"),
    ESME_RINVPASWD("14", "Invalid password (login/bind failed)"),
    ESME_RINVSYSID("15", "Invalid System ID (login/bind failed – invalid username / system id)"),

    ESME_RCANCELFAIL("17", "cancel_sm request failed"),
    ESME_RREPLACEFAIL("19", "replace_sm request failed"),
    ESME_RMSGQFUL("20", "Message Queue Full (This can indicate that the SMPP server has too many queued messages and temporarily cannot accept any more messages. It can also indicate that the SMPP server has too many messages pending for the specified recipient and will not accept any more messages for this recipient until it is able to deliver messages that are already in the queue to this recipient.)"),
    ESME_RINVSERTYP("21", "Invalid service_type value"),
    ESME_RINVNUMDESTS("51", "Invalid number_of_dests value in submit_multi request"),
    ESME_RINVDLNAME("52", "Invalid distribution list name in submit_multi request"),
    ESME_RINVDESTFLAG("64", "Invalid dest_flag in submit_multi request"),
    ESME_RINVSUBREP("66", "Invalid ‘submit with replace’ request (replace_if_present flag set)"),
    ESME_RINVESMCLASS("67", "Invalid esm_class field data"),
    ESME_RCNTSUBDL("68", "Cannot submit to distribution list (submit_multi request)"),
    ESME_RSUBMITFAIL("69", "Submit message failed"),

    ESME_RTHROTTLED("88", "Throttling error (This indicates that you are submitting messages at a rate that is faster than the provider allows)"),

// errors on Unicore side, message didn't send, some problem with session
    RECONNECT_CANNTSUBMIT("500", "Cannot submitShortMessage while session in reconnect state"),
    RECONNECT_RSPCTIMEOUT("501", "No response after waiting for 3000 millis when executing submit_sm"),
    RECONNECT_FAILEDSUBMIT("502", "Failed sending submitShortMessage command");

    private final String errCode, errMsg;

    SmppResponseError(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public static SmppResponseError contains(String errorCode) {

        if (RECONNECT_CANNTSUBMIT.errCode.equals(errorCode))
            return RECONNECT_CANNTSUBMIT;

        else if (RECONNECT_RSPCTIMEOUT.errCode.equals(errorCode))
            return RECONNECT_RSPCTIMEOUT;

        else if (RECONNECT_FAILEDSUBMIT.errCode.equals(errorCode))
            return RECONNECT_FAILEDSUBMIT;

        else if (ESME_RINVMSGLEN.errCode.equals(errorCode))
            return ESME_RINVMSGLEN;

        else if (ESME_RINVCMDLEN.errCode.equals(errorCode))
            return ESME_RINVCMDLEN;

        else if (ESME_RINVCMDID.errCode.equals(errorCode))
            return ESME_RINVCMDID;

        else if (ESME_RINVBNDSTS.errCode.equals(errorCode))
            return ESME_RINVBNDSTS;

        else if (ESME_RALYBND.errCode.equals(errorCode))
            return ESME_RALYBND;

        else if (ESME_RINVPRTFLG.errCode.equals(errorCode))
            return ESME_RINVPRTFLG;

        else if (ESME_RINVREGDLVFLG.errCode.equals(errorCode))
            return ESME_RINVREGDLVFLG;

        else if (ESME_RSYSERR.errCode.equals(errorCode))
            return ESME_RSYSERR;

        else if (ESME_RINVSRCADR.errCode.equals(errorCode))
            return ESME_RINVSRCADR;

        else if (ESME_RINVDSTADR.errCode.equals(errorCode))
            return ESME_RINVDSTADR;

        else if (ESME_RINVMSGID.errCode.equals(errorCode))
            return ESME_RINVMSGID;

        else if (ESME_RBINDFAIL.errCode.equals(errorCode))
            return ESME_RBINDFAIL;

        else if (ESME_RINVPASWD.errCode.equals(errorCode))
            return ESME_RINVPASWD;

        else if (ESME_RINVSYSID.errCode.equals(errorCode))
            return ESME_RINVSYSID;

        else if (ESME_RCANCELFAIL.errCode.equals(errorCode))
            return ESME_RCANCELFAIL;

        else if (ESME_RREPLACEFAIL.errCode.equals(errorCode))
            return ESME_RREPLACEFAIL;

        else if (ESME_RMSGQFUL.errCode.equals(errorCode))
            return ESME_RMSGQFUL;

        else if (ESME_RINVSERTYP.errCode.equals(errorCode))
            return ESME_RINVSERTYP;

        else if (ESME_RINVNUMDESTS.errCode.equals(errorCode))
            return ESME_RINVNUMDESTS;

        else if (ESME_RINVDLNAME.errCode.equals(errorCode))
            return ESME_RINVDLNAME;

        else if (ESME_RINVDESTFLAG.errCode.equals(errorCode))
            return ESME_RINVDESTFLAG;

        else if (ESME_RINVSUBREP.errCode.equals(errorCode))
            return ESME_RINVSUBREP;

        else if (ESME_RINVESMCLASS.errCode.equals(errorCode))
            return ESME_RINVESMCLASS;

        else if (ESME_RCNTSUBDL.errCode.equals(errorCode))
            return ESME_RCNTSUBDL;

        else if (ESME_RSUBMITFAIL.errCode.equals(errorCode))
            return ESME_RSUBMITFAIL;

        else if (ESME_RTHROTTLED.errCode.equals(errorCode))
            return ESME_RTHROTTLED;


        else
            return null;
    }

    /**
     * we deside, do we need to resend sms with this smpp response error.
     * @return
     * true - mean that it's critical error of this sms amd it'll never be send;
     * false - can be resend;
     */
    public boolean isCriticalResponseError() {

        if (ESME_RINVMSGLEN.errCode
                .equals(this.errCode))
            return true;

        else if (ESME_RINVCMDLEN.errCode
                .equals(this.errCode))
            return true;

        else if (ESME_RINVCMDID.errCode
                .equals(this.errCode))
            return true;

        else if (ESME_RINVSRCADR.errCode
                .equals(this.errCode))
            return true;

        else if (ESME_RINVDSTADR.errCode
                .equals(this.errCode))
            return true;

        else if (RECONNECT_RSPCTIMEOUT.errCode
                .equals(this.errCode))
            return true;

        else
            return false;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getErrCode() {
        return errCode;
    }
}
