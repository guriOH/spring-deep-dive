package com.example.springbatch.job.conditional_step;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Random;

@Slf4j
@Configuration
public class DeciderJobConfiguration {


    @Bean
    public Job decideStepJob(JobRepository jobRepository){
        return new JobBuilder("decideStepJob", jobRepository)
                .start(decideStep1(null,null))
                .next(decider())
                .from(decider())
                    .on("ODD")
                    .to(decideStep2(null,null))
                .from(decider())
                    .on("EVEN")
                    .to(decideStep3(null,null))
                .end()
                .build();
    }

    @Bean
    public Step decideStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("decideStep1", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("######### Execute Step 1");
                    return RepeatStatus.FINISHED;
                }),platformTransactionManager).build();
    }

    @Bean
    public Step decideStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("decideStep2", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("######### Execute Step 2");
                    return RepeatStatus.FINISHED;
                }),platformTransactionManager).build();
    }

    @Bean
    public Step decideStep3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("decideStep3", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("######### Execute Step 3");
                    return RepeatStatus.FINISHED;
                }),platformTransactionManager).build();
    }


    @Bean
    public JobExecutionDecider decider(){
        return ((jobExecution, stepExecution) -> {
            Random rand = new Random();

            int randomNumber = rand.nextInt(50) + 1;
            log.info("랜덤숫자: {}", randomNumber);

            if(randomNumber % 2 == 0) {
                return new FlowExecutionStatus("EVEN");
            } else {
                return new FlowExecutionStatus("ODD");
            }
        });

    }


}
