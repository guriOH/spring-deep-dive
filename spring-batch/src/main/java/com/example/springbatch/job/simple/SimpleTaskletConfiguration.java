package com.example.springbatch.job.simple;


import com.example.springbatch.job.tasklet.SimpleTasklet;
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
@RequiredArgsConstructor
@Configuration
public class SimpleTaskletConfiguration  {


    @Bean
    public Job simpleLambdaJob(JobRepository jobRepository, @Qualifier("simpleLambdaStep") Step step){
        return new JobBuilder("simpleLambdaJob",jobRepository)
                .start(step)
                .build();
    }

    @Bean
    public Job simpleTaskletJob(JobRepository jobRepository,@Qualifier("simpleTaskletStep") Step step){
        return new JobBuilder("simpleTaskletJob",jobRepository)
                .start(step)
                .build();
    }


    @Bean
    public Step simpleLambdaStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleLambdaStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("Start simple tasklet");
                    return RepeatStatus.FINISHED;
                }),platformTransactionManager).build();
    }

    @Bean
    public Step simpleTaskletStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleTaskletStep", jobRepository)
                .tasklet(new SimpleTasklet(), platformTransactionManager).build();
    }

}
