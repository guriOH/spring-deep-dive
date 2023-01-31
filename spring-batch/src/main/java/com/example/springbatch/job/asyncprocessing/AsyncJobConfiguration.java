package com.example.springbatch.job.asyncprocessing;


import com.example.springbatch.common.utils.DataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.concurrent.Future;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class AsyncJobConfiguration {

    @Bean
    public Job asyncJob(JobRepository jobRepository, @Qualifier("asyncStep") Step step){
        return new JobBuilder("asyncJob", jobRepository)
                .start(step)
                .build();
    }

    @Bean
    public Step asyncStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("asyncStep", jobRepository)
                .<String, Future<String>>chunk(100, platformTransactionManager)
                .reader(itemAsyncReader())
                .processor(asyncProcessor())
                .writer(asyncWriter())
                .taskExecutor(taskAsyncExecutor())
                .build();
    }

    @Bean
    @StepScope
    protected ItemReader<String> itemAsyncReader() {
        return new ListItemReader<>(DataUtil.getItems());
    }

    @Bean
    @StepScope
    protected ItemProcessor<String, String> itemAsyncProcessor() {
        return x -> x + ", Spring Batch in " + Thread.currentThread().getName();
    }

    @Bean
    @StepScope
    protected ItemWriter<String> itemAsyncWriter() {
        return (x) -> log.info("chunk item size :");
    }

    @Bean
    public AsyncItemProcessor<String, String> asyncProcessor(){
        AsyncItemProcessor<String, String> processors = new AsyncItemProcessor<String, String>();
        processors.setDelegate(itemAsyncProcessor());
//        processors.setTaskExecutor(taskAsyncExecutor());
        return processors;
    }

    @Bean
    public AsyncItemWriter<String> asyncWriter() {
        AsyncItemWriter<String> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(itemAsyncWriter());
        return asyncItemWriter;
    }


    @Bean
    public TaskExecutor taskAsyncExecutor(){
        return new SimpleAsyncTaskExecutor("spring_batch");
    }


}
