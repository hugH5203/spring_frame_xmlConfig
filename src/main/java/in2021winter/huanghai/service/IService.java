package in2021winter.huanghai.service;

import in2021winter.huanghai.domain.User;

import java.util.List;

/**
 * @author HuangHai
 * @date 2021/2/6 12:46
 */
public interface IService {
    /**
     * 查询所有
     * @return
     */
    List<User> findAll();

    /**
     * 保存操作
     */
    void saveUser(User user);


    /**
     * 修改用户信息
     * @param user
     */
    void updateUser(User user);

    /**
     * 删除用户
     */
    void deleteUser(Integer id);

    /**
     * 根据id查找用户
     * @param id
     * @return
     */
    User findById(Integer id);

    /**
     * 模糊查询
     * @param username
     * @return
     */
    List<User> findByName(String username);


    /**
     * 查询用户数量
     * @return
     */
    int findTotal();

    /**
     * 关于事务的方法，转账问题(因为我的表中没有money的列明，就用性别代替)
     * @param sourceName
     * @param targetName
     * @param sex
     */
    void transfer(String sourceName,String targetName, String sex);
}
