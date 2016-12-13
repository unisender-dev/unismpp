package com.smpp.transport.server;

/**
 * Created by arnoid on 2/27/14.
 */
public class SmppServer extends AbstractServer {


    private String systemId;
    private String systemType;
    private Boolean isEnabled;
    private Integer priority;
    private SmppServerType serverType;
    private Boolean isMulticonnect;
    private Integer sendSpeed;

    public SmppServer() {}

    public SmppServer(Integer id,
                      String serverURL,
                      Integer port,
                      String systemId,
                      String password,
                      String systemType,
                      Integer isEnabled,
                      Integer isMulticonnect,
                      Integer priority) {

        super(id, serverURL, port, password);

        this.systemId = systemId;
        this.systemType = systemType;
        this.isEnabled = isEnabled == 1;
        this.isMulticonnect = isMulticonnect == 1;
        this.priority = priority;

    }

    public SmppServer(Integer id,
                      String serverURL,
                      Integer port,
                      String systemId,
                      String password,
                      String systemType,
                      Integer isEnabled,
                      Integer isMulticonnect,
                      Integer priority,
                      int sendSpeed) {

        super(id, serverURL, port, password);

        this.systemId = systemId;
        this.systemType = systemType;

        this.isEnabled = isEnabled == 1;
        this.isMulticonnect = isMulticonnect == 1;
        this.priority = priority;

        this.sendSpeed = sendSpeed;
    }


    public String getSystemId() { return systemId; }
    public void setSystemId(String systemId) { this.systemId = systemId; }

    public String getSystemType() { return systemType; }
    public void setSystemType(String systemType) { this.systemType = systemType; }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Boolean getIsMulticonnect() {
        return isMulticonnect;
    }

    public void setIsMulticonnect(Boolean isMulticonnect) {
        this.isMulticonnect = isMulticonnect;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public SmppServerType getServerType() {
        return serverType;
    }

    public void setServerType(SmppServerType serverType) {
        this.serverType = serverType;
    }

    public Integer getSendSpeed() {
        return sendSpeed;
    }

    public void setSendSpeed(Integer sendSpeed) {
        this.sendSpeed = sendSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SmppServer that = (SmppServer) o;

        if (systemId != null ? !systemId.equals(that.systemId) : that.systemId != null) return false;
        if (systemType != null ? !systemType.equals(that.systemType) : that.systemType != null) return false;
        if (isEnabled != null ? !isEnabled.equals(that.isEnabled) : that.isEnabled != null) return false;
        if (priority != null ? !priority.equals(that.priority) : that.priority != null) return false;
        if (serverType != that.serverType) return false;
        if (isMulticonnect != null ? !isMulticonnect.equals(that.isMulticonnect) : that.isMulticonnect != null)
            return false;
        return sendSpeed != null ? sendSpeed.equals(that.sendSpeed) : that.sendSpeed == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (systemId != null ? systemId.hashCode() : 0);
        result = 31 * result + (systemType != null ? systemType.hashCode() : 0);
        result = 31 * result + (isEnabled != null ? isEnabled.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (serverType != null ? serverType.hashCode() : 0);
        result = 31 * result + (isMulticonnect != null ? isMulticonnect.hashCode() : 0);
        result = 31 * result + (sendSpeed != null ? sendSpeed.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SmppServer [" +
                "id" + getId() +", "+
                "systemId='" + systemId + '\'' +
                ", hostname='" + getHostname() + '\'' +
                ", port='" + getPort() + '\'' +
                ", systemType='" + systemType + '\'' +
                ", isEnabled=" + isEnabled +
                ", priority=" + priority +
                ", serverType=" + serverType +
                ", isMulticonnect=" + isMulticonnect +
                ", sendSpeed=" + sendSpeed +
                ']';
    }

}
