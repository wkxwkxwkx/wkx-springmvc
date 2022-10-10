package com.springmvc.xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * @project: mvcsourcecode
 * @description: 这个类可以理解为自己定义的xml解析器,解析mvc配置文件
 * @author: 王凯旋
 * @date: 2022/7/30 14:27:54
 * @version: 1.0
 */
public class XmlParse {
    public static void main(String[] args) {
    }

    /**
     * @description: 读取springmvc配置文件需要扫描的包,也就是获取base-package的属性值
     * @author: 王凯旋
     * @date: 2022/7/30 2:38 下午
     * @param: xml :入参样例:springmvc.xml
     * @return: java.lang.String
     **/
    public static String getBasePackage(String xml){
        try {
            SAXReader saxReader = new SAXReader();
            // 读取配置文件,获取一个输入流
            InputStream inputStream = XmlParse.class.getClassLoader().getResourceAsStream(xml);
            // 读取流获得一个dom对象
            Document document = saxReader.read(inputStream);
            //获取到根节点
            Element rootElement = document.getRootElement();
            //找到对应节点,此处为component-scan标签对应的节点
            Element componentScan = rootElement.element("component-scan");
            //找到节点后,通过attribute方法,获得base-package的属性值
            Attribute attribute = componentScan.attribute("base-package");
            String basePackage = attribute.getText();
            return basePackage;
            // 返回的basePackage样例为 : com.qcby.controller,com.qcby.service
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return "";
    }

}
