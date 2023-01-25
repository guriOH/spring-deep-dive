package com.example.springdeepdive;

import org.apache.logging.log4j.util.Base64Util;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class SpringDeepDiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDeepDiveApplication.class, args);

        String result =Base64Util.encode("2309784:2e2188f67667f47cf233ab92c514d15e");

        System.out.println(result);


    }

}

