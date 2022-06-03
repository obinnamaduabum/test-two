package com.revent.test;

import com.google.inject.AbstractModule;
import com.revent.test.service.blocked_ip.BlockedIPService;
import com.revent.test.service.blocked_ip.BlockedIPServiceImpl;
import com.revent.test.service.user_access_log.UserAccessLogService;
import com.revent.test.service.user_access_log.UserAccessLogServiceImpl;

public class AppInjector extends AbstractModule {

    public void configure() {
        bind(UserAccessLogService.class).to(UserAccessLogServiceImpl.class);
        bind(BlockedIPService.class).to(BlockedIPServiceImpl.class);

        // bind(OrderDAO.class).to(JdbcOrderDAO.class);
    }
}
