package com.haidousm.rona.common.requests;

import com.haidousm.rona.common.enums.Method;

public class GenericRequest implements Request {

    private String ipAddress;
    private int port;

    public void setIPAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Method getMethod() {
        return Method.UNDEFINED;
    }

    @Override
    public String getIPAddress() {
        return ipAddress;
    }

    @Override
    public int getPort() {
        return port;
    }
}
