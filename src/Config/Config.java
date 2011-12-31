package Config;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class Config {
    public static String HOST;
    public static String DBUSER;
    public static String DBPASSWD;
    public static String DBDRIVER;
    public static String DBURL;
    public static int DBCONN;
    public static int QUEUESIZE;
    
    static {
        try {
            HOST = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        DBUSER = "qwweee";
        DBPASSWD = "ji394su3";
        DBDRIVER = "com.mysql.jdbc.Driver";
        DBURL = "jdbc:mysql://163.22.32.101/md5?useUnicode=true&characterEncoding=utf8";
        DBCONN = 100;
        QUEUESIZE = 5;
    }
}
