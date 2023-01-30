package com.example.springbatch.job.multiple_step;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
@RequiredArgsConstructor
public class MultipleStepConfiguration {

    @Bean
    public Job examJob(JobRepository jobRepository,
                       @Qualifier("step1") Step step1,
                       @Qualifier("step2") Step step2){
        return new JobBuilder("multipleStepJob", jobRepository)
                .start(step1)
                .next(step2)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step1", jobRepository)
                .tasklet(((contribution, chunkContext) -> {

                    log.info("Execute step 1");
                    return RepeatStatus.FINISHED;
                }),platformTransactionManager).build();
    }


    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step2", jobRepository)
                .tasklet(((contribution, chunkContext) -> {

                    log.info("Execute step 2");
                    return RepeatStatus.FINISHED;
                }),platformTransactionManager).build();
    }
}
