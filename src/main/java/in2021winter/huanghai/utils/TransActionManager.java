package in2021winter.huanghai.utils;

import java.sql.SQLException;

/**
 * @author HuangHai
 * @date 2021/2/6 16:34
 */
public class TransActionManager {
    private ConnectionUtils connectionUtils;

    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }
    /**
     * 开启事务
     */
    public void beginTransAction(){
        try {
            connectionUtils.getThreadConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("事务开启失败");
        }
    }

    /**
     * 提交事务
     */
    public void commit(){
        try {
            connectionUtils.getThreadConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("事务提交失败");
        }
    }


    /**
     *回滚事务
     */
    public void rollBack(){
        try {
            connectionUtils.getThreadConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("事务回滚失败");
        }
    }

    /**
     * 还回连接池中
     */
    public void release(){
        try {
            connectionUtils.getThreadConnection().close();  //还回连接池中
            connectionUtils.remove();   //在threadLocal中去除这个变量
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("连接释放失败");
        }
    }
}
