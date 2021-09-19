package in2021winter.huanghai.factory;

import in2021winter.huanghai.service.IService;
import in2021winter.huanghai.serviceImpl.UserService;
import in2021winter.huanghai.utils.TransActionManager;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author HuangHai
 * @date 2021/2/6 22:57
 */
public class BeanFactory {
    private TransActionManager trans;
    private IService service;  //xml配置时，必须是接口类型，否则spring认为错误。如果是纯代码没要spring的IOC配置生成，就可以是实现类或接口

    public  void setTrans(TransActionManager trans) {
        this.trans = trans;
    }

    public final void setService(IService service) {
        this.service = service;
    }

    /**
            * 测试手写的动态代理，即事务控制，增强所有service层的方法
 * 动态代理：
            * 特点：字节码随用随创建，随用随加载（本身就是用反射来执行方法）
            * 作用：在不改变源码的基础上对方法增强
 * 分类：基于接口的动态代理与基于子类的动态代理
 * 基于接口的必须实现了至少一个接口，本次之测试基于接口的动态代理，因为我对这个熟悉些
 * 涉及的类：Proxy
 * 提供者：JDK官方
 * 如何创建代理对象：
            *   使用Proxy中的newProInstance方法,该方法返回一个代理对象
 *   该方法的参数：
            *   ClassLoader：类加载器，加载该类的字节码等信息
 *   Class[]：支持多个字节码数组，即被代理类所实现了的接口或继承了的子类，旨在让方法知道你要增强的方法
 *   InvocationHandler：一个接口，其中有一个方法叫invoke,用于提供增强的代码，即怎么增强，由自己写。通常都是用匿名内部类实现该接口
 */
    public IService getService(){
        //动态代理方法实现事务安全
    /*获取代理对象service。只能强转也必须强转成接口对象，因为实现同一个接口的具体方法可以不同，所以才达到增强被代理类的效果。
     如果没强转成被代理类类型，则代表同一个类的两个实例方法可以不同，自相矛盾了。*/
        return (IService) Proxy.newProxyInstance(service.getClass().getClassLoader(),
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
    }
}
