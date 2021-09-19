package in2021winter.huanghai.dao;

import in2021winter.huanghai.domain.User;

import java.sql.SQLException;
import java.util.List;

/**
 * @author HuangHai
 * @date 2021/2/6 11:04
 */
public interface IUserDao {
    /**
     * 查询所有
     * @return
     */
    List<User> findAll() throws SQLException;

    /**
     * 保存操作
     */
    void saveUser(User user) throws SQLException;


    /**
     * 修改用户信息
     * @param user
     */
    void updateUser(User user) throws SQLException;

    /**
     * 删除用户
     */
    void deleteUser(Integer id) throws SQLException;

    /**
     * 根据id查找用户
     * @param id
     * @return
     */
    User findById(Integer id) throws SQLException;

    /**
     * 模糊查询
     * @param username
     * @return
     */
    List<User> findByName(String username) throws SQLException;


    /**
     * 查询用户数量
     * @return
     */
    int findTotal() throws SQLException;

}
