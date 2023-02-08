package com.example.springaop.sample;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Component
public class SampleAspect {


    @Before("execution(* com.example.springaop.sample.*Test.*(..))")
    public void beforeAdvice(JoinPoint joinPoint){
        // 시작 부분
        System.out.println("===== Before Advice =====");
        // 날짜 출력
        System.out.println(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        // 메서드 이름 출력
        System.out.println(String.format("메서드:%s",joinPoint.getSignature().getName()));
    }

    @After("execution(* com.example.springaop.sample.*Test.*(..))")
    public void afterAdvice(JoinPoint joinPoint){
        // 시작 부분
        System.out.println("===== After Advice =====");
        // 날짜 출력
        System.out.println(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        // 메서드 이름 출력
        System.out.println(String.format("메서드:%s",joinPoint.getSignature().getName()));
    }

    @Around("execution(* com.example.springaop.sample.*Test.*(..))")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        // 시작 부분
        System.out.println("===== Around Advice =====");

        System.out.println("######### start #########");
        // 지정한 클래스의 메서드 실행
        Object result= joinPoint.proceed();
        System.out.println("######### end #########");
        // 반환값을 돌려줄 필요가있는 경우에는 Object 타입의 반환값을 돌려준다.
        return result;
    }
}
