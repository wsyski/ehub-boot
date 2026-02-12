package com.axiell.ehub.local.it;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@Transactional
public abstract class AbstractEhubRepositoryIT<D extends DevelopmentData> {
    private static final String SQL = "TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK";
    protected D developmentData;
    @Autowired
    private DataSource dataSource;

    @BeforeTransaction
    public void beforeTransaction() throws Exception {
        log.info("beforeTransaction");
        developmentData = initDevelopmentData();
        developmentData.init();
    }

    protected abstract D initDevelopmentData();

    @AfterTransaction
    public void afterTransaction() throws Exception {
        log.info("afterTransaction");
        destroy();
        developmentData = null;
    }

    public void destroy() throws Exception {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(true);
            try {
                executeDDLStatement(connection, SQL);
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            }
        } finally {
            closeConnection(connection);
        }
    }

    private void executeDDLStatement(final Connection connection, final String statement) throws SQLException {
        Statement stmt = connection.createStatement();

        try {
            stmt.execute(statement);
            connection.commit();
        } finally {
            stmt.close();
        }
    }

    private void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
