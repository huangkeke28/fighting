package util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    //JDBC驱动配置
    //创建数据库的工具类
    //创建连接对象
    //建立连接
    //关闭连接
    //volatile 防止jvm指令重排序
    public static volatile DataSource DS;
    public static final String URL = "jdbc:mysql://localhost:3306/stu_dorm";
    public static final String USER = "root";
    public static final String PASSWORD = "123456";

    private static DataSource getDS() {
        //采用双重校验锁的单例模式创建
        //提高效率，单例模式只需要创建一次即可，后序代码就没必要竞争锁
        if (DS == null) {
            //对类加锁
            synchronized(DBUtil.class) {
                //防止多线程多次创建实例
                if (DS == null) {
                    DS = new MysqlDataSource();
                    //MysqlDataSource 只是DataSource的一种 所以设计类型强转
                    ((MysqlDataSource)DS).setURL(URL);
                    ((MysqlDataSource)DS).setUser(USER);
                    ((MysqlDataSource)DS).setPassword(PASSWORD);
                }
            }
        }
        return DS;
    }

    //获取数据库连接对象
    public static Connection getConnction() {
        try {
            return getDS().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("获取数据库连接失败", e);
        }
    }
    //关闭数据库连接
    //反向关闭
    public static void close(Connection c, Statement s, ResultSet r) {
        try {
            if (r != null) {
                r.close();
            }
            if (s != null) {
                s.close();
            }
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("释放数据库资源失败", e);
        }
    }

    public static void close(Connection c, Statement s) {
        close(c,s,null);
    }
}
