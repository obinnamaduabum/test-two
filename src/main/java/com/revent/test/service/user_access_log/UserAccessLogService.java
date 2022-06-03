package com.revent.test.service.user_access_log;


import com.revent.test.entity.UserAccessLog;
import com.revent.test.projection.UserAccessProjection;

import java.util.Date;
import java.util.List;

public interface UserAccessLogService {

    void init(String fileName);

    void save(UserAccessLog userAccessLog);

    void bulk(List<UserAccessLog> accessLogList) throws IllegalAccessException;

    Long getTotalCount() throws Exception;

    List<UserAccessProjection> findAllHavingMoreThanHundredWithDateRange(Date startDate, Date endDate);

    List<UserAccessProjection> findAllHavingMoreThanHundredWithDateRangeWithDateAsString(String startDate, String endDate);
}
