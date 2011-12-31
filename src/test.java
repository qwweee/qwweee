import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

import Config.Config;
import DB.DBOP;
import DB.DatabaseFactory;

import com.twmacinta.util.MD5;
public class test {
 
    public static void main(String[] args) throws UnknownHostException, SQLException {
        //DBinit();
        //DBTest();
        MD5();
    }
    private static void DBTest() {
        DBOP.insertMD5Table("11", Config.HOST, "11", "11", 11);
        DBOP.insertSameTable("11", Config.HOST, "11", "11", 11);
        DBOP.insertEmptyTable(Config.HOST, "11");
        DBOP.insertEmptyFile(Config.HOST, "11", "11");
    }
    private static void DBinit() throws SQLException {
        DatabaseFactory.setDatabaseSettings(Config.DBDRIVER, Config.DBURL, Config.DBUSER, Config.DBPASSWD, Config.DBCONN);
        DatabaseFactory.getInstance();
        DBOP.createTable("md5table");
        DBOP.createTable("sametable");
        DBOP.createTable("emptytable");
        DBOP.createTable("emptyfile");
    }
    private static void MD5() throws UnknownHostException {
        System.out.println(java.net.InetAddress.getLocalHost().getHostName());
        String filename = "D:/eMule/Incoming/亞洲大學-叫你刪你不刪-學生-性,無碼,做愛,偷拍,自慰,淫賤,自拍,台灣.3gp";
        long start = System.currentTimeMillis();
        System.out.println(DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis())));
        File tmp = new File(filename); 
        try {
            String hash = MD5.asHex(MD5.getHash(tmp));
            System.out.println(hash+"\t"+hash.length());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("fast-md5:"+(System.currentTimeMillis()-start));
        System.out.println(tmp.length());
        start = System.currentTimeMillis();
        System.out.println(DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis())));
        FileInputStream fis;
        byte[] digest = null;
        try {
            fis = new FileInputStream(tmp);
            byte[] data = new byte[Main.BYTE_LENGTH];
            MessageDigest md = MessageDigest.getInstance(Main.DECODE);
            while (fis.read(data) != -1) {
                md.update(data);
            }
            fis.close();
            digest = md.digest();
            BigInteger bigint = new BigInteger(1, digest);
            System.out.println(bigint.toString(16)+"\t"+bigint.toString(16).length());
            System.out.println(bigint.toString(16)+"\t"+bigint.toString(16).length());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("     md5:"+(System.currentTimeMillis()-start));
        System.out.println(DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis())));
    }
    //
    @SuppressWarnings("unused")
    private static String toMd5(String source_string){
        String md5String=null;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source_string.getBytes());
            byte[] digest = md.digest();
            md5String = toHex(digest);
        }catch(Exception e)
        {e.printStackTrace();}
        return md5String;
    }
    //
    private static String toHex(byte[] digest){
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < digest.length; ++i){
                byte b = digest[i];
                int value = (b & 0x7F) + (b < 0 ? 128 : 0);
                buffer.append(value < 16 ? "0" : "");
                buffer.append(Integer.toHexString(value));
            }
        return buffer.toString();
    }
}
