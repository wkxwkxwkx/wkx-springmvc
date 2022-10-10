package com.springmvc.context;
import com.springmvc.annotation.AutoWired;
import com.springmvc.annotation.Controller;
import com.springmvc.annotation.Service;
import com.springmvc.exception.ContextException;
import com.springmvc.xml.XmlParse;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @project: mvcsourcecode
 * @description: 这个类可以理解为自己定义的spring容器
 * @author: 王凯旋
 * @date: 2022/7/30 14:12:52
 * @version: 1.0
 */
public class WebApplicationContext {
    //contextConfigLocation 其实就是springmvc配置文件的地址
    String contextConfigLocation;
    // classNameList 专门存储所有类的全路径
    List<String> classNameList = new ArrayList<>();
    //iocMap : 仿写的spring的ioc的bean容器
    public Map<String,Object> iocMap = new ConcurrentHashMap();

    public WebApplicationContext(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }
    /**
     * @description: 初始化spring容器 , 也就是在tomcat启动并且调用DispatcherServlet时,spring容器需要做的事
     * 1.使用dom4j解析springmvc配置文件
     * 2.获取包下的类名,并为其实例化对象,然后放入ioc容器中
     * 3.实现spring容器中对象的注入(也就是AutoWired的自动装配)
     * @author: 王凯旋
     * @date: 2022/7/30 2:23 下午
     **/
    public void refresh(){
        // 1.使用dom4j解析springmvc配置文件 , contextConfigLocation是一个字符串 : classpath:springmvc.xml
        String basePackage = XmlParse.getBasePackage(contextConfigLocation.split(":")[1]);
        // basePackage : com.qcby.controller,com.qcby.service
        String[] basePackages = basePackage.split(",");
        if(basePackages.length > 0){
            for (String pack : basePackages) {
                // com.qcby.controller
                // com.qcby.service
                executeScanPackage(pack);
            }
        }
        // 扫描后的结果样例: [com.qcby.controller.UserController, com.qcby.service.impl.UserServiceImpl, com.qcby.service.UserService]
        System.out.println("扫描之后的内容是:"+classNameList);
        // 实例化spring容器中的bean
        executeInstance();
        //IOC容器中的对象是
        System.out.println("IOC容器中的对象是"+iocMap);
        //实现spring容器中对象的注入
        executeAutoWired();
    }
    /**
     * @description: 扫描包的方法 在这里为com.qcby.controller 和 com.qcby.service两个包
     * 根据包名获取该包下的类的全类名
     * @author: 王凯旋
     * @date: 2022/7/30 2:49 下午
     * @param: pack  :  入参样例: com.qcby.controller
     **/
    public void executeScanPackage(String pack){
        //将 com.qcby.controller --->  /com/qcby/controller
        URL url = this.getClass().getClassLoader().getResource("/" + pack.replaceAll("\\.", "/"));
        //得到url对应的path路径
        String path = url.getPath();
        //根据路径,得到一个File类型的对象 , 其实就相当于是一个文件夹
        File dir = new File(path);
        //遍历这个文件夹
        for (File f : dir.listFiles()){
            if(f.isDirectory()){
                // 当前是一个文件目录 ,那么就递归调用这个方法,直到可以找到一个类文件
                //比如当前是:com.qcby.service.impl,那么就继续往下找
                executeScanPackage(pack+"."+f.getName());
            }else{
                //当前是一个文件 , 就获取类的全路径
                //如: com.qcby.service.impl.UserServiceImpl.class --->com.qcby.service.impl.UserServiceImpl
                String className = pack+"."+f.getName().replaceAll(".class","");
                //获取一个类的全路径,就将其添加到在classNameList中
                classNameList.add(className);
            }
        }
    }

    /**
     * @description: 获取到全类名之后,实例化spring容器中的bean对象 ,并将其放入IOC容器中
     * 为Controller层的类 和Service的类 实例化对象
     * @author: 王凯旋
     * @date: 2022/7/30 5:32 下午
     **/
    public void executeInstance(){
        if(classNameList.size() == 0){
            //没有需要实例化的类
            throw new ContextException("没有需要实例化的类");
        }
        try {
            for (String className : classNameList) {
                Class clazz = Class.forName(className);
                // clazz.isAnnotationPresent : 判断这个类的头上是否被某注解标注
                if (clazz.isAnnotationPresent(Controller.class)) {
                    // 此时为控制层的类 : com.qcby.controller.UserController
                    // 获取控制层对象的名字(也就是要获取当前类的类名) , 将第一个字母转为小写(默认解析为小写) : userController
                    String beanName = clazz.getSimpleName().substring(0,1).toLowerCase()+clazz.getSimpleName().substring(1);
                    // 容器里键为简单类名,值为一个已经实例化的对象
                    iocMap.put(beanName,clazz.newInstance());
                }else if (clazz.isAnnotationPresent(Service.class)){
                    // 此时为业务层的类 : com.qcby.service.impl.UserServiceImpl
                    //先拿到业务层的类上注解:Service
                    Service serviceAnnotation = (Service) clazz.getAnnotation(Service.class);
                    // 用户可能会在Service注解上自定义value为beanName , 没有定义的话就是默认首字母小写
                    String beanName = serviceAnnotation.value();
                    if("".equals(beanName)){
                        // 如果用户没有自定义Service的value属性
                        // 我们就看一下当前类实现了哪些接口
                        Class<?>[] interfaces = clazz.getInterfaces();
                        for (Class<?> anInterface : interfaces) {
                            //然后以接口的名字为键, 当前类的实例化对象为值,将其存储到iocMap容器中
                            String beanInterfaceName = anInterface.getSimpleName().substring(0,1).toLowerCase()+anInterface.getSimpleName().substring(1);
                            iocMap.put(beanInterfaceName,clazz.newInstance());
                        }
                    }else {
                        //用户自定义了Service的value属性 , 我们直接以value值为键
                        iocMap.put(beanName,clazz.newInstance());
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 该方法是为了实现spring容器中对象的注入 , 也就是自动装配
     * @author: 王凯旋
     * @date: 2022/7/30 7:25 下午
     **/
    public void executeAutoWired(){
        try {
            if(iocMap.isEmpty()){
                throw new ContextException("没有找到初始化的bean对象");
            }
            for (Map.Entry<String,Object> entry : iocMap.entrySet()){
                //使用entry迭代iocMap容器
                String key = entry.getKey();
                Object bean = entry.getValue();
                //获取bean中所有的字段  目的是为了找到controller层带有AutoWired注解的属性
                Field[] declaredFields = bean.getClass().getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    //从所有字段中找到带有AutoWired注解的字段
                    if(declaredField.isAnnotationPresent(AutoWired.class)){
                        //首先拿到AutoWired注解,因为用户可能为其value属性赋值
                        AutoWired autoWiredAnnotation = declaredField.getAnnotation(AutoWired.class);
                        String beanName = autoWiredAnnotation.value();
                        if("".equals(beanName)){
                            // 如果用户没有给AutoWired注解的value属性赋值,我们就给其一个默认值,就是其类型名 , 还是要将类名的首字母小写
                            Class<?> type = declaredField.getType();
                            beanName = type.getSimpleName().substring(0,1).toLowerCase()+type.getSimpleName().substring(1);
                        }
                        //暴力反射 ,因为属性有可能是私有的
                        declaredField.setAccessible(true);
                        // 给控制层的属性注入实例化对象
                        declaredField.set(bean,iocMap.get(beanName));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
