package com.example.springbatch.job.conditional_step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class ConditionalStepConfiguration {

    @Bean
    public Job conditionalStepJob(JobRepository jobRepository, @Qualifier("conditionalStep1") Step step){
        return new JobBuilder("conditionalStepJob", jobRepository)
                .start(conditionalStep1(null,null))
                    .on("FAILED")
                    .to(conditionalStep2(null,null))
                    .on("*")
                    .end()
                .from(conditionalStep1(null,null))
                    .on("*")
                    .to(conditionalStep3(null,null))
                    .next(conditionalStep2(null,null))
                    .on("*")
                    .end()
                .end()
                .build();
    }

    @Bean
    public Step conditionalStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("conditionalStep1", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("######### Execute Step 1");

                    // 분기를 위한 상태값은 BatchStatus가 아닌 ExitStatus 이다.
                    // BatchStatus 는 Job또는 Step의 실행 결과값 ExistStatus는 스텝의 실행 후 상태값
                    // 분기를 위해서는 ExitStatus를 조정해야한다.
                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }),platformTransactionManager).build();
    }

    @Bean
    public Step conditionalStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("conditionalStep2", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("######### Execute Step 2");
                    return RepeatStatus.FINISHED;
                }),platformTransactionManager).build();
    }

    @Bean
    public Step conditionalStep3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("conditionalStep3", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("######### Execute Step 3");
                    return RepeatStatus.FINISHED;
                }),platformTransactionManager).build();
    }
}
