package main;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import quoters.Quoter;

public class Main {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        context.getBean(Quoter.class).sayQuote();






    }
}
