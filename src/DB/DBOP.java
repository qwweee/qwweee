/**
 * 資料庫存取function
 */
package DB;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Config.Config;

/**
 * @author bbxp
 * 資料庫的操作function
 */
public class DBOP {
    /**
     * 建立md5table資料表的sql指令
     */
    private static final String C_MD5TABLE = "CREATE TABLE `md5table` (  `fid` int(10) NOT NULL AUTO_INCREMENT,  `md5` varchar(32) NOT NULL,  `hostname` text NOT NULL,  `name` text NOT NULL,  `path` text NOT NULL,  `size` bigint(15) NOT NULL,  PRIMARY KEY (`fid`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
    /**
     * 建立sametable資料表的sql指令
     */
    private static final String C_SAMETABLE = "CREATE TABLE `sametable` (  `id` int(10) NOT NULL AUTO_INCREMENT,  `md5` varchar(32) NOT NULL,  `hostname` text NOT NULL,  `name` text NOT NULL,  `path` text NOT NULL,  `size` bigint(15) NOT NULL,  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
    /**
     * 建立emptytable資料表的sql指令
     */
    private static final String C_EMPTYFILE = "CREATE TABLE `emptyfile` (  `id` int(10) NOT NULL AUTO_INCREMENT,  `hostname` text NOT NULL,  `name` text NOT NULL,  `path` text NOT NULL,  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
    /**
     * 建立emptyfile資料表的sql指令
     */
    private static final String C_EMPTYTABLE = "CREATE TABLE `emptytable` (  `id` int(10) NOT NULL AUTO_INCREMENT,  `hostname` text NOT NULL,  `path` text NOT NULL,  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
    /**
     * 建構元 
     */
    private DBOP() {
    }
    /**
     * 判斷table是否存在
     * @param tableName 資料表名稱
     * @return boolean true代表存在，false代表不存在
     * @throws SQLException
     */
    public static boolean isDBTable(String tableName) throws SQLException {
        DatabaseMetaData dbmd = DatabaseFactory.getInstance().getConnection().getMetaData();
        ResultSet resultSet = dbmd.getTables(null, null, tableName, null);
        if (resultSet.next()) {
            return true;
        }
        return false;
    }
    /**
     * 建立table
     * @param tableName 資料表名稱
     * @return boolean true代表建立，false代表沒有建立
     * @throws SQLException
     */
    public static boolean createTable(String tableName) throws SQLException {
        if (isDBTable(tableName)) {
            return false; 
        }
        boolean noerror = true;
        String sql = "";
        Connection con = null;
        PreparedStatement pstm = null;
        if (tableName.equalsIgnoreCase("md5table")) {
            sql = DBOP.C_MD5TABLE;
        } else if (tableName.equalsIgnoreCase("sametable")) {
            sql = DBOP.C_SAMETABLE;
        } else if (tableName.equalsIgnoreCase("emptytable")){
            sql = DBOP.C_EMPTYTABLE;
        } else if (tableName.equalsIgnoreCase("emptyfile")) {
            sql = DBOP.C_EMPTYFILE;
        } else {
            return false;
        }
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(sql);
            pstm.execute();
            pstm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            noerror = false;
        } finally {
            if (null != pstm) {
                try {
                    pstm.close();
                } catch (SQLException ignore) {
                    System.out.println("close DB preparestatement error!");
                    // ignore
                }
                pstm = null;
            }
            if (null != con) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                    System.out.println("close DB connection error!");
                    // ignore
                }
                con = null;
            }
        }
        return noerror;
    }
    /**
     * 新增一筆資料到md5table
     * @param md5 md5資料
     * @param host 主機名稱
     * @param name 檔案名稱
     * @param path 路徑
     * @return boolean true代表新增成功，false代表新增失敗
     */
    public static boolean insertMD5Table(String md5, String host, String name, String path, long size) {
        boolean havechange = false;
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("INSERT INTO `md5table` (`fid`,`md5`,`hostname`,`name`,`path`,`size`) VALUES (NULL,?,?,?,?,?)");
            pstm.setString(1, md5);
            pstm.setString(2, host);
            pstm.setString(3, name);
            pstm.setString(4, path);
            pstm.setLong(5, size);
            pstm.execute();
            pstm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            havechange = false;
        } finally {
            if (null != pstm) {
                try {
                    pstm.close();
                } catch (SQLException ignore) {
                    System.out.println("close DB preparestatement error!");
                    // ignore
                }
                pstm = null;
            }
            if (null != con) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                    System.out.println("close DB connection error!");
                    // ignore
                }
                con = null;
            }
        }
        return havechange;
    }
    /**
     * 如果有相同md5，則新增一筆資料至sameTable
     * @param md5 md5資料
     * @param host 主機名稱
     * @param name 檔案名稱
     * @param path 路徑
     * @return boolean true代表新增成功，false代表新增失敗
     */
    public static boolean insertSameTable(String md5, String host, String name, String path, long size) {
        boolean havechange = false;
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("INSERT INTO `sametable` (`id`,`md5`,`hostname`,`name`,`path`,`size`) VALUES (NULL,?,?,?,?,?)");
            pstm.setString(1, md5);
            pstm.setString(2, host);
            pstm.setString(3, name);
            pstm.setString(4, path);
            pstm.setLong(5, size);
            pstm.execute();
            pstm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            havechange = false;
        } finally {
            if (null != pstm) {
                try {
                    pstm.close();
                } catch (SQLException ignore) {
                    System.out.println("close DB preparestatement error!");
                    // ignore
                }
                pstm = null;
            }
            if (null != con) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                    System.out.println("close DB connection error!");
                    // ignore
                }
                con = null;
            }
        }
        return havechange;
    }
    /**
     * 如果為空的資料夾，則新增一筆資料至emptyTable
     * @param host 主機名稱
     * @param path 路徑
     * @return boolean true代表新增成功，false代表新增失敗
     */
    public static boolean insertEmptyTable(String host, String path) {
        boolean havechange = false;
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("INSERT INTO `emptytable` (`id`,`hostname`,`path`) VALUES (NULL,?,?)");
            pstm.setString(1, host);
            pstm.setString(2, path);
            pstm.execute();
            pstm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            havechange = false;
        } finally {
            if (null != pstm) {
                try {
                    pstm.close();
                } catch (SQLException ignore) {
                    System.out.println("close DB preparestatement error!");
                    // ignore
                }
                pstm = null;
            }
            if (null != con) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                    System.out.println("close DB connection error!");
                    // ignore
                }
                con = null;
            }
        }
        return havechange;
    }
    /**
     * 如果為空的檔案，則新增一筆資料至emptyFile
     * @param host 主機名稱
     * @param name 資料夾名稱
     * @param path 路徑
     * @return boolean true代表新增成功，false代表新增失敗
     */
    public static boolean insertEmptyFile(String host, String path, String name) {
        boolean havechange = false;
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("INSERT INTO `emptyfile` (`id`,`hostname`,`name`,`path`) VALUES (NULL,?,?,?)");
            pstm.setString(1, host);
            pstm.setString(2, name);
            pstm.setString(3, path);
            pstm.execute();
            pstm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            havechange = false;
        } finally {
            if (null != pstm) {
                try {
                    pstm.close();
                } catch (SQLException ignore) {
                    System.out.println("close DB preparestatement error!");
                    // ignore
                }
                pstm = null;
            }
            if (null != con) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                    System.out.println("close DB connection error!");
                    // ignore
                }
                con = null;
            }
        }
        return havechange;
    }
    /**
     * 檢查是否有相同的md5
     * @param md5 檢測的md5資料
     * @return boolean true代表有資料，false代表沒有資料
     */
    public static boolean checkMD5Table(String md5, File file) {
        boolean havedata = false;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `md5table` WHERE `md5table`.`md5` = ?");
            pstm.setString(1, md5);
            rs = pstm.executeQuery();
            if (rs.next()) {
                if (rs.getString(3).equalsIgnoreCase(Config.HOST))
                havedata = true;
            } else {
                havedata = false;
            }
            rs.close();
            pstm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            havedata = false;
        } finally {
            if (null != rs) {
                try {
                    rs.close();
                } catch (SQLException ignore) {
                    System.out.println("close DB resultset error!");
                    // ignore
                }
                rs = null;
            }
            if (null != pstm) {
                try {
                    pstm.close();
                } catch (SQLException ignore) {
                    System.out.println("close DB preparestatement error!");
                    // ignore
                }
                pstm = null;
            }
            if (null != con) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                    System.out.println("close DB connection error!");
                    // ignore
                }
                con = null;
            }
        }
        return havedata;
    }
}
