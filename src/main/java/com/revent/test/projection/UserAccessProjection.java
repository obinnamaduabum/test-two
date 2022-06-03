package com.revent.test.projection;

import java.util.Date;

public interface UserAccessProjection {

    Long getCount();

    Long getId();

    String getStatus();

    String getUserAgent();

    String getIp();

    Date getDate();
}
