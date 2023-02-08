package com.example.springaop;

import com.example.springaop.sample.SampleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringAopApplication {
    public static void main(String[] args) {

        SpringApplication.run(SpringAopApplication.class, args)
                .getBean(SpringAopApplication.class)
                .execute();

    }


    @Autowired
    SampleTest sampleTest;


    private void execute(){
        sampleTest.sample1Test();
//        sampleTest.sample2Test();
    }


}
