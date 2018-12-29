package cumt.innovative.training.project.basketballappointment.utils;

import cumt.innovative.training.project.basketballappointment.config.ConfigurationModel;

import java.sql.*;

public class DatabaseUtil {
    /* Table designs, see https://paste.ubuntu.com/p/XbRNgMrZFm/ */
    private final static String DATABASE_NAME = "jdbc:sqlite:" + ConfigurationModel.getInstance().databaseName;
    private final static String JDBC_CLASS_NAME = "org.sqlite.JDBC";

    private static Connection c = null;
    private static Statement stmt = null;

    static {
        try {
            Class.forName(JDBC_CLASS_NAME);
            c = DriverManager.getConnection(DATABASE_NAME);
            c.setAutoCommit(true);
            stmt = c.createStatement();
        } catch (Exception ex) {
            throw new RuntimeException(ExceptionUtil.getSimpleMessage(ex));
        }
    }

    public static ResultSet executeQuery(String sqlCommand) {
        try {
            return stmt.executeQuery(sqlCommand);
        } catch (SQLException e) {
            throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
        }
    }

    public static int executeUpdate(String sqlCommand) {
        try {
            return stmt.executeUpdate(sqlCommand);
        } catch (SQLException e) {
            throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
        }
    }

    public static void close() {
        try {
            stmt.close();
            c.close();
        } catch (SQLException e) {
            throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
        }
    }
}
