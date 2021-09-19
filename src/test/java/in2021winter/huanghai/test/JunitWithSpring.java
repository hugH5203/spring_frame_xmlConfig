package in2021winter.huanghai.test;

import in2021winter.huanghai.domain.User;
import in2021winter.huanghai.service.IService;
import in2021winter.huanghai.serviceImpl.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**  测试spring与Junit的整合
 * @author HuangHai
 * @date 2021/2/8 22:28
 *
 *   由于每次测试spring的方法都要获取spring容器与容器中的对象，重复代码多，
 *   虽然可以将它们写入Junit的@Before与@After注解中，但是可能测试人员不了解spring怎么获取容器，就导致测试不了，
 *   spring虽然有IOC操作，可以注入对象，但是由于Junit里面自己集成了一个main方法，会解析有没有注解，有就直接执行该方法，
 *   就导致了spring注入的对象根本没有用到。
 *   为了解决这个问题，spring特意提供了一个注解，将它（指Junit的运行器：runner）替换成spring提供的运行器，首先需要导入spring-test的jar坐标。
 *   @Runwith  ：替换运行器,整合spring的固定写法  @Runwith(SpringJUnit4ClassRunner.class)
 *
 *   @ContextConfiguration  ：告知spring的容器，它是基于xml还是注解的
 *   location:表示基于xml的，加上classpath关键字，表示在类路径下的哪个文件
 *   classes：指定注解类所在的位置，传字节码类  即xx.class
 *
 *   ****千万记住，当我们使用spring5.x版本的时候，Junit必须是4.12及以上，否则报错,因为spring的运行器只兼容Junit4.12及以上
 */

@RunWith(SpringJUnit4ClassRunner.class)
     //@ContextConfiguration(classes = 配置类或父配置类.class)  //告诉springRunner(运行器)是哪个配置类
@ContextConfiguration(locations = "classpath:bean.xml")  //告诉springRunner(运行器)配置文件的路径，在类路径下
public class JunitWithSpring {

    @Autowired     //自动注入，不需要set方法（这是半注解方式）
    IService userService;

    @Test
    public void testService(){
        //3.用获取的对象执行方法
        List<User> users = userService.findAll();
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println("------------------------");
        List<User> list = userService.findByName("%王%");
        for (User user : list) {
            System.out.println(user);
        }
        System.out.println("--------------");
        User user = new User();
        user.setUsername("人之初");
        user.setBirthday(new Date());
        user.setSex("女");
        user.setAddress("广东");
        userService.saveUser(user);
    }

    /**
     * 测试spring的事务管理器,测试时将userService里的异常与xml配置文件里相关配置打开
     * 测试失败，不知道为什么
     */
    @Test
    public void testSpringTransManager(){
        userService.transfer("%天%","%老%","测试5");

    }
}
