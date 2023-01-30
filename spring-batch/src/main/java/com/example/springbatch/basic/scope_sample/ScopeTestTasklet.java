package com.example.springbatch.basic.scope_sample;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.management.ThreadInfo;

@Slf4j
@Component
@StepScope
public class ScopeTestTasklet implements Tasklet {


    @Value("#{jobParameters[a]}")
    int a;
//
//    @Value("#{jobParameters[b]}")
//    int b;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        System.out.println(Thread.currentThread().getName()+" " + "value : " + a);
        a += 1;

        return RepeatStatus.FINISHED;
    }
}
