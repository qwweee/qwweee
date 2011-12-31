import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;

import utils.Queue;

import Config.Config;
import DB.DBOP;
import DB.DatabaseFactory;


public class Main{
    public static String DECODE = "MD5";
    public static int BYTE_LENGTH = 10240;
    public static Queue queue;
    private ArrayList<String> dirArr;
    private String host;
    private int emptyfile;
    private int emptydir;
    private int samefile;
    private long allsize;
    private String dirname;
    public Main() throws IOException, NoSuchAlgorithmException {
        emptyfile = 0;
        emptydir = 0;
        samefile = 0;
        allsize = 0;
        this.host = Config.HOST;
        dirArr = new ArrayList<String>();
        File[] array = File.listRoots();
        for (int i  = 0 ; i < array.length ; i ++ ) {
            if (array[i].getCanonicalPath().equalsIgnoreCase("c:\\")) {
                continue;
            }
            ReadDirectory(array[i]);
        }
        System.out.println("所有檔案數量有："+NumberFormat.getIntegerInstance().format(dirArr.size())+"個檔案。");
        System.out.println(String.format("%8s : %,5d 個", "空資料夾", emptydir));
        System.out.println(String.format("%8s : %,5d 個", "空檔案", emptyfile));
        System.out.println(String.format("%8s : %,5d 個", "相同檔案", samefile));
        System.out.println(String.format("%8s : %,d MB", "總共檔案大小", allsize/1000000));
    }
    public Main(String dirname) throws IOException, NoSuchAlgorithmException {
        emptyfile = 0;
        emptydir = 0;
        samefile = 0;
        this.dirname = dirname;
        this.host = Config.HOST;
        dirArr = new ArrayList<String>();
        ReadDirectory(new File(this.dirname));
        //queue.enQueue(this);
    }
    public void run() {
        for (int i = 0 ; i < dirArr.size() ; i ++) {
            String md5String = "";
            File tmp = new File(dirArr.get(i));
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
            md5String = toHex(digest);
            if (!DBOP.checkMD5Table(md5String, tmp)) {
                if (tmp.length() == 0) {
                    if (tmp.isDirectory()) { //空資料夾
                        DBOP.insertEmptyTable(host, tmp.getAbsolutePath());
                    } else { //無資料的檔案
                        DBOP.insertEmptyFile(host, tmp.getAbsolutePath(), tmp.getName());
                        emptyfile++;
                    }
                } else {
                    //文件
                    DBOP.insertMD5Table(md5String, host, tmp.getName(), tmp.getParent(), tmp.length());
                    allsize += tmp.length();
                }
            } else {
                //相同的檔案
                DBOP.insertSameTable(md5String, host, tmp.getName(), tmp.getParent(), tmp.length());
                samefile++;
                allsize += tmp.length();
            }
        }
        System.out.println(dirname+" 檔案數量有："+NumberFormat.getIntegerInstance().format(dirArr.size())+"個檔案。");
        System.out.println(String.format("%8s : %,5d 個", "空資料夾", emptydir));
        System.out.println(String.format("%8s : %,5d 個", "空檔案", emptyfile));
        System.out.println(String.format("%8s : %,5d 個", "相同檔案", samefile));
        System.out.println(String.format("%8s : %,d MB", "總共檔案大小", allsize/1000000));
    }
    private String toHex(byte[] digest){
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < digest.length; ++i){
                byte b = digest[i];
                int value = (b & 0x7F) + (b < 0 ? 128 : 0);
                buffer.append(value < 16 ? "0" : "");
                buffer.append(Integer.toHexString(value));
            }
        return buffer.toString();
    }
    private void ReadDirectory(File dir) throws IOException {
        if (dir.isDirectory()) {
            File[] filelist = dir.listFiles();
            if (filelist == null) {
            } else if (filelist.length > 0) {
                for (int i = 0 ; i < filelist.length ; i ++) {
                    ReadDirectory(filelist[i]);
                }
            } else {
                //空資料夾
                DBOP.insertEmptyTable(host, dir.getAbsolutePath());
                emptydir++;
            }
        } else {
            try {
                dirArr.add(dir.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressWarnings("unused")
    private void printList() {
        for (int i = 0 ; i < dirArr.size() ; i ++) {
            System.out.println(dirArr.get(i));
        }
    }
    public static void main (String[] arg) throws SQLException, IOException, NoSuchAlgorithmException {
        DBinit();
        init();
        ProcessQueue[] process = new ProcessQueue[Config.QUEUESIZE];
        for (ProcessQueue tmp : process) {
            tmp = new ProcessQueue(Main.queue);
            tmp.start();
        }
        //new Main("H:/機密整理區").run();
        new Main("H:/機密整理區").run();
        
    }
    private static void init() {
        queue = new Queue(Config.QUEUESIZE);
    }
    private static void DBinit() throws SQLException {
        DatabaseFactory.setDatabaseSettings(Config.DBDRIVER, Config.DBURL, Config.DBUSER, Config.DBPASSWD, Config.DBCONN);
        DatabaseFactory.getInstance();
        System.out.println("MD5Table資料表"+(DBOP.createTable("md5table")?"建立完成":"存在"));
        System.out.println("SameTable資料表"+(DBOP.createTable("sametable")?"建立完成":"存在"));
        System.out.println("EmptyTable資料表"+(DBOP.createTable("emptytable")?"建立完成":"存在"));
        System.out.println("EmptyFile資料表"+(DBOP.createTable("emptyfile")?"建立完成":"存在"));
    }
}
class ProcessQueue extends Thread{
    private Queue queue;
    public ProcessQueue(Queue queue) {
        this.queue = queue;
    }
    public void run() {
        while (queue.getSize() != 0) {
            Main task = (Main)queue.deQueue();
            task.run();
        }
    }
}