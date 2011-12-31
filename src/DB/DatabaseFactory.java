package DB;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DatabaseFactory {
    private ComboPooledDataSource DBsource;
    private static DatabaseFactory Instance;
    private static String DBdriver;
    private static String DBurl;
    private static String DBuser;
    private static String DBpassword;
    private static int DBmaxCon;
    public DatabaseFactory() throws SQLException {
        try {
            DBsource = new ComboPooledDataSource();
            DBsource.setAutoCommitOnClose(true);
            DBsource.setInitialPoolSize(10);
            DBsource.setMinPoolSize(10);
            DBsource.setMaxPoolSize(DBmaxCon);
            DBsource.setAcquireRetryAttempts(0); // try to obtain connections
            DBsource.setAcquireRetryDelay(500); // 500 miliseconds wait before
            DBsource.setCheckoutTimeout(500); // 0 = wait indefinitely for new
            DBsource.setAcquireIncrement(5); // if pool is exhausted, get 5
            //DBsource.setAutomaticTestTable("connection_test_table");
            DBsource.setTestConnectionOnCheckin(false);
            DBsource.setIdleConnectionTestPeriod(60); // test idle connection
            DBsource.setMaxIdleTime(100); // 0 = idle connections never expire
            DBsource.setMaxStatementsPerConnection(100);
            DBsource.setBreakAfterAcquireFailure(false); // never fail if any
            DBsource.setDriverClass(DBdriver);
            DBsource.setJdbcUrl(DBurl);
            DBsource.setUser(DBuser);
            DBsource.setPassword(DBpassword);
            /* Test the connection */
            DBsource.getConnection().close();
        } catch (SQLException x) {
            System.out.println("Database Connection FAILED");
            // rethrow the exception
            throw x;
        } catch (Exception e) {
            System.out.println("Database Connection FAILED");
            throw new SQLException("could not init DB connection:" + e);
        }
    }

    public static void setDatabaseSettings(String driver, String url,
            String user, String password, int maxCon) {
        DBdriver = driver;
        DBurl = url;
        DBuser = user;
        DBpassword = password;
        DBmaxCon = maxCon;
    }

    public void shutdown() {
        try {
            DBsource.close();
        } catch (Exception e) {
        }
        try {
            DBsource = null;
        } catch (Exception e) {
        }
    }

    public static DatabaseFactory getInstance() throws SQLException {
        if (Instance == null) {
            Instance = new DatabaseFactory();
        }
        return Instance;
    }

    public Connection getConnection() throws SQLException {
        Connection con = null;
        while (con == null) {
            try {
                con = DBsource.getConnection();
                con.setAutoCommit(false);
            } catch (SQLException e) {
                System.out.println("DB Fault!");
            }
        }
        return con;
    }
}
