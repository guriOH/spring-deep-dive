package com.example.springbatch.job.asyncprocessing;


import com.example.springbatch.utils.DataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class AsyncJobConfiguration {

    @Bean
    public Job asyncJob(JobRepository jobRepository){
        return new JobBuilder("asyncJob", jobRepository)
                .start(asyncStep(null,null))
                .build();
    }

    @Bean
    @StepScope
    public Step asyncStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("asyncStep", jobRepository)
                .<String, String>chunk(100, platformTransactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    @StepScope
    protected ItemReader<String> itemReader() {
        return new ListItemReader<>(DataUtil.getItems());
    }

    @Bean
    @StepScope
    protected ItemProcessor<String, String> itemProcessor() {
        return item -> item + ", Spring Batch";
    }

    @Bean
    @StepScope
    protected ItemWriter<String> itemWriter() {
        return items -> log.info("chunk item size : {}", items.size());
    }



}
