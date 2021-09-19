package in2021winter.huanghai.serviceImpl;

import in2021winter.huanghai.dao.IUserDao;
import in2021winter.huanghai.domain.User;
import in2021winter.huanghai.service.IService;

import java.sql.SQLException;
import java.util.List;

/**
 * @author HuangHai
 * @date 2021/2/6 12:47
 */
public class UserService implements IService {

    private IUserDao userDao;

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> findAll() {
        try {
            return userDao.findAll();
        } catch (SQLException e) {
            System.out.println("service失败");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(User user) {
        try {
            userDao.saveUser(user);
        } catch (SQLException e) {
            System.out.println("service失败");
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            userDao.updateUser(user);
        } catch (SQLException e) {
            System.out.println("service失败");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(Integer id) {
        try {
            userDao.deleteUser(id);
        } catch (SQLException e) {
            System.out.println("service失败");
            e.printStackTrace();
        }
    }

    @Override
    public User findById(Integer id) {
        try {
            return userDao.findById(id);
        } catch (SQLException e) {
            System.out.println("service失败");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findByName(String username) {
        try {
            return userDao.findByName(username);
        } catch (SQLException e) {
            System.out.println("service失败");
            throw new RuntimeException(e);
        }
    }

    @Override
    public int findTotal() {
        try {
            return userDao.findTotal();
        } catch (SQLException e) {
            System.out.println("service失败");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void transfer(String sourceName, String targetName, String sex) {
        try {
            //1，根据名字查询转出账户
            User source = userDao.findByName(sourceName).get(0);
            //2.根据名字查询转入账户
            User target = userDao.findByName(targetName).get(0);
            //3.转入账户减钱(修改性别)
            source.setSex("ta"+sex);
            //4.转出账户加钱（修改性别）
            target.setSex("ta2"+sex);
            //5.转入账户更新数据
            userDao.updateUser(source);
            //异常测试
            int s=100/0;
            //6.转出账户更新数据
            userDao.updateUser(target);
        } catch (SQLException e) {
            System.out.println("事务测试失败");
            e.printStackTrace();
        }
    }
}
