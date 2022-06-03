package com.revent.test.dao;

import com.google.gson.Gson;
import com.revent.test.UserAccessLogoPojo;
import com.revent.test.db.JdbcConnection;
import com.revent.test.entity.UserAccessLog;
import com.revent.test.utils.MyUtils;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Logger;


public class UserAccessLogDao {

    private final Logger logger = Logger.getLogger(UserAccessLogDao.class.getSimpleName());

    private final Optional<Connection> connection;

    public UserAccessLogDao() {
        this.connection = JdbcConnection.getConnection();
    }

    public Long getTotalCount() throws Exception {
        if(!connection.isPresent()) {
            throw new IllegalAccessException("No connection found");
        }

        long totalCount = 0L;
        String sql = "select count(*) as rowCount from user_access_log";

        try {

            Connection conn = connection.get();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                totalCount = (long) resultSet.getInt(1);
                System.out.println("numberOfRows= " + totalCount);
            } else {
                System.out.println("error: could not get the record counts");
            }

            return totalCount;

        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    public List<UserAccessLogoPojo> findAllHavingMoreThanHundredWithDateRange(Date startDate, Date endDate, long range) throws IllegalAccessException {

        if(!connection.isPresent()) {
            throw new IllegalAccessException("No connection found");
        }

        List<UserAccessLogoPojo>  userAccessLogList = new ArrayList<>();

        String sql = "select ual.user_agent as userAgent, ual.status as status, ual.ip as ip, count(ual) from user_access_log ual where ual.date BETWEEN '"+
                MyUtils.dateToString(startDate, "yyyy-MM-dd HH:mm:ss")
                +"' AND '"
                +MyUtils.dateToString(endDate, "yyyy-MM-dd HH:mm:ss")+
                "' group by ual.ip, ual.user_agent, ual.status having count(ual) > "+ range;

        try (Statement statement = connection.get().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            int count = 0;
            while (resultSet.next()) {
                UserAccessLogoPojo userAccessLog = new UserAccessLogoPojo();
                userAccessLog.setUserAgent(resultSet.getString(1));
                userAccessLog.setStatus(resultSet.getString(2));
                userAccessLog.setIp(resultSet.getString(3));
                userAccessLog.setCount((long) resultSet.getInt(4));
                userAccessLogList.add(userAccessLog);
                logger.info(new Gson().toJson(userAccessLog));
                count++;
            }

            return userAccessLogList;

        } catch (SQLException ex) {
            logger.info(ex.getMessage());
            throw new Error(ex);
        }

    }

    public void bulkSave(List<UserAccessLog> userAccessLogList) throws IllegalAccessException {

        if(!connection.isPresent()) {
            throw new IllegalAccessException("No connection found");
        }

        if(userAccessLogList.size() <= 0) {
            logger.info("Null not allowed!");
        }

        String sql = "INSERT INTO user_access_log(USER_AGENT, STATUS, IP, DATE) VALUES(?, ?, ?, ?)";

        try {

            Connection conn = connection.get();

            conn.setAutoCommit(false);

            String message = "Can not be null";
            for(UserAccessLog userAccessLog: userAccessLogList) {
                PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                UserAccessLog nonNullUserAccessLog = Objects.requireNonNull(userAccessLog, message);
                preparedStatement.setString(1, nonNullUserAccessLog.getUserAgent());
                preparedStatement.setString(2, nonNullUserAccessLog.getStatus());
                preparedStatement.setString(3, nonNullUserAccessLog.getIp());

                // logger.info(MyUtils.dateToString(date, "yyyy-MM-dd HH:mm:ss"));
                Timestamp ts = new Timestamp(nonNullUserAccessLog.getDate().getTime());

                preparedStatement.setTimestamp(4, ts);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                logger.info("affectedRows: " +affectedRows);

                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        nonNullUserAccessLog.setId(resultSet.getLong(1));
                        long id = resultSet.getLong(1);
                        System.out.println("Inserted ID : " + id);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            conn.commit();

            conn.setAutoCommit(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
