package com.revent.test.dao;

import com.revent.test.db.JdbcConnection;
import com.revent.test.entity.UserAccessLog;

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

//
//    public List<UserAccessLog> findAllHavingMoreThanHundredWithDateRange(Date startDate, Date endDate, int range) {
//
//        List<UserAccessLog>  userAccessLogList = new ArrayList<>();
//
//        String sql = "select ual, count(ual) as totalCount from user_access_log ual where ual.date BETWEEN "+startDate+" AND "+endDate+" group by ual.ip, ual.user_agent, ual.status having count(ual) > "+ range+";";
//
//
//        try (Statement statement = connection.get().createStatement();
//             ResultSet resultSet = statement.executeQuery(sql)) {
//
//            while (resultSet.next()) {
//                Long totalCount = resultSet.getLong("totalCount");
//                UserAccessLog userAccessLog = resultSet.getObject(0, UserAccessLog.class);
//                userAccessLogList.add(userAccessLog);
//            }
//
//        } catch (SQLException ex) {
//            logger.info(ex.getMessage());
//        }
//
//    }
//
//
//    public Long getTotalCount() {
//
//        return connection.flatMap(conn -> {
//            Long count = 0L;
//
//            String sql = "";
//
//            try (Statement statement = conn.createStatement();
//                 ResultSet resultSet = statement.executeQuery(sql)) {
//
//                if (resultSet.next()) {
//                    count = (Long) 0L;
//                }
//            } catch (SQLException ex) {
//                logger.info(ex.getMessage());
//            }
//
//            return count;
//        });
//
//    }
//
//    @Override
//    public Optional<Long> save(UserAccessLog userAccessLog) {
//
//        if(userAccessLog == null) {
//            logger.info("Null not allowed!");
//            return Optional.empty();
//        }
//
//        String message = "";
//
//        UserAccessLog nonNullUserAccessLog = Objects.requireNonNull(userAccessLog, message);
//        String sql = "INSERT INTO user_access_log(ID, USER_AGENT, STATUS, IP, DATE) VALUES(?, ?, ?, ?, ?)";
//
//        Optional<Long> generatedId = Optional.empty();
//
//        try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//
//            statement.setLong(1, nonNullUserAccessLog.getId());
//            statement.setString(2, nonNullUserAccessLog.getUserAgent());
//            statement.setString(3, nonNullUserAccessLog.getStatus());
//            statement.setString(4, nonNullUserAccessLog.getIp());
//            statement.setDate(5, (java.sql.Date) nonNullUserAccessLog.getDate());
//
//            int numberOfInsertedRows = statement.executeUpdate();
//
//            if (numberOfInsertedRows > 0) {
//                try (ResultSet resultSet = statement.getGeneratedKeys()) {
//                    if (resultSet.next()) {
//                        generatedId = Optional.of(resultSet.getLong(1));
//                    }
//                }
//            }
//        }
//        catch (SQLException ex) {
//            this.logger.info(ex.getMessage());
//        }
//
//        return generatedId;
//
//    }


    public void bulkSave(List<UserAccessLog> userAccessLogList) throws IllegalAccessException {
        if(!connection.isPresent()) {
            throw new IllegalAccessException("No connection found");
        }

        if(userAccessLogList.size() <= 0) {
            logger.info("Null not allowed!");
        }

        String sql = "INSERT INTO user_access_log(ID, USER_AGENT, STATUS, IP, DATE) VALUES(?, ?, ?, ?, ?)";

        try {

            Connection conn = connection.get();

           // Statement statement = conn.prepareStatement(sql);
            //statement.execute(sql);

            conn.setAutoCommit(false);

            String message = "Can not be null";
            for(UserAccessLog userAccessLog: userAccessLogList) {
                PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                UserAccessLog nonNullUserAccessLog = Objects.requireNonNull(userAccessLog, message);

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        nonNullUserAccessLog.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }

                preparedStatement.setLong(1, nonNullUserAccessLog.getId());
                preparedStatement.setString(2, nonNullUserAccessLog.getUserAgent());
                preparedStatement.setString(3, nonNullUserAccessLog.getStatus());
                preparedStatement.setString(4, nonNullUserAccessLog.getIp());
                preparedStatement.setDate(5, new java.sql.Date(nonNullUserAccessLog.getDate().getTime()));
                preparedStatement.execute();
            }

            conn.commit();

            conn.setAutoCommit(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @Override
//    public void update(UserAccessLog userAccessLog) {
//
//    }
//
//    @Override
//    public void delete(UserAccessLog userAccessLog) {
//
//    }
//
//    @Override
//    public Optional get(int id) {
//        return Optional.empty();
//    }
//
//    @Override
//    public Collection getAll() {
//        return null;
//    }
}
