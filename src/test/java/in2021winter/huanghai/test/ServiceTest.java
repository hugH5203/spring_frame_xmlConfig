package in2021winter.huanghai.test;

import in2021winter.huanghai.domain.User;
import in2021winter.huanghai.service.IService;
import in2021winter.huanghai.serviceImpl.UserService;
import in2021winter.huanghai.utils.TransActionManager;
import org.junit.Test;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**基于xml的spring测试，（IOC解耦，Aop面向切面，一系列通知）还有事务控制，
 * @author HuangHai
 * @date 2021/2/6 13:04
 */
public class ServiceTest {

    /**
     * 该方法仅用于测试最基本的IOC解耦
     */
    @Test
    public void testService(){
    //1.获取xml配置的spring容器
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
    //2.根据id在spring容器中获取对象
        UserService userService = context.getBean("userService", UserService.class);
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
@Test
public void testUpdate(){
    //1.获取xml配置的spring容器
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
    //2.根据id在spring容器中获取对象
    UserService userService = context.getBean("userService", UserService.class);
    //3.用获取的对象执行方法
    User user = userService.findByName("%王%").get(0);
    user.setAddress("南京");
    userService.updateUser(user);
}

    /**
     * 测试事务的安全性
     */
    @Test
public void testTransfer(){
    //1.获取xml配置的spring容器
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
    //2.根据id在spring容器中获取对象
    UserService userService = context.getBean("userService", UserService.class);
    //3.用获取的对象执行方法
    userService.transfer("%天%","%王%","男2");
}

    /**
     * 测试xml配置的（自己写的）动态代理，使用时得关闭spring中aop的配置
     */
    @Test
public void testCodeProxy(){
    //1.获取xml配置的spring容器
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
    //2.根据id在spring容器中获取对象
    IService service = context.getBean("proxyService", IService.class);
    service.transfer("%天%","%保%","长1");
}


    /**
     * 自己手写的，用纯代码写的动态代理，测试看看,使用时得关闭spring中aop的配置。和上面那个区别不大。
     */
    @Test
    public void testProxy (){

    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
    final   UserService service=context.getBean("userService", UserService.class);
    final TransActionManager trans = context.getBean("transManager", TransActionManager.class);
       IService se= (IService) Proxy.newProxyInstance(service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                new InvocationHandler() {

                    /**
                     * 被代理对象的任何方法都会经过该方法
                     * @param proxy:要返回的代理对象（已经在被代理对象的基础上进行了增强）
                     * @param method ：当前执行的方法（可以看做一个万能方法，也可以是所有方法的结合，它就代表着所有方法）
                     * @param args ：当前方法所需要的参数
                     * @return  和调用的被代理方法有相同的返回值
                     * @throws Throwable
                     */
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //这里面写要增强的代码
                        Object returnValue=null;
                        /*
                        获取所有方法的参数可以根据被代理对象的的参数数量来获取：即args[下标]
                        判断调用的方法是不是某个方法："方法名".equals(method.getName()),if(),else()
                        接下来才是我要写的（上面只是为了方便记忆）.*/
                        try {
                            trans.beginTransAction();   //开启事务，即设置自动提交为false
                            returnValue=method.invoke(service,args);//用反射进行事务
                            trans.commit();   //事务提交
                            return returnValue;
                        } catch (Exception e) {
                            trans.rollBack();  //事务出现异常就进行回滚
                            System.out.println("事务出现异常");
                            throw new RuntimeException(e);
                        }finally {
                            trans.release();  //释放资源
                        }
                    }
        });
       se.transfer("%天%","%保%","嘿嘿");
                }


    /**
     * 测试spring的xml配置的动态代理即Aop切面，如果关掉了aop的设置，就是一个普通的没有被增强的对象，用时打开xml里aop的配置
     */
    @Test
public void testSpringAutoProxy(){
    //1.获取xml配置的spring容器
    ClassPathXmlApplicationContext context1 = new ClassPathXmlApplicationContext("bean.xml");
    //2.根据id在spring容器中获取对象
    IService userService = context1.getBean("userService", IService.class);
    //3.用获取的对象执行方法
    List<User> users = userService.findAll();
    for (User user : users) {
        System.out.println(user);
    }
}
}
