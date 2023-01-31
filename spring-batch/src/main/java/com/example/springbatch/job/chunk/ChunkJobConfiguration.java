package com.example.springbatch.job.chunk;


import com.example.springbatch.job.chunk.listener.CompleteStepListener;
import com.example.springbatch.common.utils.DataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class ChunkJobConfiguration {

    private final CompleteStepListener completeStepListener;

    @Bean
    public Job chunkSampleJob(JobRepository jobRepository){
        return new JobBuilder("chunkSampleJob", jobRepository)
                .start(taskBaseStep(null,null))
                .next(chunkSampleStep(null, null))
                .build();
    }


    @Bean
    public Step taskBaseStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("taskBaseStep",jobRepository)
                .tasklet(this.chunkTasklet(),platformTransactionManager)
                .listener(completeStepListener)
                .build();
    }

    @Bean
    public Step chunkSampleStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {

        return new StepBuilder("chunkSampleStep", jobRepository)
                .<String, String>chunk(100, platformTransactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .listener(completeStepListener)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet chunkTasklet(){
        return ((contribution, chunkContext) -> {
            log.info("Start test chunk tasklet");
            return RepeatStatus.FINISHED;
        });
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

//
//    @Bean
//    @StepScope
//    public Tasklet tasklet(@Value("#{jobParameters[chunkSize]}") String value) {
//        List<String> items = getItems();
//        return ((contribution, chunkContext) -> {
//            StepExecution stepExecution = contribution.getStepExecution();
////            JobParameters jobParameters = stepExecution.getJobParameters();
////            String value = jobParameters.getString("chunkSize", "10");
//            int chunkSize = StringUtils.isNotEmpty(value) ? Integer.parseInt(value) : 10;
//
//
//            int fromIndex = (int) stepExecution.getReadCount();
//            int toIndex = fromIndex + chunkSize;
//
//            if(fromIndex >= items.size()) {
//                return RepeatStatus.FINISHED;
//            }
//
//            List<String> subList = items.subList(fromIndex, toIndex);// 0번부터 9번까지
//
//            log.info("task item size :{}", subList.size());
//
//            stepExecution.setReadCount(toIndex);
//
//            return RepeatStatus.CONTINUABLE;
//        });
//    }


}
