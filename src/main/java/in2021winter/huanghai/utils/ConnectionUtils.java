package in2021winter.huanghai.utils;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author HuangHai
 * @date 2021/2/6 16:10
 */
public class ConnectionUtils {
   /* ThreadLocal为线程局部变量，即每一个访问的线程拥有的，独立的，不会被其他线程干扰的变量，原理是一个map的数据结构，
     key为自动命名的线程id，值便是set进去的那个对象，只能存一个，用完要记得remove掉，因为值为一个强引用，gc永不回收。
     常用于多线程或单例对象，此处因为需要确认只用一条connection，来确保事务的安全性*/
    private ThreadLocal<Connection> threadLocal= new ThreadLocal<>();

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取唯一的连接，确保事务的安全性
     * @return
     */
    public Connection getThreadConnection(){
        //1.先从threadlocal里面获取，如果有就获取，没有就创建
        try {
            Connection connection = threadLocal.get();
            if (connection==null){               //如果没有就创建,并且存入threadLocal中
                connection=dataSource.getConnection();
                threadLocal.set(connection);
            }
            return connection;
        } catch (Exception e) {
            System.out.println("threadLocal中获取connection失败");
            throw new RuntimeException(e);
        }
    }


    /**
     * 在threadlocal中去除这个变量，防止强引用占内存
     */
    public void remove(){
        threadLocal.remove();
    }
}
