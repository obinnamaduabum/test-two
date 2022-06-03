package com.revent.test.service.user_access_log;


import com.revent.test.UserAccessLogoPojo;
import com.revent.test.entity.UserAccessLog;

import java.util.Date;
import java.util.List;

public interface UserAccessLogService {

    void init(String fileName, String startDateAsString, String endDateAsString, long range);

    void save(UserAccessLog userAccessLog);

    void bulk(List<UserAccessLog> accessLogList) throws IllegalAccessException;

    Long getTotalCount() throws Exception;

    List<UserAccessLogoPojo> findAllHavingMoreThanHundredWithDateRange(Date startDate, Date endDate, long range) throws IllegalAccessException;
}