/**
 * 
 */
package Result;

import java.sql.SQLException;

import DB.DatabaseFactory;

/**
 * @author bbxp
 *
 */
public class ShowResult {
    /**
     * 建構元
     */
    public ShowResult() {
        
    }
    /**
     * main function
     * @param arg
     * @throws SQLException 
     */
    public static void main(String[] arg) throws SQLException {
        DatabaseFactory.setDatabaseSettings("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/md5?useUnicode=true&characterEncoding=utf8", "root", "ji394su3", 30);
        DatabaseFactory.getInstance();
        
    }
}
