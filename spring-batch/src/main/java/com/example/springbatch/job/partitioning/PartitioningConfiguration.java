//package com.example.springbatch.job.partitioning;
//
//
//import com.example.springbatch.common.utils.DataUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.partition.support.Partitioner;
//import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.support.ListItemReader;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.transaction.PlatformTransactionManager;
//
//
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class PartitioningConfiguration {
//
//
//
//    @Bean
//    public Job partitionJob(JobRepository jobRepository){
//        return new JobBuilder("partitionJob", jobRepository)
//                .start(masterStep(null, null))
//                .build();
//    }
//
//    @Bean
//    public Step slaveStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("asyncStep", jobRepository)
//                .<String, String>chunk(1, platformTransactionManager)
//                .reader(itemPartitionReader())
//                .processor(itemPartitionProcessor())
//                .writer(itemPartitionWriter())
//                .build();
//    }
//
//    @Bean
//    public Step masterStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("masterStep",jobRepository)
//                .partitioner("orderCollectSlave", customPartitioner())
//                .partitionHandler(partitionHandler(null,null))
//                .build();
//    }
//
//    @Bean
//    public Partitioner customPartitioner() {
//        return new SamplePartitioner();
//    }
//
//    @Bean
//    @StepScope
//    public TaskExecutorPartitionHandler partitionHandler(
//            @Qualifier("slaveStep") Step step,
//            @Qualifier("executor") TaskExecutor executor) {
//        TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler();
//        partitionHandler.setStep(step);
//        partitionHandler.setTaskExecutor(executor);
//        partitionHandler.setGridSize(5);
//        return partitionHandler;
//    }
//
//    @Bean("executor")
//    @StepScope
//    public TaskExecutor executor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(5);
//        executor.setMaxPoolSize(5);
//        executor.setThreadNamePrefix("partition-thread");
//        executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
//        executor.initialize();
//        return executor;
//    }
//
//    @Bean
//    @StepScope
//    protected ItemReader<String> itemPartitionReader() {
//        return new ListItemReader<>(DataUtil.getItems());
//    }
//
//    @Bean
//    @StepScope
//    protected ItemProcessor<String, String> itemPartitionProcessor() {
//        return x -> x + ", Spring Batch in " + Thread.currentThread().getName();
//    }
//
//    @Bean
//    @StepScope
//    protected ItemWriter<String> itemPartitionWriter() {
//        return (x) -> log.info("chunk item size :");
//    }
//
//
//
//}
