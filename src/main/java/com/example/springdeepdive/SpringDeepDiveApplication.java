package com.example.springdeepdive;

import org.apache.logging.log4j.util.Base64Util;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class SpringDeepDiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDeepDiveApplication.class, args);

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(TestConfiguration.class);
        ctx.refresh();
        Cat cat = (Cat)ctx.getBean("cat");;

        cat.setName("Test11");


        Cat cat2 = (Cat)ctx.getBean("cat");;
        System.out.println("asd");

    }

}

