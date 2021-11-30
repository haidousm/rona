package com.haidousm.rona.common.requests;

import com.haidousm.rona.common.enums.Method;
import com.sun.istack.Nullable;

public class GenericRequest implements Request {

    @Nullable
    private String ipAddress;
    @Nullable
    private int port;
    Method method;
    private String body;

    public void setIPAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIPAddress() {
        return ipAddress;
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
