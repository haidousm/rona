package com.haidousm.rona.common.responses;

import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.enums.Status;

public interface Response {
    void setStatus(Status status);
    Status getStatus();
    Method getMethod();
}
