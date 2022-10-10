package com.springmvc.servlet;

import com.springmvc.annotation.Controller;
import com.springmvc.annotation.RequestMapping;
import com.springmvc.context.WebApplicationContext;
import com.springmvc.exception.ContextException;
import com.springmvc.handler.MyHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @project: mvcsourcecode
 * @description: 自己定义的DispatcherServlet , mvc的核心控制器
 * @author: 王凯旋
 * @date: 2022/7/30 12:18:51
 * @version: 1.0
 */
public class DispatcherServlet extends HttpServlet {

    private WebApplicationContext webApplicationContext;
    //handlerList集合中 存储 url和controller层方法 的映射关系
    List<MyHandler> handlerList = new ArrayList();

    @Override
    public void init() throws ServletException {
        // 1.servlet初始化的时候,读取配置文件springmvc.xml ,获取的contextConfigLocation字符串为 : classpath:springmvc.xml
        String contextConfigLocation = this.getServletConfig().getInitParameter("contextConfigLocation");
        // 2.创建Spring容器,为了解析springmvc配置文件
        webApplicationContext = new WebApplicationContext(contextConfigLocation);
        // 3.初始化spring容器
        webApplicationContext.refresh();
        // 4.初始化请求映射 : 用户通过/user/query路径访问-->我们给用户匹配相应的controller -->然后匹配相应的method -->然后进行parameter传递
        initHandleMapping();
        System.out.println("请求地址和控制器内方法的映射关系:"+handlerList);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //进行请求的分发处理
        executeDispatch(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }

    /**
     * @description: 初始化请求映射 : 找到url对应的类下的方法
     * @author: 王凯旋
     * @date: 2022/7/30 8:33 下午
     **/
    public void initHandleMapping(){
        if(webApplicationContext.iocMap.isEmpty()){
            throw new ContextException("spring容器为空");
        }
        for (Map.Entry<String,Object> entry:webApplicationContext.iocMap.entrySet()){
            //从ioc容器中找到类名
            Class<?> clazz = entry.getValue().getClass();
            if(clazz.isAnnotationPresent(Controller.class)){
                //如果当前类为Controller层 , 我们就取出该类的所有方法
                Method[] declaredMethods = clazz.getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                        // 迭代所有方法时,找到带有RequestMapping注解的方法
                        // 我们取出这个注解,获取到注解value属性的值(就是路径url:/user/query)
                        RequestMapping requestMappingAnnotation = declaredMethod.getAnnotation(RequestMapping.class);
                        // url: /user/query
                        String url = requestMappingAnnotation.value();
                        //获取到对应的controller类和类下带有RequestMapping的方法和url后,将其放入handlerList
                        handlerList.add(new MyHandler(url,entry.getValue(),declaredMethod));
                    }
                }
            }
        }
    }

    /**
     * @description: 请求分发处理
     * 这个方法完成的功能 : 用户请求打到DispatcherServlet之后,通过这个方法可以知道需要分发到哪个处理器
     * @author: 王凯旋
     * @date: 2022/7/30 9:04 下午
     * @param: request
     * @param: response
     **/
    public void executeDispatch(HttpServletRequest request, HttpServletResponse response){
        MyHandler handler = getHandler(request);
        try {
            //用户路径找不到对应的handler
            if(handler == null){
                response.getWriter().print("<h1>404 NOT FOUND</ h1>");
            }else {
                // 我们需要拿到handler对应的方法中的所有参数
                Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
                // 定义一个参数的数组
                Object[] params = new Object[parameterTypes.length];
                // 获取请求中的参数集合
                Map<String, String[]> parameterMap = request.getParameterMap();
                //遍历请求中的参数,这里要将request和response除外
                for (Map.Entry<String,String[]> entry : parameterMap.entrySet()){
                    //我们模拟的方法只有一个name参数,所以我们直接用[0]获取 ,这里写的有瑕疵
                    String nameParam = entry.getValue()[0];
                    // 这里因为我们预先知道第三个参数是name , 所以可以直接这么赋值,后期待优化
                    params[2] = nameParam;
                }
                params[0] = request;
                params[1] = response;
                //获取到参数之后,来调用控制器的方法
                handler.getMethod().invoke(handler.getController(),params);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @description: 获取请求对应的handler
     * @author: 王凯旋
     * @date: 2022/7/30 9:06 下午
     * @param: request
     * @return: com.springmvc.handler.MyHandler
     **/
    private MyHandler getHandler(HttpServletRequest request){
        // requestURI 就是 RequestMapping注解上的 /user/query
        String requestURI = request.getRequestURI();
        //获取到用户访问的url之后,我们要去对比下处理器映射器集合中是否有对应的handler
        for (MyHandler myHandler : handlerList) {
            if (myHandler.getUrl().equals(requestURI)) {
                //处理器映射器中的url如果和用户访问的url相等,我们就返回这个handler
                return myHandler;
            }
        }
        return null;
    }

}
