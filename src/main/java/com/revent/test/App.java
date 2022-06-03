package com.revent.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.revent.test.service.user_access_log.UserAccessLogService;

public class App {

    public static void main( String[] args ) {

        Injector injector = Guice.createInjector(new AppInjector());
        UserAccessLogService userAccessLogService = injector.getInstance(UserAccessLogService.class);
        new Init();
    }

}
