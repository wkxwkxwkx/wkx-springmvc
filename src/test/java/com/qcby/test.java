package com.qcby;

import com.springmvc.xml.XmlParse;
import org.junit.Test;

/**
 * @project: mvcsourcecode
 * @description: 测试类
 * @author: 王凯旋
 * @date: 2022/7/30 14:39:50
 * @version: 1.0
 */
public class test {

    @Test
    public void getBase(){
        String basePackage = XmlParse.getBasePackage("springmvc.xml");
        System.out.println(basePackage);
    }

}
