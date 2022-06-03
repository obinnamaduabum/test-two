package com.revent.test;

import com.google.inject.Inject;
import com.revent.test.service.user_access_log.UserAccessLogService;
import com.revent.test.service.user_access_log.UserAccessLogServiceImpl;

public class Init {

    @Inject
    private UserAccessLogService userAccessLogService;

    public Init() {
        userAccessLogService = new UserAccessLogServiceImpl();
        this.userAccessLogService.init("user_access.txt");
    }
}
