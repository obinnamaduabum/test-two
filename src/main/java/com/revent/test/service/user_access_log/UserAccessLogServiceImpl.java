package com.revent.test.service.user_access_log;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.revent.test.UserAccessLogoPojo;
import com.revent.test.dao.UserAccessLogDao;
import com.revent.test.entity.UserAccessLog;
import com.revent.test.projection.UserAccessProjection;
import com.revent.test.utils.MyUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

@Singleton
public class UserAccessLogServiceImpl implements UserAccessLogService {

    private final UserAccessLogDao userAccessLogDao;

    private final Logger logger = Logger.getLogger(UserAccessLogServiceImpl.class.getSimpleName());
    
    public UserAccessLogServiceImpl() {
        this.userAccessLogDao = new UserAccessLogDao();
    }

    @Override
    public void save(UserAccessLog userAccessLog) {
       // this.userAccessLogDao.save(userAccessLog);
    }

    public void init(String fileName, String startDateAsString, String endDateAsString, long range) {

        try {

            if(this.getTotalCount() <= 0L) {

                List<UserAccessLog> userAccessLogList = new ArrayList<>();

                File myObj = new File("src/main/resources/logs/"+ fileName);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    String[] splitValues = data.split("\\|");
                    UserAccessLog userAccessLog = new UserAccessLog();
                    userAccessLog.setDate(MyUtils.dateOfTypeStringToDate(splitValues[0], "yyyy-MM-dd HH:mm:ss"));
                    userAccessLog.setIp(splitValues[1]);
                    userAccessLog.setUserAgent(splitValues[4]);
                    userAccessLog.setStatus(splitValues[3]);
                    userAccessLogList.add(userAccessLog);
                }
                myReader.close();
                this.bulk(userAccessLogList);

            } else {
                this.logger.info("Already loaded!");
            }

            Date startDate = MyUtils.dateOfTypeStringToDate(startDateAsString, "yyyy-MM-dd HH:mm:ss");
            Date endDate = MyUtils.dateOfTypeStringToDate(endDateAsString, "yyyy-MM-dd HH:mm:ss");

            List<UserAccessLogoPojo> logList = this.findAllHavingMoreThanHundredWithDateRange(startDate, endDate, range);
            for(UserAccessLogoPojo userAccessLog: logList) {
                logger.info(new Gson().toJson(userAccessLog.getDate()));
            }

        } catch (FileNotFoundException | ParseException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void bulk(List<UserAccessLog> accessLogList) throws IllegalAccessException {
        this.userAccessLogDao.bulkSave(accessLogList);
    }

    @Override
    public Long getTotalCount() throws Exception {
        return this.userAccessLogDao.getTotalCount();
    }

    @Override
    public List<UserAccessLogoPojo> findAllHavingMoreThanHundredWithDateRange(Date startDate, Date endDate, long range) throws IllegalAccessException {
        return this.userAccessLogDao.findAllHavingMoreThanHundredWithDateRange(startDate, endDate, range);
    }
}
