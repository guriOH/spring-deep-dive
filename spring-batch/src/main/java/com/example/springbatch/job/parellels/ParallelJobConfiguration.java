package com.example.springbatch.job.parellels;


import com.example.springbatch.utils.DataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.FlowJobBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class ParallelJobConfiguration {

    @Bean
    public Job parallelJob(JobRepository jobRepository){
        return new JobBuilder("parallelJob", jobRepository)
                .start(splitFlow())
                .next(parallelTaskletSecond(null,null))
                .build().build();
    }


    @Bean
    public TaskExecutor taskExecutor(){
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    @Bean
    public Flow splitFlow() {
        return new FlowBuilder<Flow>("splitFlow")
                .split(taskExecutor())
                .add(flow1(),flow2())
                .build();
    }

    @Bean
    public Flow flow1() {
        return new FlowBuilder<Flow>("flow1")
                .start(parallelTaskletFirst(null,null))
                .build();
    }

    @Bean
    public Flow flow2() {
        return (Flow) new FlowBuilder("flow2")
                .start(parallelTaskletSecond(null,null))
                .build();
    }


    @Bean
    public Step parallelTaskletFirst(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("parallelTaskletFirst", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    for (int i = 0; i < 100; i++) {
                        System.out.println(Thread.currentThread().getName()+ " parallelTaskletFirst : " + i);
                    }
                    return RepeatStatus.FINISHED;
                }),platformTransactionManager)
                .build();
    }

    @Bean
    public Step parallelTaskletSecond(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("parallelTaskletSecond", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    for (int i = 0; i < 100; i++) {
                        System.out.println(Thread.currentThread().getName()+ " parallelTaskletSecond : " + i);
                    }
                    return RepeatStatus.FINISHED;
                }),platformTransactionManager)
                .build();
    }

//    @Bean
//    @StepScope
//    public Step parallelStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("parallelStep", jobRepository)
//                .<String, String>chunk(100, platformTransactionManager)
//                .reader(itemReader())
//                .processor(itemProcessor())
//                .writer(itemWriter())
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    protected ItemReader<String> itemReader() {
//        return new ListItemReader<>(DataUtil.getItems());
//    }
//
//    @Bean
//    @StepScope
//    protected ItemProcessor<String, String> itemProcessor() {
//        return item -> item + ", Spring Batch";
//    }
//
//    @Bean
//    @StepScope
//    protected ItemWriter<String> itemWriter() {
//        return items -> log.info("chunk item size : {}", items.size());
//    }
//


}
