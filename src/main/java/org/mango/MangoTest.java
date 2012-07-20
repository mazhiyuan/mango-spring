package org.mango;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-17
 * Time: 上午9:39
 * To change this template use File | Settings | File Templates.
 */
public class MangoTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        MangoCollection collection = (MangoCollection) context.getBean("collection");

        Mazhiyuan mazhiyuan = collection.findOne().as();
        System.out.println(mazhiyuan.getName());
    }
}
