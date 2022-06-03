package com.revent.test;

import com.google.inject.Inject;
import com.revent.test.service.user_access_log.UserAccessLogService;
import com.revent.test.service.user_access_log.UserAccessLogServiceImpl;

public class Init {

    private UserAccessLogService userAccessLogService;

    public Init() {
        userAccessLogService = new UserAccessLogServiceImpl();
        this.userAccessLogService.init("user_access.txt", "2022-01-01 13:00:00", "2022-01-01 14:00:00", 200L);
    }
}
