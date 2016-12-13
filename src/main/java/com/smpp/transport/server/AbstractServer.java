package com.smpp.transport.server;

import java.io.Serializable;

/**
 * Created by rado on 5/14/14.
 */
public abstract class AbstractServer implements Serializable {

    private Integer id;
    private String hostname;
    private Integer port;
    private String password;

    public AbstractServer() {}

    public AbstractServer(Integer id, String serverURL,
                          Integer port, String password) {
        this.id = id;
        this.hostname = serverURL;
        this.port = port;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }
    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "id=" + id + ", " + hostname + ":" + port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractServer)) return false;

        AbstractServer that = (AbstractServer) o;

        if (!hostname.equals(that.hostname)) return false;
        if (!id.equals(that.id)) return false;
        if (!password.equals(that.password)) return false;
        if (!port.equals(that.port)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + hostname.hashCode();
        result = 31 * result + port.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

}
