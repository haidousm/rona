package com.haidousm.rona.common.requests;

import com.haidousm.rona.common.enums.Method;

public interface Request {
    Method getMethod();

    String getIPAddress();

    void setIPAddress(String ipAddress);

    int getPort();

    void setPort(int port);

    String getBody();
}
