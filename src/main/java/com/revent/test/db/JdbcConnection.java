package com.revent.test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;

public class JdbcConnection {

        private static final Logger logger = Logger.getLogger(JdbcConnection.class.getName());
        private static Optional<Connection> connection = Optional.empty();

        public static Optional<Connection> getConnection() {
            if (!connection.isPresent()) {
                String url = "jdbc:postgresql://localhost:5432/req_limit";
                String user = "airs_user";
                String password = "airs2022!wwswdwdw";
                try {
                    connection = Optional.ofNullable(
                            DriverManager.getConnection(url, user, password));
                } catch (SQLException ex) {
                    logger.info(ex.getMessage());
                }
            }

            return connection;
        }
    }