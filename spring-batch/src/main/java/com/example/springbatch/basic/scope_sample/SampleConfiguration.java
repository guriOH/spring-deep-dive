package com.example.springbatch.basic.scope_sample;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class SampleConfiguration {

    private final ScopeTestTasklet scopeTestTasklet;

    @Bean
    public Job exampleJob(JobRepository jobRepository) {
        return new JobBuilder("scopeSampleJob", jobRepository)
                .start(scopeSampleTaskletJob(null,null))
                .preventRestart()
                .build();
    }


    @Bean
    public Step scopeSampleJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("scopeSampleStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("Scope sample test");
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .taskExecutor(executor())
                .build();
    }

    @Bean
    public Step scopeSampleTaskletJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("scopeSampleTaskletStep", jobRepository)
                .tasklet(scopeTestTasklet, transactionManager)
                .taskExecutor(executor())
                .build();
    }


    @Bean
    public TaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor(); // (2)
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setThreadNamePrefix("multi-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();
        return executor;
    }
}
