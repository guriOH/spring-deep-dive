package com.example.springbatch.job.flow_step;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FlowStepConfiguration {


//    @Bean
//    public Job examJob(JobRepository jobRepository, Flow flow){
//        return new JobBuilder("flowStepJob", jobRepository)
//                .start(flow)
//                .build();
//    }
//
//
//    @Bean
//    public Flow flow(){
//        return Flow
//    }
}
